package UserInterface;

import Utils.Annotations.Layer;
import Utils.Enums.LayerType;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * The {@code LoadingDialogSpinner} class represents a modal dialog that displays
 * a loading spinner and a message while a process is running.
 * It is designed to be used in a graphical user interface (GUI) application.
 */
@Layer(LayerType.FRONT_END)
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

        // Ícone de loading
        ImageIcon loadingIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/loading.gif")));
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