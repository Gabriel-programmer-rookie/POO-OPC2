public abstract class ItemBiblioteca {
    private final int id;
    private String titulo;
    private int ano;
    private boolean disponivel;

    public ItemBiblioteca(int id, String titulo, int ano) {
        this.id = id;
        this.titulo = titulo;
        this.ano = ano;
        this.disponivel = true;
    }

    public abstract String getTipoItem();

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    @Override
    public String toString() {
        return "[" + getTipoItem() + "] ID: " + id + " | Título: " + titulo +
               " | Ano: " + ano + " | Disponível: " + (disponivel ? "Sim" : "Não");
    }
}