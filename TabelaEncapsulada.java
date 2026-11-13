import java.awt.Color;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

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

        // --- CORES DA TABELA ---
        Color azulMarinho = new Color(10, 41, 99);
        Color azulMedio = new Color(13, 110, 158);

        this.setBackground(Color.WHITE);
        this.setForeground(new Color(20, 35, 65));
        this.setGridColor(new Color(220, 230, 240));
        this.setSelectionBackground(azulMedio);
        this.setSelectionForeground(Color.WHITE);
        this.setRowHeight(24);

        JTableHeader header = this.getTableHeader();
        header.setBackground(azulMarinho);
        header.setForeground(Color.WHITE);
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        header.setOpaque(true);
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