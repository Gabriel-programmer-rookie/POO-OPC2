public class Revista extends ItemBiblioteca {
    private int edicao;

    // Construtor 1 (Padrão)
    public Revista(int id, String titulo, int ano, int edicao) {
        super(id, titulo, ano);
        this.edicao = edicao;
    }

    // Construtor 2 (Sobrecarga)
    public Revista(int id, String titulo, int ano, int edicao, boolean disponivel) {
        super(id, titulo, ano);
        this.setDisponivel(disponivel);
        this.edicao = edicao;
    }

    public int getEdicao() { return edicao; }
    public void setEdicao(int edicao) { this.edicao = edicao; }

    @Override
    public String getTipoItem() { return "REVISTA"; }

    @Override
    public String toString() {
        return super.toString() + " | Edição: " + edicao;
    }
}