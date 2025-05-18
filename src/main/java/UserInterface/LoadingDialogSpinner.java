package UserInterface;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * A modal dialog that displays a loading animation and message while a process is running.
 * <p>
 * This dialog is intended to be shown during long-running operations, such as importing or validating a file,
 * to inform the user that the application is busy.
 */
public class LoadingDialogSpinner extends JDialog {


    /**
     * Constructs a new {@code LoadingDialogSpinner} attached to a parent frame.
     *
     * @param parent the parent {@link JFrame} to which this dialog is modal.
     */
    public LoadingDialogSpinner(JFrame parent) {
        super(parent, "Processing...", true);
        setLayout(new BorderLayout());
        setUndecorated(true);

        // Painel principal com fundo branco
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE); // fundo branco
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        ImageIcon loadingIcon = new ImageIcon();

        // Ícone de loading
        URL loadingUrl = getClass().getResource("/Images/loading.gif");

        if (loadingUrl != null) {
            loadingIcon = new ImageIcon(loadingUrl);
        } else {
            System.err.println("⚠️ Imagem '/Images/loading.gif' não encontrada.");
            loadingIcon = new ImageIcon(); // Ícone vazio para evitar null
        }

        JLabel iconLabel = new JLabel(loadingIcon, JLabel.CENTER);
        iconLabel.setOpaque(false); // não força fundo
        iconLabel.setBackground(Color.WHITE); // só se necessário

        // Texto
        JLabel loadingLabel = new JLabel("Loading...", JLabel.CENTER);
        loadingLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        loadingLabel.setOpaque(false);
        loadingLabel.setForeground(Color.BLACK);
        loadingLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Junta tudo
        panel.add(iconLabel, BorderLayout.CENTER);
        panel.add(loadingLabel, BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);
        setBackground(new Color(0, 0, 0, 0)); // transparente

        setSize(250, 150);
        setLocationRelativeTo(parent);
    }
}