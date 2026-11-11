import java.util.List;

public interface IPesquisavel {
    List<ItemBiblioteca> pesquisarPorTitulo(String titulo);
    List<ItemBiblioteca> pesquisarPorTitulo(String titulo, boolean apenasDisponiveis); // Sobrecarga de método
    List<ItemBiblioteca> pesquisarPorTipo(String tipo);
    List<ItemBiblioteca> pesquisarPorAutor(String autor);
    List<ItemBiblioteca> pesquisarPorDisponibilidade(boolean disponivel);
}