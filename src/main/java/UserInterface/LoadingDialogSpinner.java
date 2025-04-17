package UserInterface;

import javax.swing.*;
import java.awt.*;

public class LoadingDialogSpinner extends JDialog {
    public LoadingDialogSpinner(JFrame parent) {
        super(parent, "A processar...", true);
        setLayout(new BorderLayout());
        setUndecorated(true);

        // Painel principal com fundo branco
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE); // fundo branco
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Ícone de loading
        ImageIcon loadingIcon = new ImageIcon(getClass().getResource("/Images/loading.gif"));
        JLabel iconLabel = new JLabel(loadingIcon, JLabel.CENTER);
        iconLabel.setOpaque(false); // não força fundo
        iconLabel.setBackground(Color.WHITE); // só se necessário

        // Texto
        JLabel loadingLabel = new JLabel("A importar e validar o ficheiro...", JLabel.CENTER);
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