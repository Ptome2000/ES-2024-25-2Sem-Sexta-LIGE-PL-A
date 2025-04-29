package UserInterface;


import Models.*;
import Repository.*;
import Services.*;

import edu.uci.ics.jung.visualization.VisualizationViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private VisualizationViewer<PropertyPolygon, String> viewer;
    private PropertyCollector collector;
    private JLabel numPropsLabel;
    private JLabel numPropsByMunicipalityLabel;
    private JLabel numPropsByParishLabel;
    private JLabel avgPropsByMunicipalityLabel;
    private JLabel avgPropsByParishLabel;
    private JComboBox<String> districtJComboBox;
    private JComboBox<String> municipalityJComboBox;
    private JComboBox<String> parishJComboBox;
    private JLabel avgSizeLabel;
    private JPanel graphPanel;


    public MainFrame() {
        setTitle("GeoOrganizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 1024);
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

        districtJComboBox = new JComboBox<>();
        districtJComboBox.setVisible(false);
        sidebar.add(districtJComboBox);
        districtJComboBox.addItem(null);

        municipalityJComboBox = new JComboBox<>();
        municipalityJComboBox.setVisible(false);
        municipalityJComboBox.addItem(null);
        sidebar.add(municipalityJComboBox);


        parishJComboBox = new JComboBox<>();
        parishJComboBox.setVisible(false);
        parishJComboBox.addItem(null);
        sidebar.add(parishJComboBox);


//        JCheckBox showOwnerIdCheckbox = new JCheckBox("Mostrar ID do proprietário");
//        showOwnerIdCheckbox.setForeground(Color.WHITE);
//        showOwnerIdCheckbox.setBackground(new Color(30, 30, 30));
//        showOwnerIdCheckbox.setFont(new Font("SansSerif", Font.PLAIN, 16));
//        showOwnerIdCheckbox.setAlignmentX(Component.CENTER_ALIGNMENT);
//
////        showOwnerIdCheckbox.addActionListener(e -> {
////            boolean mostrar = showOwnerIdCheckbox.isSelected();
////            if (viewer != null) {  // Verifica se viewer não é nulo
////                // Configura o transformer de rótulo de vértices baseado no estado do checkbox
////                viewer.getRenderContext().setVertexLabelTransformer(mostrar ?
////                        (PropertyPolygon p) -> String.valueOf(p.getOwner()) :
////                        (PropertyPolygon p) -> "");
////                viewer.repaint();  // Atualiza o gráfico para refletir a mudança
////            }
////        });

// Adiciona à sidebar
        sidebar.add(Box.createVerticalStrut(10));
//        sidebar.add(showOwnerIdCheckbox);

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
                String fileName = selectedFile.getName();
                if (!fileName.toLowerCase().endsWith(".csv")) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Por favor, selecione um ficheiro CSV válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return; // aborta a importação
                }

                // Mostra spinner
                LoadingDialogSpinner loading = new LoadingDialogSpinner(MainFrame.this);
                SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    edu.uci.ics.jung.graph.Graph<PropertyPolygon, String> jungGraph;
                    @Override
                    protected Void doInBackground() {
                        try {
                            List<District> properties = CsvProcessor.convertToRegionsAndProperties(selectedFile.getAbsolutePath());
                            collector = new PropertyCollector(properties);
                            jungGraph = PropertyGraphJungBuilder.buildGraph(collector.collectAllProperties());
                            updateInfoGrafo(collector.collectAllProperties());
                            // Podes guardar os dados se quiseres
                            List<String> district = collector.getDistrictNames();
                            for (String d : district) {
                                System.out.println("este é o output" + d);
                                districtJComboBox.addItem(d);
                            }
                            districtJComboBox.setVisible(true);

                            districtJComboBox.addActionListener(e -> {
                                String selectedDistrict = (String) districtJComboBox.getSelectedItem();
                                List<String> municipalities = collector.getMunicipalityNames(selectedDistrict);
                                for (String m : municipalities) {
                                    municipalityJComboBox.addItem(m);
                                }
                                municipalityJComboBox.setVisible(true);
                            });

                            municipalityJComboBox.addActionListener(e -> {
                               String selectedMunicipality = (String) municipalityJComboBox.getSelectedItem();
                               if (selectedMunicipality != null) {
                                   List<PropertyPolygon> p = collector.filterByMunicipality(selectedMunicipality);
                                   updateGraph(p);
                                   updateMunicipalityInfo(p);
                               }
                               List<String> parishes = collector.getParishNames(selectedMunicipality);


                               for (String p : parishes) {
                                   parishJComboBox.addItem(p);
                               }
                               parishJComboBox.setVisible(true);

                            });

                            parishJComboBox.addActionListener(e -> {
                                String selectedParish = (String) parishJComboBox.getSelectedItem();
                                if (selectedParish != null) {
                                    List<PropertyPolygon> p = collector.filterByParish(selectedParish);
                                    updateGraph(p);
                                    updateParishInfo(p);
                                }
                            });

                        } catch (Exception ex) {
                            CsvLogger.logError("Erro ao importar: " + ex.getMessage());
                            JOptionPane.showMessageDialog(MainFrame.this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        loading.dispose(); // Fecha o spinner
//                        // Criar painel com o grafo
//
                        graphPanel = GraphViewer.createGraphPanel(jungGraph, 1024, 1024);
//
                        // Substituir conteúdo atual do centro pelo grafo
                        SwingUtilities.invokeLater(() -> {
                            contentPanel.removeAll();
                            contentPanel.add(graphPanel, BorderLayout.CENTER);
                            contentPanel.revalidate();
                            contentPanel.repaint();
                        });
                        showSuccessDialog("CSV importado com sucesso!");                    }
                };

                worker.execute();
                loading.setVisible(true); // Mostra enquanto processa
            }
        });

// Adiciona o botão à sidebar
        sidebar.add(importCsvButton);

        // Content panel (centro da tela)
        contentPanel = new JPanel(); // USA O CAMPO DA CLASSE
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

        // Painel de informações (lado direito do MainFrame, separado do contentPanel)

        JPanel graphInfoPanel = new JPanel();

        graphInfoPanel.setLayout(new BoxLayout(graphInfoPanel, BoxLayout.Y_AXIS));

        graphInfoPanel.setPreferredSize(new Dimension(400, getHeight()));

        graphInfoPanel.setBackground(new Color(245, 245, 245)); // fundo claro



        JLabel infoTitle = new JLabel("Informações do Grafo");

        infoTitle.setFont(new Font("SansSerif", Font.BOLD, 18));

        infoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));



        numPropsLabel = new JLabel("Número de propriedades: -");
        numPropsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        numPropsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        avgSizeLabel = new JLabel("Tamanho médio das propriedades: -");
        avgSizeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        avgSizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        numPropsByMunicipalityLabel = new JLabel("Propriedades por município: -");
        numPropsByMunicipalityLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        numPropsByMunicipalityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        avgPropsByMunicipalityLabel = new JLabel("Propriedades por município: -");
        avgPropsByMunicipalityLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        avgPropsByMunicipalityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        numPropsByParishLabel = new JLabel("Propriedades por freguesia: -");
        numPropsByParishLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        numPropsByParishLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        avgPropsByParishLabel = new JLabel("Propriedades por freguesia: -");
        avgPropsByParishLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        avgPropsByParishLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        graphInfoPanel.add(Box.createVerticalStrut(20));

        graphInfoPanel.add(infoTitle);

        graphInfoPanel.add(Box.createVerticalStrut(10));

        graphInfoPanel.add(numPropsLabel);

        graphInfoPanel.add(Box.createVerticalStrut(5));

        graphInfoPanel.add(avgSizeLabel);

        graphInfoPanel.add(Box.createVerticalGlue());

        graphInfoPanel.add(numPropsByMunicipalityLabel);
        graphInfoPanel.add(Box.createVerticalStrut(5));
        graphInfoPanel.add(avgPropsByMunicipalityLabel);
        graphInfoPanel.add(Box.createVerticalGlue());
        graphInfoPanel.add(numPropsByParishLabel);
        graphInfoPanel.add(Box.createVerticalStrut(5));
        graphInfoPanel.add(avgPropsByParishLabel);
        graphInfoPanel.add(Box.createVerticalGlue());

        add(graphInfoPanel, BorderLayout.EAST); // Aqui é fora do contentPanel!
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

    private void updateInfoGrafo(List<PropertyPolygon> propriedades) {
        int total = propriedades.size();
        double media = propriedades.stream()
                .mapToDouble(PropertyPolygon::getShapeArea) // assumes getArea() exists
                .average()
                .orElse(0.0);

        numPropsLabel.setText("Número de propriedades: " + total);
        avgSizeLabel.setText(String.format("Tamanho médio das propriedades: %.2f m²", media));
    }

    private void updateMunicipalityInfo(List<PropertyPolygon> propriedades) {
        int total = propriedades.size();
        double media = propriedades.stream()
                .mapToDouble(PropertyPolygon::getShapeArea) // assumes getArea() exists
                .average()
                .orElse(0.0);
        numPropsByMunicipalityLabel.setText("Propriedades por município: " + total);
        avgPropsByMunicipalityLabel.setText(String.format("Tamanho médio das propriedades: %.2f m²", media));
        // numPropsByMunicipalityLabel.setText("Propriedades por município: " + ...);
    }

    private void updateParishInfo(List<PropertyPolygon> propriedades) {
        int total = propriedades.size();
        double media = propriedades.stream()
                .mapToDouble(PropertyPolygon::getShapeArea) // assumes getArea() exists
                .average()
                .orElse(0.0);
        numPropsByParishLabel.setText("Propriedades por freguesia: " + total);
        avgPropsByParishLabel.setText(String.format("Tamanho médio das propriedades: %.2f m²", media));
    }
    private void updateGraph(List<PropertyPolygon> propriedades) {

        graphPanel.removeAll();
        graphPanel = GraphViewer.createGraphPanel(PropertyGraphJungBuilder.buildGraph(propriedades), 1024, 1024);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}