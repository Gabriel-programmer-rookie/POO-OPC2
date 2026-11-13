import java.awt.*;
import java.util.List;
import javax.swing.*;

public class BibliotecaGUI extends JFrame {
    private final BibliotecaManager manager;

    private TabelaEncapsulada tblLivros;
    private TabelaEncapsulada tblRevistas;
    private TabelaEncapsulada tblDVDs;
    private TabelaEncapsulada tblUsuarios;
    private TabelaEncapsulada tblEmprestimos;

    public BibliotecaGUI() {
        manager = new BibliotecaManager();

        setTitle("Sistema de Gestão de Biblioteca Universitária");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Livros", criarPainelLivros());
        tabbedPane.addTab("Revistas", criarPainelRevistas());
        tabbedPane.addTab("DVDs", criarPainelDVDs());
        tabbedPane.addTab("Usuários", criarPainelUsuarios());
        tabbedPane.addTab("Empréstimos e Devoluções", criarPainelEmprestimos());

        add(tabbedPane);
        atualizarTodasTabelas();
    }

    // --- PAINEL LIVROS ---
    private JPanel criarPainelLivros() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel pnlBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtBuscaTitulo = new JTextField(12);
        JTextField txtBuscaAutor = new JTextField(12);
        JCheckBox chkDisp = new JCheckBox("Apenas Disponíveis");
        JButton btnBuscar = new JButton("Filtrar");
        JButton btnLimpar = new JButton("Mostrar Todos");

        pnlBusca.add(new JLabel("Título:")); pnlBusca.add(txtBuscaTitulo);
        pnlBusca.add(new JLabel("Autor:")); pnlBusca.add(txtBuscaAutor);
        pnlBusca.add(chkDisp);
        pnlBusca.add(btnBuscar); pnlBusca.add(btnLimpar);
        panel.add(pnlBusca, BorderLayout.NORTH);

        String[] colunas = {"ID", "Título", "Ano", "Autor", "Nº Páginas", "Status"};
        tblLivros = new TabelaEncapsulada(colunas);
        panel.add(new JScrollPane(tblLivros), BorderLayout.CENTER);

        JPanel pnlSul = new JPanel(new BorderLayout());
        JPanel pnlCadastro = new JPanel(new GridLayout(3, 4, 5, 5));
        pnlCadastro.setBorder(BorderFactory.createTitledBorder("Cadastrar Novo Livro"));

        JTextField txtId = new JTextField();
        JTextField txtTitulo = new JTextField();
        JTextField txtAno = new JTextField();
        JTextField txtAutor = new JTextField();
        JTextField txtPaginas = new JTextField();
        JButton btnCadastrar = new JButton("Cadastrar Livro");

        pnlCadastro.add(new JLabel("ID:")); pnlCadastro.add(txtId);
        pnlCadastro.add(new JLabel("Título:")); pnlCadastro.add(txtTitulo);
        pnlCadastro.add(new JLabel("Ano:")); pnlCadastro.add(txtAno);
        pnlCadastro.add(new JLabel("Autor:")); pnlCadastro.add(txtAutor);
        pnlCadastro.add(new JLabel("Nº Páginas:")); pnlCadastro.add(txtPaginas);
        pnlCadastro.add(new JLabel("")); pnlCadastro.add(btnCadastrar);

        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEditar = new JButton("Editar Selecionado");
        JButton btnRemover = new JButton("Remover Selecionado");
        pnlAcoes.add(btnEditar); pnlAcoes.add(btnRemover);

        pnlSul.add(pnlCadastro, BorderLayout.CENTER);
        pnlSul.add(pnlAcoes, BorderLayout.SOUTH);
        panel.add(pnlSul, BorderLayout.SOUTH);

        btnCadastrar.addActionListener(e -> {
            String idStr = txtId.getText().trim();
            String titStr = txtTitulo.getText().trim();
            String anoStr = txtAno.getText().trim();
            String autStr = txtAutor.getText().trim();
            String pagStr = txtPaginas.getText().trim();

            if (idStr.isEmpty() || !idStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Coloque apenas números no ID!", "Erro no ID", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (titStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo Título é obrigatório!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (anoStr.isEmpty() || !anoStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Coloque apenas números no Ano!", "Erro no Ano", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (autStr.isEmpty() || !autStr.matches("^[\\p{L}\\s]+$")) {
                JOptionPane.showMessageDialog(this, "Não pode conter números, apenas letras no Autor!", "Erro no Autor", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (pagStr.isEmpty() || !pagStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Coloque apenas números no Número de Páginas!", "Erro no Nº Páginas", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                manager.adicionarItem(new Livro(
                    Integer.parseInt(idStr),
                    titStr,
                    Integer.parseInt(anoStr),
                    autStr,
                    Integer.parseInt(pagStr)
                ));
                JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");
                atualizarTabelaLivros();
                txtId.setText(""); txtTitulo.setText(""); txtAno.setText(""); txtAutor.setText(""); txtPaginas.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEditar.addActionListener(e -> {
            int row = tblLivros.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um livro na tabela.");
                return;
            }
            int id = (int) tblLivros.obterValor(row, 0);
            try {
                String nTit = JOptionPane.showInputDialog(this, "ID " + id + " (Imutável) - Novo Título:", tblLivros.obterValor(row, 1));
                if (nTit == null || nTit.isBlank()) return;

                String anoStr = JOptionPane.showInputDialog(this, "Novo Ano:", tblLivros.obterValor(row, 2));
                if (anoStr == null || !anoStr.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Coloque apenas números no Ano!");
                    return;
                }

                String nAut = JOptionPane.showInputDialog(this, "Novo Autor:", tblLivros.obterValor(row, 3));
                if (nAut == null || !nAut.matches("^[\\p{L}\\s]+$")) {
                    JOptionPane.showMessageDialog(this, "Não pode conter números, apenas letras no Autor!");
                    return;
                }

                String pagStr = JOptionPane.showInputDialog(this, "Novas Páginas:", tblLivros.obterValor(row, 4));
                if (pagStr == null || !pagStr.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Coloque apenas números no Número de Páginas!");
                    return;
                }

                manager.editarLivro(id, nTit.trim(), Integer.parseInt(anoStr), nAut.trim(), Integer.parseInt(pagStr));
                atualizarTabelaLivros();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage());
            }
        });

        btnRemover.addActionListener(e -> {
            int row = tblLivros.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um livro na tabela.");
                return;
            }
            int id = (int) tblLivros.obterValor(row, 0);
            try {
                manager.removerItem(id);
                atualizarTabelaLivros();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBuscar.addActionListener(e -> {
            String t = txtBuscaTitulo.getText().trim();
            String a = txtBuscaAutor.getText().trim();
            boolean disp = chkDisp.isSelected();
            List<ItemBiblioteca> res = manager.getAcervo();

            if (!t.isEmpty()) res = manager.pesquisarPorTitulo(t, disp);
            else if (!a.isEmpty()) res = manager.pesquisarPorAutor(a);
            else if (disp) res = manager.pesquisarPorDisponibilidade(true);

            preencherTabelaLivros(res);
        });

        btnLimpar.addActionListener(e -> {
            txtBuscaTitulo.setText(""); txtBuscaAutor.setText(""); chkDisp.setSelected(false);
            atualizarTabelaLivros();
        });

        return panel;
    }

    // --- PAINEL REVISTAS ---
    private JPanel criarPainelRevistas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] colunas = {"ID", "Título", "Ano", "Edição", "Status"};
        tblRevistas = new TabelaEncapsulada(colunas);
        panel.add(new JScrollPane(tblRevistas), BorderLayout.CENTER);

        JPanel pnlSul = new JPanel(new BorderLayout());
        JPanel pnlCadastro = new JPanel(new GridLayout(3, 4, 5, 5));
        pnlCadastro.setBorder(BorderFactory.createTitledBorder("Cadastrar Nova Revista"));

        JTextField txtId = new JTextField();
        JTextField txtTitulo = new JTextField();
        JTextField txtAno = new JTextField();
        JTextField txtEdicao = new JTextField();
        JButton btnCadastrar = new JButton("Cadastrar Revista");

        pnlCadastro.add(new JLabel("ID:")); pnlCadastro.add(txtId);
        pnlCadastro.add(new JLabel("Título:")); pnlCadastro.add(txtTitulo);
        pnlCadastro.add(new JLabel("Ano:")); pnlCadastro.add(txtAno);
        pnlCadastro.add(new JLabel("Edição:")); pnlCadastro.add(txtEdicao);
        pnlCadastro.add(new JLabel("")); pnlCadastro.add(new JLabel(""));
        pnlCadastro.add(new JLabel("")); pnlCadastro.add(btnCadastrar);

        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEditar = new JButton("Editar Selecionada");
        JButton btnRemover = new JButton("Remover Selecionada");
        pnlAcoes.add(btnEditar); pnlAcoes.add(btnRemover);

        pnlSul.add(pnlCadastro, BorderLayout.CENTER);
        pnlSul.add(pnlAcoes, BorderLayout.SOUTH);
        panel.add(pnlSul, BorderLayout.SOUTH);

        btnCadastrar.addActionListener(e -> {
            String idStr = txtId.getText().trim();
            String titStr = txtTitulo.getText().trim();
            String anoStr = txtAno.getText().trim();
            String edStr = txtEdicao.getText().trim();

            if (idStr.isEmpty() || !idStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Coloque apenas números no ID!", "Erro no ID", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (titStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo Título é obrigatório!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (anoStr.isEmpty() || !anoStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Coloque apenas números no Ano!", "Erro no Ano", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (edStr.isEmpty() || !edStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Coloque apenas números na Edição!", "Erro na Edição", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                manager.adicionarItem(new Revista(
                    Integer.parseInt(idStr),
                    titStr,
                    Integer.parseInt(anoStr),
                    Integer.parseInt(edStr)
                ));
                JOptionPane.showMessageDialog(this, "Revista cadastrada com sucesso!");
                atualizarTabelaRevistas();
                txtId.setText(""); txtTitulo.setText(""); txtAno.setText(""); txtEdicao.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEditar.addActionListener(e -> {
            int row = tblRevistas.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma revista.");
                return;
            }
            int id = (int) tblRevistas.obterValor(row, 0);
            try {
                String nTit = JOptionPane.showInputDialog(this, "ID " + id + " (Imutável) - Novo Título:", tblRevistas.obterValor(row, 1));
                if (nTit == null || nTit.isBlank()) return;

                String anoStr = JOptionPane.showInputDialog(this, "Novo Ano:", tblRevistas.obterValor(row, 2));
                if (anoStr == null || !anoStr.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Coloque apenas números no Ano!");
                    return;
                }

                String edStr = JOptionPane.showInputDialog(this, "Nova Edição:", tblRevistas.obterValor(row, 3));
                if (edStr == null || !edStr.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Coloque apenas números na Edição!");
                    return;
                }

                manager.editarRevista(id, nTit.trim(), Integer.parseInt(anoStr), Integer.parseInt(edStr));
                atualizarTabelaRevistas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage());
            }
        });

        btnRemover.addActionListener(e -> {
            int row = tblRevistas.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione uma revista.");
                return;
            }
            int id = (int) tblRevistas.obterValor(row, 0);
            try {
                manager.removerItem(id);
                atualizarTabelaRevistas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // --- PAINEL DVDS ---
    private JPanel criarPainelDVDs() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] colunas = {"ID", "Título", "Ano", "Duração (min)", "Status"};
        tblDVDs = new TabelaEncapsulada(colunas);
        panel.add(new JScrollPane(tblDVDs), BorderLayout.CENTER);

        JPanel pnlSul = new JPanel(new BorderLayout());
        JPanel pnlCadastro = new JPanel(new GridLayout(3, 4, 5, 5));
        pnlCadastro.setBorder(BorderFactory.createTitledBorder("Cadastrar Novo DVD"));

        JTextField txtId = new JTextField();
        JTextField txtTitulo = new JTextField();
        JTextField txtAno = new JTextField();
        JTextField txtDuracao = new JTextField();
        JButton btnCadastrar = new JButton("Cadastrar DVD");

        pnlCadastro.add(new JLabel("ID:")); pnlCadastro.add(txtId);
        pnlCadastro.add(new JLabel("Título:")); pnlCadastro.add(txtTitulo);
        pnlCadastro.add(new JLabel("Ano:")); pnlCadastro.add(txtAno);
        pnlCadastro.add(new JLabel("Duração:")); pnlCadastro.add(txtDuracao);
        pnlCadastro.add(new JLabel("")); pnlCadastro.add(new JLabel(""));
        pnlCadastro.add(new JLabel("")); pnlCadastro.add(btnCadastrar);

        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEditar = new JButton("Editar Selecionado");
        JButton btnRemover = new JButton("Remover Selecionado");
        pnlAcoes.add(btnEditar); pnlAcoes.add(btnRemover);

        pnlSul.add(pnlCadastro, BorderLayout.CENTER);
        pnlSul.add(pnlAcoes, BorderLayout.SOUTH);
        panel.add(pnlSul, BorderLayout.SOUTH);

        btnCadastrar.addActionListener(e -> {
            String idStr = txtId.getText().trim();
            String titStr = txtTitulo.getText().trim();
            String anoStr = txtAno.getText().trim();
            String durStr = txtDuracao.getText().trim();

            if (idStr.isEmpty() || !idStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Coloque apenas números no ID!", "Erro no ID", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (titStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo Título é obrigatório!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (anoStr.isEmpty() || !anoStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Coloque apenas números no Ano!", "Erro no Ano", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (durStr.isEmpty() || !durStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Coloque apenas números na Duração!", "Erro na Duração", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                manager.adicionarItem(new DVD(
                    Integer.parseInt(idStr),
                    titStr,
                    Integer.parseInt(anoStr),
                    Integer.parseInt(durStr)
                ));
                JOptionPane.showMessageDialog(this, "DVD cadastrado com sucesso!");
                atualizarTabelaDVDs();
                txtId.setText(""); txtTitulo.setText(""); txtAno.setText(""); txtDuracao.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEditar.addActionListener(e -> {
            int row = tblDVDs.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um DVD.");
                return;
            }
            int id = (int) tblDVDs.obterValor(row, 0);
            try {
                String nTit = JOptionPane.showInputDialog(this, "ID " + id + " (Imutável) - Novo Título:", tblDVDs.obterValor(row, 1));
                if (nTit == null || nTit.isBlank()) return;

                String anoStr = JOptionPane.showInputDialog(this, "Novo Ano:", tblDVDs.obterValor(row, 2));
                if (anoStr == null || !anoStr.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Coloque apenas números no Ano!");
                    return;
                }

                String durStr = JOptionPane.showInputDialog(this, "Nova Duração (min):", tblDVDs.obterValor(row, 3));
                if (durStr == null || !durStr.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Coloque apenas números na Duração!");
                    return;
                }

                manager.editarDVD(id, nTit.trim(), Integer.parseInt(anoStr), Integer.parseInt(durStr));
                atualizarTabelaDVDs();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage());
            }
        });

        btnRemover.addActionListener(e -> {
            int row = tblDVDs.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um DVD.");
                return;
            }
            int id = (int) tblDVDs.obterValor(row, 0);
            try {
                manager.removerItem(id);
                atualizarTabelaDVDs();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // --- PAINEL USUÁRIOS ---
    private JPanel criarPainelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] colunas = {"ID", "Nome", "Tipo Usuário", "Empréstimos"};
        tblUsuarios = new TabelaEncapsulada(colunas);
        panel.add(new JScrollPane(tblUsuarios), BorderLayout.CENTER);

        JPanel pnlSul = new JPanel(new BorderLayout());
        JPanel pnlCadastro = new JPanel(new GridLayout(2, 4, 5, 5));
        pnlCadastro.setBorder(BorderFactory.createTitledBorder("Cadastrar Usuário"));

        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Aluno", "Professor", "Funcionario"});
        JTextField txtId = new JTextField();
        JTextField txtNome = new JTextField();
        JButton btnCadastrar = new JButton("Cadastrar Usuário");

        pnlCadastro.add(new JLabel("Tipo:")); pnlCadastro.add(cbTipo);
        pnlCadastro.add(new JLabel("ID:")); pnlCadastro.add(txtId);
        pnlCadastro.add(new JLabel("Nome:")); pnlCadastro.add(txtNome);
        pnlCadastro.add(new JLabel("")); pnlCadastro.add(btnCadastrar);

        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEditar = new JButton("Editar Nome");
        JButton btnRemover = new JButton("Remover Usuário");
        pnlAcoes.add(btnEditar); pnlAcoes.add(btnRemover);

        pnlSul.add(pnlCadastro, BorderLayout.CENTER);
        pnlSul.add(pnlAcoes, BorderLayout.SOUTH);
        panel.add(pnlSul, BorderLayout.SOUTH);

        btnCadastrar.addActionListener(e -> {
            String idStr = txtId.getText().trim();
            String nomeStr = txtNome.getText().trim();

            if (idStr.isEmpty() || !idStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Coloque apenas números no ID!", "Erro no ID", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (nomeStr.isEmpty() || !nomeStr.matches("^[\\p{L}\\s]+$")) {
                JOptionPane.showMessageDialog(this, "Não pode conter números, apenas letras no Nome!", "Erro no Nome", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(idStr);
                String tipo = (String) cbTipo.getSelectedItem();

                Usuario u = null;
                if ("Aluno".equals(tipo)) u = new Aluno(id, nomeStr);
                else if ("Professor".equals(tipo)) u = new Professor(id, nomeStr);
                else if ("Funcionario".equals(tipo)) u = new Funcionario(id, nomeStr);

                if (u != null) {
                    manager.cadastrarUsuario(u);
                    JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
                    atualizarTabelaUsuarios();
                    txtId.setText(""); txtNome.setText("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEditar.addActionListener(e -> {
            int row = tblUsuarios.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um usuário.");
                return;
            }
            int id = (int) tblUsuarios.obterValor(row, 0);
            try {
                String nNome = JOptionPane.showInputDialog(this, "ID " + id + " (Imutável) - Novo Nome:", tblUsuarios.obterValor(row, 1));
                if (nNome == null || nNome.isBlank()) return;
                if (!nNome.matches("^[\\p{L}\\s]+$")) {
                    JOptionPane.showMessageDialog(this, "Não pode conter números, apenas letras no Nome!");
                    return;
                }

                manager.editarUsuario(id, nNome.trim());
                atualizarTabelaUsuarios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage());
            }
        });

        btnRemover.addActionListener(e -> {
            int row = tblUsuarios.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um usuário.");
                return;
            }
            int id = (int) tblUsuarios.obterValor(row, 0);
            try {
                manager.removerUsuario(id);
                atualizarTabelaUsuarios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // --- PAINEL EMPRÉSTIMOS ---
    private JPanel criarPainelEmprestimos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] colunas = {"Usuário", "Item", "Data Empréstimo", "Devolução Prevista"};
        tblEmprestimos = new TabelaEncapsulada(colunas);
        panel.add(new JScrollPane(tblEmprestimos), BorderLayout.CENTER);

        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlAcoes.setBorder(BorderFactory.createTitledBorder("Operações"));

        JTextField txtIdUsuario = new JTextField(5);
        JTextField txtIdItem = new JTextField(5);
        JButton btnEmprestar = new JButton("Realizar Empréstimo");
        JButton btnDevolver = new JButton("Realizar Devolução");

        pnlAcoes.add(new JLabel("ID Usuário:")); pnlAcoes.add(txtIdUsuario);
        pnlAcoes.add(new JLabel("ID Item:")); pnlAcoes.add(txtIdItem);
        pnlAcoes.add(btnEmprestar); pnlAcoes.add(btnDevolver);

        panel.add(pnlAcoes, BorderLayout.SOUTH);

        btnEmprestar.addActionListener(e -> {
            String uStr = txtIdUsuario.getText().trim();
            String iStr = txtIdItem.getText().trim();

            if (uStr.isEmpty() || !uStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Coloque apenas números no ID do Usuário!", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (iStr.isEmpty() || !iStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Coloque apenas números no ID do Item!", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                manager.realizarEmprestimo(Integer.parseInt(uStr), Integer.parseInt(iStr));
                JOptionPane.showMessageDialog(this, "Empréstimo realizado com sucesso!");
                atualizarTodasTabelas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnDevolver.addActionListener(e -> {
            String uStr = txtIdUsuario.getText().trim();
            String iStr = txtIdItem.getText().trim();

            if (uStr.isEmpty() || !uStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Coloque apenas números no ID do Usuário!", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (iStr.isEmpty() || !iStr.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Coloque apenas números no ID do Item!", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                manager.realizarDevolucao(Integer.parseInt(uStr), Integer.parseInt(iStr));
                JOptionPane.showMessageDialog(this, "Devolução realizada com sucesso!");
                atualizarTodasTabelas();
            } catch (MultaAtrasoException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Devolução com Multa", JOptionPane.WARNING_MESSAGE);
                atualizarTodasTabelas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // --- MÉTODOS AUXILIARES ---
    private void atualizarTodasTabelas() {
        atualizarTabelaLivros();
        atualizarTabelaRevistas();
        atualizarTabelaDVDs();
        atualizarTabelaUsuarios();
        atualizarTabelaEmprestimos();
    }

    private void atualizarTabelaLivros() {
        preencherTabelaLivros(manager.getAcervo());
    }

    private void preencherTabelaLivros(List<ItemBiblioteca> lista) {
        tblLivros.limpar();
        for (ItemBiblioteca item : lista) {
            if (item instanceof Livro l) {
                tblLivros.adicionarLinha(new Object[]{
                    l.getId(), l.getTitulo(), l.getAno(), l.getAutor(), l.getNumPaginas(),
                    l.isDisponivel() ? "Disponível" : "Emprestado"
                });
            }
        }
    }

    private void atualizarTabelaRevistas() {
        tblRevistas.limpar();
        for (ItemBiblioteca item : manager.getAcervo()) {
            if (item instanceof Revista r) {
                tblRevistas.adicionarLinha(new Object[]{
                    r.getId(), r.getTitulo(), r.getAno(), r.getEdicao(),
                    r.isDisponivel() ? "Disponível" : "Emprestado"
                });
            }
        }
    }

    private void atualizarTabelaDVDs() {
        tblDVDs.limpar();
        for (ItemBiblioteca item : manager.getAcervo()) {
            if (item instanceof DVD d) {
                tblDVDs.adicionarLinha(new Object[]{
                    d.getId(), d.getTitulo(), d.getAno(), d.getDuracaoMinutos(),
                    d.isDisponivel() ? "Disponível" : "Emprestado"
                });
            }
        }
    }

    private void atualizarTabelaUsuarios() {
        tblUsuarios.limpar();
        for (Usuario u : manager.getUsuarios()) {
            tblUsuarios.adicionarLinha(new Object[]{
                u.getId(), u.getNome(), u.getTipoUsuario(),
                u.getQtdEmprestados() + "/" + u.getLimiteEmprestimos()
            });
        }
    }

    private void atualizarTabelaEmprestimos() {
        tblEmprestimos.limpar();
        for (Emprestimo emp : manager.getEmprestimosAtivos()) {
            tblEmprestimos.adicionarLinha(new Object[]{
                emp.getUsuario().getNome(), emp.getItem().getTitulo(),
                emp.getDataEmprestimo(), emp.getDataDevolucaoPrevista()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BibliotecaGUI().setVisible(true));
    }
}