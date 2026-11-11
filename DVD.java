public class DVD extends ItemBiblioteca {
    private int duracaoMinutos;

    // Construtor 1 (Padrão)
    public DVD(int id, String titulo, int ano, int duracaoMinutos) {
        super(id, titulo, ano);
        this.duracaoMinutos = duracaoMinutos;
    }

    // Construtor 2 (Sobrecarga)
    public DVD(int id, String titulo, int ano, int duracaoMinutos, boolean disponivel) {
        super(id, titulo, ano);
        this.setDisponivel(disponivel);
        this.duracaoMinutos = duracaoMinutos;
    }

    public int getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(int duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }

    @Override
    public String getTipoItem() { return "DVD"; }

    @Override
    public String toString() {
        return super.toString() + " | Duração: " + duracaoMinutos + " min";
    }
}