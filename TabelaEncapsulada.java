import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class TabelaEncapsulada extends JTable {
    private final DefaultTableModel modelo;

    public TabelaEncapsulada(String[] colunas) {
        this.modelo = new DefaultTableModel(colunas, 0);
        this.setModel(this.modelo);

        // Remove o editor de texto das celulas (impede digitar dentro dos quadrados)
        this.setDefaultEditor(Object.class, null);

        // Permite selecionar apenas uma linha por vez e trava a ordem das colunas
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.getTableHeader().setReorderingAllowed(false);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void limpar() {
        modelo.setRowCount(0);
    }

    public void adicionarLinha(Object[] dados) {
        modelo.addRow(dados);
    }

    public Object obterValor(int linha, int coluna) {
        return modelo.getValueAt(linha, coluna);
    }
}