public class Livro extends ItemBiblioteca {
    private String autor;
    private int numPaginas;

    // Construtor 1 (Padrão)
    public Livro(int id, String titulo, int ano, String autor, int numPaginas) {
        super(id, titulo, ano);
        this.autor = autor;
        this.numPaginas = numPaginas;
    }

    // Construtor 2 (Sobrecarga)
    public Livro(int id, String titulo, int ano, String autor, int numPaginas, boolean disponivel) {
        super(id, titulo, ano);
        this.setDisponivel(disponivel);
        this.autor = autor;
        this.numPaginas = numPaginas;
    }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public int getNumPaginas() { return numPaginas; }
    public void setNumPaginas(int numPaginas) { this.numPaginas = numPaginas; }

    @Override
    public String getTipoItem() { return "LIVRO"; }

    @Override
    public String toString() {
        return super.toString() + " | Autor: " + autor + " | Páginas: " + numPaginas;
    }
}