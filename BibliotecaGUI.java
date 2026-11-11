import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BibliotecaGUI extends JFrame {
    private final BibliotecaManager manager;

    // Tabelas e Models Individuais
    private DefaultTableModel modelLivros;
    private DefaultTableModel modelRevistas;
    private DefaultTableModel modelDVDs;
    private DefaultTableModel modelUsuarios;
    private DefaultTableModel modelEmprestimos;

    public BibliotecaGUI() {
        manager = new BibliotecaManager();

        setTitle("Sistema de Gestão de Biblioteca Universitária");
        setSize(900, 650);
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

        String[] colunas = {"ID", "Título", "Ano", "Autor", "Nº Páginas", "Status"};
        modelLivros = new DefaultTableModel(colunas, 0);
        panel.add(new JScrollPane(new JTable(modelLivros)), BorderLayout.CENTER);

        JPanel pnlCadastro = new JPanel(new GridLayout(3, 4, 5, 5));
        pnlCadastro.setBorder(BorderFactory.createTitledBorder("Cadastrar Novo Livro"));

        JTextField txtId = new JTextField();
        JTextField txtTitulo = new JTextField();
        JTextField txtAno = new JTextField();
        JTextField txtAutor = new JTextField();
        JTextField txtPaginas = new JTextField();
        JButton btnCadastrar = new JButton("Cadastrar Livro");

        pnlCadastro.add(new JLabel("ID:"));
        pnlCadastro.add(txtId);
        pnlCadastro.add(new JLabel("Título:"));
        pnlCadastro.add(txtTitulo);
        pnlCadastro.add(new JLabel("Ano:"));
        pnlCadastro.add(txtAno);
        pnlCadastro.add(new JLabel("Autor:"));
        pnlCadastro.add(txtAutor);
        pnlCadastro.add(new JLabel("Nº Páginas:"));
        pnlCadastro.add(txtPaginas);
        pnlCadastro.add(new JLabel(""));
        pnlCadastro.add(btnCadastrar);

        panel.add(pnlCadastro, BorderLayout.SOUTH);

        btnCadastrar.addActionListener(e -> {
            if (txtId.getText().trim().isEmpty() ||
                txtTitulo.getText().trim().isEmpty() ||
                txtAno.getText().trim().isEmpty() ||
                txtAutor.getText().trim().isEmpty() ||
                txtPaginas.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos do Livro são de preenchimento obrigatório!", "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(txtId.getText().trim());
                String titulo = txtTitulo.getText().trim();
                int ano = Integer.parseInt(txtAno.getText().trim());
                String autor = txtAutor.getText().trim();
                int paginas = Integer.parseInt(txtPaginas.getText().trim());

                manager.adicionarItem(new Livro(id, titulo, ano, autor, paginas));
                JOptionPane.showMessageDialog(this, "Livro cadastrado com sucesso!");
                atualizarTabelaLivros();

                txtId.setText(""); txtTitulo.setText(""); txtAno.setText("");
                txtAutor.setText(""); txtPaginas.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Os campos ID, Ano e Nº Páginas devem conter apenas números válidos!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar livro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // --- PAINEL REVISTAS ---
    private JPanel criarPainelRevistas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] colunas = {"ID", "Título", "Ano", "Edição", "Status"};
        modelRevistas = new DefaultTableModel(colunas, 0);
        panel.add(new JScrollPane(new JTable(modelRevistas)), BorderLayout.CENTER);

        JPanel pnlCadastro = new JPanel(new GridLayout(3, 4, 5, 5));
        pnlCadastro.setBorder(BorderFactory.createTitledBorder("Cadastrar Nova Revista"));

        JTextField txtId = new JTextField();
        JTextField txtTitulo = new JTextField();
        JTextField txtAno = new JTextField();
        JTextField txtEdicao = new JTextField();
        JButton btnCadastrar = new JButton("Cadastrar Revista");

        pnlCadastro.add(new JLabel("ID:"));
        pnlCadastro.add(txtId);
        pnlCadastro.add(new JLabel("Título:"));
        pnlCadastro.add(txtTitulo);
        pnlCadastro.add(new JLabel("Ano:"));
        pnlCadastro.add(txtAno);
        pnlCadastro.add(new JLabel("Edição:"));
        pnlCadastro.add(txtEdicao);
        pnlCadastro.add(new JLabel(""));
        pnlCadastro.add(new JLabel(""));
        pnlCadastro.add(new JLabel(""));
        pnlCadastro.add(btnCadastrar);

        panel.add(pnlCadastro, BorderLayout.SOUTH);

        btnCadastrar.addActionListener(e -> {
            if (txtId.getText().trim().isEmpty() ||
                txtTitulo.getText().trim().isEmpty() ||
                txtAno.getText().trim().isEmpty() ||
                txtEdicao.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos da Revista são de preenchimento obrigatório!", "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(txtId.getText().trim());
                String titulo = txtTitulo.getText().trim();
                int ano = Integer.parseInt(txtAno.getText().trim());
                int edicao = Integer.parseInt(txtEdicao.getText().trim());

                manager.adicionarItem(new Revista(id, titulo, ano, edicao));
                JOptionPane.showMessageDialog(this, "Revista cadastrada com sucesso!");
                atualizarTabelaRevistas();

                txtId.setText(""); txtTitulo.setText(""); txtAno.setText(""); txtEdicao.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Os campos ID, Ano e Edição devem conter apenas números válidos!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar revista: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // --- PAINEL DVDS ---
    private JPanel criarPainelDVDs() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] colunas = {"ID", "Título", "Ano", "Duração (min)", "Status"};
        modelDVDs = new DefaultTableModel(colunas, 0);
        panel.add(new JScrollPane(new JTable(modelDVDs)), BorderLayout.CENTER);

        JPanel pnlCadastro = new JPanel(new GridLayout(3, 4, 5, 5));
        pnlCadastro.setBorder(BorderFactory.createTitledBorder("Cadastrar Novo DVD"));

        JTextField txtId = new JTextField();
        JTextField txtTitulo = new JTextField();
        JTextField txtAno = new JTextField();
        JTextField txtDuracao = new JTextField();
        JButton btnCadastrar = new JButton("Cadastrar DVD");

        pnlCadastro.add(new JLabel("ID:"));
        pnlCadastro.add(txtId);
        pnlCadastro.add(new JLabel("Título:"));
        pnlCadastro.add(txtTitulo);
        pnlCadastro.add(new JLabel("Ano:"));
        pnlCadastro.add(txtAno);
        pnlCadastro.add(new JLabel("Duração (min):"));
        pnlCadastro.add(txtDuracao);
        pnlCadastro.add(new JLabel(""));
        pnlCadastro.add(new JLabel(""));
        pnlCadastro.add(new JLabel(""));
        pnlCadastro.add(btnCadastrar);

        panel.add(pnlCadastro, BorderLayout.SOUTH);

        btnCadastrar.addActionListener(e -> {
            if (txtId.getText().trim().isEmpty() ||
                txtTitulo.getText().trim().isEmpty() ||
                txtAno.getText().trim().isEmpty() ||
                txtDuracao.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos do DVD são de preenchimento obrigatório!", "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(txtId.getText().trim());
                String titulo = txtTitulo.getText().trim();
                int ano = Integer.parseInt(txtAno.getText().trim());
                int duracao = Integer.parseInt(txtDuracao.getText().trim());

                manager.adicionarItem(new DVD(id, titulo, ano, duracao));
                JOptionPane.showMessageDialog(this, "DVD cadastrado com sucesso!");
                atualizarTabelaDVDs();

                txtId.setText(""); txtTitulo.setText(""); txtAno.setText(""); txtDuracao.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Os campos ID, Ano e Duração devem conter apenas números válidos!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar DVD: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // --- PAINEL USUÁRIOS ---
    private JPanel criarPainelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] colunas = {"ID", "Nome", "Tipo Usuário", "Empréstimos"};
        modelUsuarios = new DefaultTableModel(colunas, 0);
        panel.add(new JScrollPane(new JTable(modelUsuarios)), BorderLayout.CENTER);

        JPanel pnlCadastro = new JPanel(new GridLayout(2, 4, 5, 5));
        pnlCadastro.setBorder(BorderFactory.createTitledBorder("Cadastrar Usuário"));

        JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Aluno", "Professor", "Funcionario"});
        JTextField txtId = new JTextField();
        JTextField txtNome = new JTextField();
        JButton btnCadastrar = new JButton("Cadastrar Usuário");

        pnlCadastro.add(new JLabel("Tipo:"));
        pnlCadastro.add(cbTipo);
        pnlCadastro.add(new JLabel("ID:"));
        pnlCadastro.add(txtId);
        pnlCadastro.add(new JLabel("Nome:"));
        pnlCadastro.add(txtNome);
        pnlCadastro.add(new JLabel(""));
        pnlCadastro.add(btnCadastrar);

        panel.add(pnlCadastro, BorderLayout.SOUTH);

        btnCadastrar.addActionListener(e -> {
            if (txtId.getText().trim().isEmpty() || txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos do Usuário são de preenchimento obrigatório!", "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int id = Integer.parseInt(txtId.getText().trim());
                String nome = txtNome.getText().trim();
                String tipo = (String) cbTipo.getSelectedItem();

                Usuario u = null;
                if ("Aluno".equals(tipo)) u = new Aluno(id, nome);
                else if ("Professor".equals(tipo)) u = new Professor(id, nome);
                else if ("Funcionario".equals(tipo)) u = new Funcionario(id, nome);

                if (u != null) {
                    manager.cadastrarUsuario(u);
                    JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
                    atualizarTabelaUsuarios();
                    txtId.setText(""); txtNome.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "O campo ID deve conter apenas um número válido!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // --- PAINEL EMPRÉSTIMOS ---
    private JPanel criarPainelEmprestimos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] colunas = {"Usuário", "Item", "Data Empréstimo", "Devolução Prevista"};
        modelEmprestimos = new DefaultTableModel(colunas, 0);
        panel.add(new JScrollPane(new JTable(modelEmprestimos)), BorderLayout.CENTER);

        JPanel pnlAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlAcoes.setBorder(BorderFactory.createTitledBorder("Operações"));

        JTextField txtIdUsuario = new JTextField(5);
        JTextField txtIdItem = new JTextField(5);
        JButton btnEmprestar = new JButton("Realizar Empréstimo");
        JButton btnDevolver = new JButton("Realizar Devolução");

        pnlAcoes.add(new JLabel("ID Usuário:"));
        pnlAcoes.add(txtIdUsuario);
        pnlAcoes.add(new JLabel("ID Item:"));
        pnlAcoes.add(txtIdItem);
        pnlAcoes.add(btnEmprestar);
        pnlAcoes.add(btnDevolver);

        panel.add(pnlAcoes, BorderLayout.SOUTH);

        btnEmprestar.addActionListener(e -> {
            if (txtIdUsuario.getText().trim().isEmpty() || txtIdItem.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID do Usuário e o ID do Item!", "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int idU = Integer.parseInt(txtIdUsuario.getText().trim());
                int idI = Integer.parseInt(txtIdItem.getText().trim());
                manager.realizarEmprestimo(idU, idI);
                JOptionPane.showMessageDialog(this, "Empréstimo realizado com sucesso!");
                atualizarTodasTabelas();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Os IDs devem conter apenas números válidos!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnDevolver.addActionListener(e -> {
            if (txtIdUsuario.getText().trim().isEmpty() || txtIdItem.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID do Usuário e o ID do Item!", "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int idU = Integer.parseInt(txtIdUsuario.getText().trim());
                int idI = Integer.parseInt(txtIdItem.getText().trim());
                manager.realizarDevolucao(idU, idI);
                JOptionPane.showMessageDialog(this, "Devolução realizada com sucesso!");
                atualizarTodasTabelas();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Os IDs devem conter apenas números válidos!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // --- ATUALIZAÇÕES INDIVIDUAIS DE TABELAS ---
    private void atualizarTodasTabelas() {
        atualizarTabelaLivros();
        atualizarTabelaRevistas();
        atualizarTabelaDVDs();
        atualizarTabelaUsuarios();
        atualizarTabelaEmprestimos();
    }

    private void atualizarTabelaLivros() {
        modelLivros.setRowCount(0);
        for (ItemBiblioteca item : manager.getAcervo()) {
            if (item instanceof Livro) {
                Livro l = (Livro) item;
                modelLivros.addRow(new Object[]{
                    l.getId(), l.getTitulo(), l.getAno(), l.getAutor(), l.getNumPaginas(),
                    l.isDisponivel() ? "Disponível" : "Emprestado"
                });
            }
        }
    }

    private void atualizarTabelaRevistas() {
        modelRevistas.setRowCount(0);
        for (ItemBiblioteca item : manager.getAcervo()) {
            if (item instanceof Revista) {
                Revista r = (Revista) item;
                modelRevistas.addRow(new Object[]{
                    r.getId(), r.getTitulo(), r.getAno(), r.getEdicao(),
                    r.isDisponivel() ? "Disponível" : "Emprestado"
                });
            }
        }
    }

    private void atualizarTabelaDVDs() {
        modelDVDs.setRowCount(0);
        for (ItemBiblioteca item : manager.getAcervo()) {
            if (item instanceof DVD) {
                DVD d = (DVD) item;
                modelDVDs.addRow(new Object[]{
                    d.getId(), d.getTitulo(), d.getAno(), d.getDuracaoMinutos(),
                    d.isDisponivel() ? "Disponível" : "Emprestado"
                });
            }
        }
    }

    private void atualizarTabelaUsuarios() {
        modelUsuarios.setRowCount(0);
        for (Usuario u : manager.getUsuarios()) {
            modelUsuarios.addRow(new Object[]{
                u.getId(), u.getNome(), u.getTipoUsuario(),
                u.getQtdEmprestados() + "/" + u.getLimiteEmprestimos()
            });
        }
    }

    private void atualizarTabelaEmprestimos() {
        modelEmprestimos.setRowCount(0);
        for (Emprestimo emp : manager.getEmprestimosAtivos()) {
            modelEmprestimos.addRow(new Object[]{
                emp.getUsuario().getNome(), emp.getItem().getTitulo(),
                emp.getDataEmprestimo(), emp.getDataDevolucaoPrevista()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BibliotecaGUI().setVisible(true));
    }
}