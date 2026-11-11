import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BibliotecaManager implements IPesquisavel {
    private final List<ItemBiblioteca> acervo = new ArrayList<>();
    private final Map<Integer, Usuario> usuarios = new HashMap<>();
    private final List<Emprestimo> emprestimosAtivos = new ArrayList<>();
    
    private final String ARQUIVO_ACERVO = "acervo.txt";
    private final String ARQUIVO_USUARIOS = "usuarios.txt";
    private final String ARQUIVO_EMPRESTIMOS = "emprestimos.txt";

    public static final double VALOR_MULTA_DIARIA = 2.50;

    public BibliotecaManager() {
        carregarDados();
    }

    // --- CADASTRO ---
    public void adicionarItem(ItemBiblioteca item) throws Exception {
        if (buscarItemPorId(item.getId()) != null) {
            throw new Exception("Já existe um item cadastrado com o ID " + item.getId() + ".");
        }
        acervo.add(item);
        salvarDados();
    }

    public void cadastrarUsuario(Usuario usuario) throws Exception {
        if (buscarUsuarioPorId(usuario.getId()) != null) {
            throw new Exception("Já existe um usuário cadastrado com o ID " + usuario.getId() + ".");
        }
        usuarios.put(usuario.getId(), usuario);
        salvarDados();
    }

    // --- REMOÇÃO ---
    public void removerItem(int id) throws Exception {
        ItemBiblioteca item = buscarItemPorId(id);
        if (item == null) throw new Exception("Item não encontrado.");
        if (!item.isDisponivel()) throw new Exception("Não é possível remover um item emprestado.");
        acervo.remove(item);
        salvarDados();
    }

    public void removerUsuario(int id) throws Exception {
        Usuario u = buscarUsuarioPorId(id);
        if (u == null) throw new Exception("Usuário não encontrado.");
        if (u.getQtdEmprestados() > 0) throw new Exception("Não é possível remover usuário com empréstimos pendentes.");
        usuarios.remove(id);
        salvarDados();
    }

    // --- EDIÇÃO ---
    public void editarLivro(int id, String novoTitulo, int novoAno, String novoAutor, int novasPaginas) throws Exception {
        ItemBiblioteca item = buscarItemPorId(id);
        if (!(item instanceof Livro l)) throw new Exception("Livro não encontrado.");
        l.setTitulo(novoTitulo);
        l.setAno(novoAno);
        l.setAutor(novoAutor);
        l.setNumPaginas(novasPaginas);
        salvarDados();
    }

    public void editarRevista(int id, String novoTitulo, int novoAno, int novaEdicao) throws Exception {
        ItemBiblioteca item = buscarItemPorId(id);
        if (!(item instanceof Revista r)) throw new Exception("Revista não encontrada.");
        r.setTitulo(novoTitulo);
        r.setAno(novoAno);
        r.setEdicao(novaEdicao);
        salvarDados();
    }

    public void editarDVD(int id, String novoTitulo, int novoAno, int novaDuracao) throws Exception {
        ItemBiblioteca item = buscarItemPorId(id);
        if (!(item instanceof DVD d)) throw new Exception("DVD não encontrado.");
        d.setTitulo(novoTitulo);
        d.setAno(novoAno);
        d.setDuracaoMinutos(novaDuracao);
        salvarDados();
    }

    public void editarUsuario(int id, String novoNome) throws Exception {
        Usuario u = buscarUsuarioPorId(id);
        if (u == null) throw new Exception("Usuário não encontrado.");
        u.setNome(novoNome);
        salvarDados();
    }

    // --- BUSCA ---
    public ItemBiblioteca buscarItemPorId(int id) {
        for (ItemBiblioteca item : acervo) {
            if (item.getId() == id) return item;
        }
        return null;
    }

    public Usuario buscarUsuarioPorId(int id) {
        return usuarios.get(id);
    }

    // --- EMPRÉSTIMOS E DEVOLUÇÕES COM MULTA ---
    public void realizarEmprestimo(int idUsuario, int idItem) throws LimiteExcedidoException, ItemIndisponivelException, Exception {
        Usuario u = usuarios.get(idUsuario);
        ItemBiblioteca item = buscarItemPorId(idItem);

        if (u == null) throw new Exception("Usuário não encontrado.");
        if (item == null) throw new Exception("Item não encontrado no acervo.");
        if (!item.isDisponivel()) throw new ItemIndisponivelException("O item já está emprestado.");
        if (u.getQtdEmprestados() >= u.getLimiteEmprestimos()) {
            throw new LimiteExcedidoException("O limite de empréstimos foi atingido.");
        }

        item.setDisponivel(false);
        u.incrementarEmprestimo();
        emprestimosAtivos.add(new Emprestimo(u, item));
        salvarDados();
    }

    public void realizarDevolucao(int idUsuario, int idItem) throws MultaAtrasoException, Exception {
        Usuario u = usuarios.get(idUsuario);
        ItemBiblioteca item = buscarItemPorId(idItem);

        if (u == null || item == null) throw new Exception("Dados inválidos para devolução.");
        if (item.isDisponivel()) throw new Exception("O item consta como disponível.");

        Emprestimo emp = null;
        for (Emprestimo e : emprestimosAtivos) {
            if (e.getItem().getId() == idItem && e.getUsuario().getId() == idUsuario) {
                emp = e;
                break;
            }
        }

        long diasAtraso = 0;
        if (emp != null) {
            LocalDate hoje = LocalDate.now();
            if (hoje.isAfter(emp.getDataDevolucaoPrevista())) {
                diasAtraso = ChronoUnit.DAYS.between(emp.getDataDevolucaoPrevista(), hoje);
            }
        }

        item.setDisponivel(true);
        u.decrementarEmprestimo();
        emprestimosAtivos.removeIf(e -> e.getItem().getId() == idItem && e.getUsuario().getId() == idUsuario);
        salvarDados();

        if (diasAtraso > 0) {
            double multa = diasAtraso * VALOR_MULTA_DIARIA;
            throw new MultaAtrasoException(diasAtraso, multa);
        }
    }

    // --- CONSULTAS (IPESQUISAVEL COM SOBRECARGA) ---
    @Override
    public List<ItemBiblioteca> pesquisarPorTitulo(String titulo) {
        return pesquisarPorTitulo(titulo, false);
    }

    @Override
    public List<ItemBiblioteca> pesquisarPorTitulo(String titulo, boolean apenasDisponiveis) {
        List<ItemBiblioteca> res = new ArrayList<>();
        for (ItemBiblioteca item : acervo) {
            boolean bateTitulo = item.getTitulo().toLowerCase().contains(titulo.toLowerCase());
            boolean bateDisp = !apenasDisponiveis || item.isDisponivel();
            if (bateTitulo && bateDisp) res.add(item);
        }
        return res;
    }

    @Override
    public List<ItemBiblioteca> pesquisarPorTipo(String tipo) {
        List<ItemBiblioteca> res = new ArrayList<>();
        for (ItemBiblioteca item : acervo) {
            if (item.getTipoItem().equalsIgnoreCase(tipo)) res.add(item);
        }
        return res;
    }

    @Override
    public List<ItemBiblioteca> pesquisarPorAutor(String autor) {
        List<ItemBiblioteca> res = new ArrayList<>();
        for (ItemBiblioteca item : acervo) {
            if (item instanceof Livro l && l.getAutor().toLowerCase().contains(autor.toLowerCase())) {
                res.add(l);
            }
        }
        return res;
    }

    @Override
    public List<ItemBiblioteca> pesquisarPorDisponibilidade(boolean disponivel) {
        List<ItemBiblioteca> res = new ArrayList<>();
        for (ItemBiblioteca item : acervo) {
            if (item.isDisponivel() == disponivel) res.add(item);
        }
        return res;
    }

    public List<ItemBiblioteca> getAcervo() { return acervo; }
    public Collection<Usuario> getUsuarios() { return usuarios.values(); }
    public List<Emprestimo> getEmprestimosAtivos() { return emprestimosAtivos; }

    // --- PERSISTÊNCIA ---
    public final void salvarDados() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_ACERVO))) {
            for (ItemBiblioteca item : acervo) {
                if (item instanceof Livro l) {
                    writer.println("LIVRO;" + l.getId() + ";" + l.getTitulo() + ";" + l.getAno() + ";" + l.isDisponivel() + ";" + l.getAutor() + ";" + l.getNumPaginas());
                } else if (item instanceof Revista r) {
                    writer.println("REVISTA;" + r.getId() + ";" + r.getTitulo() + ";" + r.getAno() + ";" + r.isDisponivel() + ";" + r.getEdicao());
                } else if (item instanceof DVD d) {
                    writer.println("DVD;" + d.getId() + ";" + d.getTitulo() + ";" + d.getAno() + ";" + d.isDisponivel() + ";" + d.getDuracaoMinutos());
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar acervo: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_USUARIOS))) {
            for (Usuario u : usuarios.values()) {
                writer.println(u.getTipoUsuario() + ";" + u.getId() + ";" + u.getNome() + ";" + u.getQtdEmprestados());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar usuários: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_EMPRESTIMOS))) {
            for (Emprestimo emp : emprestimosAtivos) {
                writer.println(emp.getUsuario().getId() + ";" + emp.getItem().getId() + ";" + emp.getDataEmprestimo() + ";" + emp.getDataDevolucaoPrevista());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar empréstimos: " + e.getMessage());
        }
    }

    public final void carregarDados() {
        acervo.clear();
        usuarios.clear();
        emprestimosAtivos.clear();

        // 1. Carrega Acervo com tratamento explicito de FileNotFoundException
        File fAcervo = new File(ARQUIVO_ACERVO);
        try (BufferedReader reader = new BufferedReader(new FileReader(fAcervo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] p = linha.split(";", -1);
                if (p.length < 5) continue;

                String tipo = p[0];
                int id = Integer.parseInt(p[1]);
                String titulo = p[2];
                int ano = Integer.parseInt(p[3]);
                boolean disp = Boolean.parseBoolean(p[4]);

                if (tipo.equals("LIVRO") && p.length >= 7) {
                    Livro l = new Livro(id, titulo, ano, p[5], Integer.parseInt(p[6]), disp);
                    acervo.add(l);
                } else if (tipo.equals("REVISTA") && p.length >= 6) {
                    Revista r = new Revista(id, titulo, ano, Integer.parseInt(p[5]), disp);
                    acervo.add(r);
                } else if (tipo.equals("DVD") && p.length >= 6) {
                    DVD d = new DVD(id, titulo, ano, Integer.parseInt(p[5]), disp);
                    acervo.add(d);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Aviso: Arquivo de acervo não encontrado. Um novo será criado ao salvar.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao ler arquivo de acervo: " + e.getMessage());
        }

        // 2. Carrega Usuários com tratamento de FileNotFoundException
        File fUsuarios = new File(ARQUIVO_USUARIOS);
        try (BufferedReader reader = new BufferedReader(new FileReader(fUsuarios))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] p = linha.split(";", -1);
                if (p.length < 4) continue;

                String tipo = p[0];
                int id = Integer.parseInt(p[1]);
                String nome = p[2];

                Usuario u = null;
                if (tipo.equals("ALUNO")) u = new Aluno(id, nome);
                else if (tipo.equals("PROFESSOR")) u = new Professor(id, nome);
                else if (tipo.equals("FUNCIONARIO")) u = new Funcionario(id, nome);

                if (u != null) usuarios.put(u.getId(), u);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Aviso: Arquivo de usuários não encontrado. Um novo será criado ao salvar.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao ler arquivo de usuários: " + e.getMessage());
        }

        // 3. Carrega Empréstimos
        File fEmprestimos = new File(ARQUIVO_EMPRESTIMOS);
        try (BufferedReader reader = new BufferedReader(new FileReader(fEmprestimos))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] p = linha.split(";", -1);
                if (p.length < 4) continue;

                int idUsuario = Integer.parseInt(p[0]);
                int idItem = Integer.parseInt(p[1]);
                LocalDate dtEmp = LocalDate.parse(p[2]);
                LocalDate dtDev = LocalDate.parse(p[3]);

                Usuario u = buscarUsuarioPorId(idUsuario);
                ItemBiblioteca item = buscarItemPorId(idItem);

                if (u != null && item != null) {
                    emprestimosAtivos.add(new Emprestimo(u, item, dtEmp, dtDev));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Aviso: Arquivo de empréstimos não encontrado. Um novo será criado ao salvar.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erro ao ler arquivo de empréstimos: " + e.getMessage());
        }

        sincronizarEstados();
    }

    private void sincronizarEstados() {
        for (ItemBiblioteca item : acervo) item.setDisponivel(true);
        for (Usuario u : usuarios.values()) {
            while (u.getQtdEmprestados() > 0) u.decrementarEmprestimo();
        }

        for (Emprestimo emp : emprestimosAtivos) {
            emp.getItem().setDisponivel(false);
            emp.getUsuario().incrementarEmprestimo();
        }
    }
}