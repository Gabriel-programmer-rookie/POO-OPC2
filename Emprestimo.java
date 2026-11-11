import java.time.LocalDate;

public class Emprestimo {
    private final Usuario usuario;
    private final ItemBiblioteca item;
    private final LocalDate dataEmprestimo;
    private final LocalDate dataDevolucaoPrevista;

    // Construtor 1: Novos empréstimos (calcula data atual e +14 dias)
    public Emprestimo(Usuario usuario, ItemBiblioteca item) {
        this(usuario, item, LocalDate.now(), LocalDate.now().plusDays(14));
    }

    // Construtor 2: Sobrecarga para carregar empréstimos existentes mantendo datas salvas
    public Emprestimo(Usuario usuario, ItemBiblioteca item, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista) {
        this.usuario = usuario;
        this.item = item;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public Usuario getUsuario() { return usuario; }
    public ItemBiblioteca getItem() { return item; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }

    @Override
    public String toString() {
        return "Empréstimo -> Usuário: " + usuario.getNome() + 
               " | Item: " + item.getTitulo() + 
               " | Retirado em: " + dataEmprestimo + 
               " | Devolução Prevista: " + dataDevolucaoPrevista;
    }
}