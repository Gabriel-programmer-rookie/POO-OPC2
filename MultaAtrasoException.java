public class MultaAtrasoException extends Exception {
    private final double valorMulta;
    private final long diasAtraso;

    public MultaAtrasoException(long diasAtraso, double valorMulta) {
        super(String.format("Devolução realizada com atraso de %d dia(s). Multa gerada: R$ %.2f", diasAtraso, valorMulta));
        this.diasAtraso = diasAtraso;
        this.valorMulta = valorMulta;
    }

    public double getValorMulta() { return valorMulta; }
    public long getDiasAtraso() { return diasAtraso; }
}