import javax.swing.SwingUtilities;

public class PrincipalBiblioteca {
    public static void main(String[] args) {
        // Inicializa a interface gráfica Swing
        SwingUtilities.invokeLater(() -> new BibliotecaGUI().setVisible(true));
    }
}