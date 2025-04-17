package UserInterface;

import DetectAdjacentProperties.AdjacencyDetector;
import DetectAdjacentProperties.AdjacentPropertyPair;
import DetectAdjacentProperties.PropertyPolygon;
import UploadCSV.CsvException;
import UploadCSV.CsvLogger;
import UploadCSV.CsvUploader;
import UploadCSV.CsvValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.SwingWorker;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("GeoOrganizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBackground(new Color(30, 30, 30)); // cor escura
        add(sidebar, BorderLayout.WEST);

// Logo no topo da Sidebar
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon(getClass().getClassLoader().getResource("Images/logo_Side.png"));  // Carregar o logo
// Redimensionar a imagem para o tamanho desejado (por exemplo, 150x150 pixels)
        Image logoImage = logoIcon.getImage(); // Obtém a imagem do logo
        Image resizedImageSide = logoImage.getScaledInstance(200, 80, Image.SCALE_SMOOTH); // Redimensiona a imagem
        logoIcon = new ImageIcon(resizedImageSide); // Cria um novo ImageIcon com a imagem redimensionada
// Define o ícone redimensionado no JLabel
        logoLabel.setIcon(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logoLabel);

// Adiciona um espaçador para empurrar o botão para o fim
        sidebar.add(Box.createVerticalGlue());

// Botão de Importar CSV
        JButton importCsvButton = new JButton("Importar CSV");

// Configura o texto para ser branco
        importCsvButton.setForeground(Color.WHITE);

// Define o fundo do botão para a cor da sidebar (escuro)
        importCsvButton.setOpaque(true);
        importCsvButton.setBackground(new Color(30, 30, 30));  // Cor da sidebar
        importCsvButton.setBorderPainted(false); // Remove a borda padrão
        importCsvButton.setFocusPainted(false); // Remove o foco azul

// Aumenta o tamanho do botão
        importCsvButton.setPreferredSize(new Dimension(180, 50));

// Ajusta a fonte para aumentar o texto
        importCsvButton.setFont(new Font("SansSerif", Font.BOLD, 18));

// Configura o alinhamento do botão
        importCsvButton.setAlignmentX(Component.CENTER_ALIGNMENT);

// Adiciona o efeito de hover
        importCsvButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                // Mudando a cor de fundo para um tom de cinza mais claro quando o mouse entra
                importCsvButton.setBackground(new Color(50, 50, 50)); // Um tom de cinza mais claro
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                // Voltando ao fundo escuro da sidebar quando o mouse sai
                importCsvButton.setBackground(new Color(30, 30, 30)); // Cor original da sidebar
            }
        });

        importCsvButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecionar ficheiro CSV");
            int result = fileChooser.showOpenDialog(MainFrame.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                // Mostra spinner
                LoadingDialogSpinner loading = new LoadingDialogSpinner(MainFrame.this);
                SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() {
                        try {
                            CsvUploader uploader = new CsvUploader();
                            CsvValidator validator = new CsvValidator();

                            List<String[]> data = uploader.uploadCsv(selectedFile.getAbsolutePath());
                            validator.validate(data);

                            List<PropertyPolygon> properties = AdjacencyDetector.convertToProperties(data);
                            List<AdjacentPropertyPair> adjacentProperties = AdjacencyDetector.findAdjacentProperties(properties);

                            // Podes guardar os dados se quiseres
                        } catch (Exception ex) {
                            CsvLogger.logError("Erro ao importar: " + ex.getMessage());
                            JOptionPane.showMessageDialog(MainFrame.this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        loading.dispose(); // Fecha o spinner
                        showSuccessDialog("CSV importado com sucesso!");                    }
                };

                worker.execute();
                loading.setVisible(true); // Mostra enquanto processa
            }
        });

// Adiciona o botão à sidebar
        sidebar.add(importCsvButton);

        // Content panel (centro da tela)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

// Logo central (substituir "LOGO" por um JLabel com o logo)
        JLabel centerLogo = new JLabel();
        logoIcon = new ImageIcon(getClass().getClassLoader().getResource("Images/logo.png"));  // Carregar o logo

// Redimensionar a imagem para o tamanho desejado
// Aqui ajustamos para um logo que ocupe uma parte significativa da tela (exemplo 500x500 pixels)
        logoImage = logoIcon.getImage(); // Obtém a imagem do logo
        Image resizedImageCenter = logoImage.getScaledInstance(500, 500, Image.SCALE_SMOOTH); // Redimensiona a imagem
        logoIcon = new ImageIcon(resizedImageCenter); // Cria um novo ImageIcon com a imagem redimensionada

// Define o ícone redimensionado no JLabel
        centerLogo.setIcon(logoIcon);

// Centralizando o logo no painel
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Alinhamento ao centro
        logoPanel.add(centerLogo);  // Adiciona o logo ao painel
        contentPanel.add(logoPanel, BorderLayout.CENTER);  // Adiciona o painel ao contentPanel
    }

    public void showSuccessDialog(String message) {
        JDialog dialog = new JDialog(this, "Sucesso", true);
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Ícone verde de check
        JLabel iconLabel = new JLabel("\u2714"); // ✔
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        iconLabel.setForeground(new Color(0, 153, 0));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Texto da mensagem
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botão de OK
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(okButton);

        dialog.add(iconLabel, BorderLayout.NORTH);
        dialog.add(messageLabel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}