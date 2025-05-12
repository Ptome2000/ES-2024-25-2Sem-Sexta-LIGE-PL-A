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
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.SwingWorker;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private JPanel contentPanelCenter;
    private VisualizationViewer<PropertyPolygon, String> viewer;
    private PropertyCollector collector;

    private JLabel districtTitle;
    private JLabel municipalityTitle;
    private JLabel parishTitle;
    private JLabel ownerTitle;

    private JLabel numPropsByDistrictLabel;
    private JLabel numPropsByMunicipalityLabel;
    private JLabel numPropsByParishLabel;
    private JLabel numPropsByOwnerLabel;

    private JLabel avgPropsByDistrictLabel;
    private JLabel avgPropsByMunicipalityLabel;
    private JLabel avgPropsByParishLabel;
    private JLabel avgPropsByOwnerLabel;

    private JComboBox<String> districtJComboBox;
    private JComboBox<String> municipalityJComboBox;
    private JComboBox<String> parishJComboBox;
    private JComboBox<String> ownerJComboBox;

    private JCheckBox toggleShowOwnerId;

    private JPanel graphPanel;
    private edu.uci.ics.jung.graph.Graph<PropertyPolygon, String> jungGraph;

    private boolean showOwnerIds = false;
    private List<PropertyPolygon> currentDisplayedProperties;

    public MainFrame() {
        setTitle("GeoOrganizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 1024);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

// === SIDEBAR ===
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBackground(new Color(30, 30, 30)); // fundo escuro
        add(sidebar, BorderLayout.WEST);

// === LOGO NO TOPO ===
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon(getClass().getClassLoader().getResource("Images/logo_Side.png"));
        Image logoImage = logoIcon.getImage();
        Image resizedImageSide = logoImage.getScaledInstance(200, 80, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(resizedImageSide);
        logoLabel.setIcon(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logoLabel);

// === COMBOBOXES COM LABELS ===
        Color sidebarBg = new Color(30, 30, 30);
        Color white = Color.WHITE;
        Font labelFont = new Font("SansSerif", Font.BOLD, 13);
        Font comboFont = new Font("SansSerif", Font.PLAIN, 14);
        Dimension comboSize = new Dimension(220, 36);

        JLabel districtLabel = new JLabel("District:");
        districtLabel.setForeground(white);
        districtLabel.setFont(labelFont);

        JLabel municipalityLabel = new JLabel("Municipality:");
        municipalityLabel.setForeground(white);
        municipalityLabel.setFont(labelFont);

        JLabel parishLabel = new JLabel("Parish:");
        parishLabel.setForeground(white);
        parishLabel.setFont(labelFont);

        JLabel ownerLabel = new JLabel("Owner ID:");
        ownerLabel.setForeground(white);
        ownerLabel.setFont(labelFont);

        districtJComboBox = new JComboBox<>();
        municipalityJComboBox = new JComboBox<>();
        parishJComboBox = new JComboBox<>();
        ownerJComboBox = new JComboBox<>();

        districtLabel.setVisible(false);
        municipalityLabel.setVisible(false);
        parishLabel.setVisible(false);
        ownerLabel.setVisible(false);
        districtJComboBox.setVisible(false);
        municipalityJComboBox.setVisible(false);
        parishJComboBox.setVisible(false);
        ownerJComboBox.setVisible(false);

        districtJComboBox.addItem(null);
        municipalityJComboBox.addItem(null);
        parishJComboBox.addItem(null);
        ownerJComboBox.addItem(null);

// Aplica estilo a cada combo
        JComboBox<?>[] combos = {districtJComboBox, municipalityJComboBox, parishJComboBox, ownerJComboBox};
        for (JComboBox<?> combo : combos) {
            combo.setBackground(sidebarBg);
            combo.setForeground(white);
            combo.setFont(comboFont);
            combo.setPreferredSize(comboSize);
            combo.setMaximumSize(comboSize);
            combo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            combo.setFocusable(false);
            combo.setOpaque(true);
            combo.setUI(new BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton button = new JButton("\u25BC");
                    button.setFont(new Font("SansSerif", Font.BOLD, 12));
                    button.setForeground(white);
                    button.setBackground(new Color(50, 50, 50));
                    button.setBorder(BorderFactory.createEmptyBorder());
                    return button;
                }
            });
            combo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                              boolean isSelected, boolean cellHasFocus) {
                    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    c.setBackground(isSelected ? new Color(50, 50, 50) : sidebarBg);
                    c.setForeground(white);
                    setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                    return c;
                }
            });
            combo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                              boolean isSelected, boolean cellHasFocus) {
                    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                    // Fundo branco quando dropdown está aberta
                    list.setBackground(Color.WHITE);
                    list.setSelectionBackground(new Color(220, 220, 220)); // cor ao selecionar
                    list.setSelectionForeground(Color.BLACK);

                    // Estilo da célula
                    c.setBackground(isSelected ? new Color(220, 220, 220) : Color.WHITE);
                    c.setForeground(Color.BLACK);
                    setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                    return c;
                }
            });
        }

// Adiciona à sidebar com espaçamento
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(districtLabel);
        sidebar.add(districtJComboBox);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(municipalityLabel);
        sidebar.add(municipalityJComboBox);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(parishLabel);
        sidebar.add(parishJComboBox);
        sidebar.add(Box.createVerticalStrut(30));
        sidebar.add(ownerLabel);
        sidebar.add(ownerJComboBox);

// === ESPAÇADOR PARA EMPURRAR O BOTÃO PARA BAIXO ===
        sidebar.add(Box.createVerticalGlue());

// === BOTÃO DE IMPORTAR CSV ===
        JButton importCsvButton = new JButton("Import CSV");
        importCsvButton.setForeground(Color.WHITE);
        importCsvButton.setOpaque(true);
        importCsvButton.setBackground(new Color(30, 30, 30));
        importCsvButton.setBorderPainted(false);
        importCsvButton.setFocusPainted(false);
        importCsvButton.setPreferredSize(new Dimension(180, 50));
        importCsvButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        importCsvButton.setAlignmentX(Component.CENTER_ALIGNMENT);

// Hover effect
        importCsvButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                importCsvButton.setBackground(new Color(50, 50, 50));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                importCsvButton.setBackground(new Color(30, 30, 30));
            }
        });

// === LÓGICA DE IMPORTAÇÃO DO CSV ===
        importCsvButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select CSV File");
            int result = fileChooser.showOpenDialog(MainFrame.this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().toLowerCase().endsWith(".csv")) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Por favor, selecione um ficheiro CSV válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LoadingDialogSpinner loading = new LoadingDialogSpinner(MainFrame.this);

                SwingWorker<Void, Void> worker = new SwingWorker<>() {

                    @Override
                    protected Void doInBackground() {
                        try {
                            List<District> properties = CsvProcessor.convertToRegionsAndProperties(selectedFile.getAbsolutePath());
                            collector = new PropertyCollector(properties);
                            updateGraph(collector.collectAllProperties());

                            toggleShowOwnerId.setVisible(true);
                            ownerLabel.setVisible(true);
                            ownerJComboBox.setVisible(true);
                            districtLabel.setVisible(true);
                            districtJComboBox.setVisible(true);

                            collector.getOwnerIds().stream()
                                    .map(Integer::parseInt)
                                    .sorted()
                                    .map(String::valueOf)
                                    .forEach(ownerJComboBox::addItem);

                            ownerJComboBox.addActionListener(e -> {
                                String selectedOwnerId = (String) ownerJComboBox.getSelectedItem();
                                if (selectedOwnerId != null) {
                                    String selectedDistrict = (String) districtJComboBox.getSelectedItem();
                                    String selectedMunicipality = (String) municipalityJComboBox.getSelectedItem();
                                    String selectedParish = (String) parishJComboBox.getSelectedItem();

                                    List<PropertyPolygon> filtered;

                                    if (selectedParish != null) {
                                        filtered = collector.collectPropertiesByOwnerAndParish(selectedOwnerId, selectedParish);
                                    } else if (selectedMunicipality != null) {
                                        filtered = collector.collectPropertiesByOwnerAndMunicipality(selectedOwnerId, selectedMunicipality);
                                    } else if (selectedDistrict != null) {
                                        filtered = collector.collectPropertiesByOwnerAndDistrict(selectedOwnerId, selectedDistrict);
                                    } else {
                                        // Nenhum filtro geográfico → mostra tudo e limpa campos geográficos
                                        filtered = collector.collectAllPropertiesByOwner(selectedOwnerId);
                                        districtJComboBox.setSelectedItem(null);
                                        municipalityJComboBox.setSelectedItem(null);
                                        parishJComboBox.setSelectedItem(null);
                                        setDistrictTitle("District - NA");
                                        clearMunicipalityInfo();
                                        clearParishInfo();
                                    }
                                    ownerTitle.setText("Owner - " + selectedOwnerId);
                                    numPropsByOwnerLabel.setText("Amount of Properties: " + filtered.size());

                                    double avgSize = filtered.stream()
                                            .mapToDouble(PropertyPolygon::getShapeArea)
                                            .average()
                                            .orElse(0.0);
                                    avgPropsByOwnerLabel.setText(String.format("Average Area: %.2f m²", avgSize));

                                    updateGraph(filtered);
                                }
                            });

                            List<String> districts = collector.getDistrictNames();
                            for (String d : districts) districtJComboBox.addItem(d);

                            districtJComboBox.addActionListener(e -> {
                                String selectedDistrict = (String) districtJComboBox.getSelectedItem();

                                ownerJComboBox.setSelectedItem(null);
                                clearOwnerInfo();

                                if (selectedDistrict != null) {
                                    setDistrictTitle("District - " + selectedDistrict);
                                    List<PropertyPolygon> p = collector.filterByDistrict(selectedDistrict);
                                    updateGraph(p);
                                    updateDistrictInfo(p);

                                    List<String> municipalities = collector.getMunicipalityNames(selectedDistrict);
                                    municipalityJComboBox.removeAllItems();
                                    municipalityJComboBox.addItem(null);
                                    parishJComboBox.removeAllItems();
                                    parishJComboBox.addItem(null);
                                    for (String m : municipalities) municipalityJComboBox.addItem(m);
                                    municipalityLabel.setVisible(true);
                                    municipalityJComboBox.setVisible(true);
                                } else {
                                    clearDistrictInfo();
                                    clearMunicipalityInfo();
                                    clearParishInfo();
                                    municipalityJComboBox.removeAllItems();
                                    municipalityJComboBox.setSelectedItem(null);
                                    parishJComboBox.removeAllItems();
                                    parishJComboBox.addItem(null);

                                }
                            });

                            municipalityJComboBox.addActionListener(e -> {
                                String selectedMunicipality = (String) municipalityJComboBox.getSelectedItem();

                                ownerJComboBox.setSelectedItem(null);
                                clearOwnerInfo();

                                setMunicipalityTitle("Municipality - " + selectedMunicipality);
                                if (selectedMunicipality != null) {
                                    List<PropertyPolygon> p = collector.filterByMunicipality(selectedMunicipality);
                                    updateGraph(p);
                                    updateMunicipalityInfo(p);
                                    List<String> parishes = collector.getParishNames(selectedMunicipality);
                                    parishJComboBox.removeAllItems();
                                    parishJComboBox.addItem(null);
                                    for (String parish : parishes) parishJComboBox.addItem(parish);
                                    parishLabel.setVisible(true);
                                    parishJComboBox.setVisible(true);
                                } else {
                                    clearMunicipalityInfo();
                                    municipalityJComboBox.setSelectedItem(null);
                                    parishJComboBox.removeAllItems();
                                    parishJComboBox.addItem(null);
                                    clearParishInfo();
                                }
                            });

                            parishJComboBox.addActionListener(e -> {
                                String selectedParish = (String) parishJComboBox.getSelectedItem();

                                ownerJComboBox.setSelectedItem(null);
                                clearOwnerInfo();

                                setParishTitle("Parish - " + selectedParish);
                                if (selectedParish != null) {
                                    List<PropertyPolygon> p = collector.filterByParish(selectedParish);
                                    updateGraph(p);
                                    updateParishInfo(p);
                                } else {
                                    clearParishInfo();
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
                        loading.dispose();
                        graphPanel = GraphViewer.createGraphPanel(jungGraph, 1024, 1024, showOwnerIds);

                        SwingUtilities.invokeLater(() -> {
                            contentPanelCenter.removeAll(); // <- substitui só o centro
                            contentPanelCenter.setLayout(new BorderLayout()); // se precisares de layout diferente
                            contentPanelCenter.add(graphPanel, BorderLayout.CENTER);
                            contentPanelCenter.revalidate();
                            contentPanelCenter.repaint();
                        });

                        showSuccessDialog("CSV importado com sucesso!");
                    }
                };

                worker.execute();
                loading.setVisible(true);
            }
        });

// === ADICIONA BOTÃO À SIDEBAR ===
        sidebar.add(importCsvButton);

//######################################################################################################################//

// === CONTENT PANEL ===
// Content panel (centro da tela)
        contentPanel = new JPanel(); // Usa o campo da classe
        contentPanel.setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

// Subpainel central dentro do contentPanel para conteúdo dinâmico (ex: logo ou grafo)
        contentPanelCenter = new JPanel();
        contentPanelCenter.setLayout(new FlowLayout(FlowLayout.CENTER)); // layout inicial
        contentPanel.add(contentPanelCenter, BorderLayout.CENTER);

// === LOGO CENTRAL ===
        JLabel centerLogo = new JLabel();
        logoIcon = new ImageIcon(getClass().getClassLoader().getResource("Images/logo.png")); // Carrega o logo

// Redimensionar a imagem do logo para 500x500 pixels
        logoImage = logoIcon.getImage();
        Image resizedImageCenter = logoImage.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(resizedImageCenter);
        centerLogo.setIcon(logoIcon); // Define o ícone redimensionado no JLabel

// Painel centralizado com o logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.add(centerLogo);
        contentPanelCenter.add(logoPanel); // Adiciona o logo ao contentPanelCenter

// === PAINEL INFERIOR COM DOIS LADOS ===
        JPanel bottomPanel = new JPanel(new BorderLayout());

// Painel da esquerda (para toggle de IDs)
        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toggleShowOwnerId = new JCheckBox("Show Owner's ID");
        toggleShowOwnerId.setSelected(false);
        toggleShowOwnerId.setVisible(false);

// Estilo básico do checkbox para parecer moderno
        toggleShowOwnerId.setForeground(Color.WHITE);
        toggleShowOwnerId.setBackground(new Color(30, 30, 30));
        toggleShowOwnerId.setFocusPainted(false);
        toggleShowOwnerId.setFont(new Font("SansSerif", Font.BOLD, 12));
        toggleShowOwnerId.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleShowOwnerId.setOpaque(true);

        Dimension toggleShowOwnerIdSize = toggleShowOwnerId.getPreferredSize();
        toggleShowOwnerId.setPreferredSize(new Dimension(toggleShowOwnerIdSize.width, 30));
        bottomLeftPanel.add(toggleShowOwnerId);

        toggleShowOwnerId.addActionListener(e -> {
            showOwnerIds = toggleShowOwnerId.isSelected();
            updateGraph(currentDisplayedProperties);
        });

// Painel da direita (para toggle de info)
        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JToggleButton toggleInfoToggle = new JToggleButton("Hide Info");
        toggleInfoToggle.setSelected(true);

// Estilo do botão de info
        toggleInfoToggle.setForeground(Color.WHITE);
        toggleInfoToggle.setOpaque(true);
        toggleInfoToggle.setBackground(new Color(30, 30, 30));
        toggleInfoToggle.setBorderPainted(false);
        toggleInfoToggle.setFocusPainted(false);
        Dimension toggleInfoToggleSize = toggleInfoToggle.getPreferredSize();
        toggleInfoToggle.setPreferredSize(new Dimension(toggleInfoToggleSize.width, 30));
        toggleInfoToggle.setFont(new Font("SansSerif", Font.BOLD, 12));
        toggleInfoToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleInfoToggle.setUI(new BasicToggleButtonUI());
        toggleInfoToggle.setContentAreaFilled(false);
        toggleInfoToggle.setOpaque(true);
        toggleInfoToggle.addChangeListener(e -> {
            toggleInfoToggle.setBackground(new Color(30, 30, 30));
        });

        bottomRightPanel.add(toggleInfoToggle);

// Junta os dois lados ao painel inferior
        bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);
        bottomPanel.add(bottomRightPanel, BorderLayout.EAST);

// Adiciona o painel inferior ao contentPanel
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

//######################################################################################################################//

// === INFO PANEL ===
        JPanel graphInfoPanel = new JPanel();
        graphInfoPanel.setLayout(new BoxLayout(graphInfoPanel, BoxLayout.Y_AXIS));
        graphInfoPanel.setPreferredSize(new Dimension(400, getHeight()));
        graphInfoPanel.setBackground(new Color(245, 245, 245)); // Fundo claro

// Título
        JLabel infoTitle = new JLabel("Graph Informations");
        infoTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        infoTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

// Labels
        districtTitle = new JLabel("District - NA");
        districtTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        districtTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        numPropsByDistrictLabel = new JLabel("Amount of Properties: - NA");
        numPropsByDistrictLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        numPropsByDistrictLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avgPropsByDistrictLabel = new JLabel("Average Area: - NA");
        avgPropsByDistrictLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        avgPropsByDistrictLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        municipalityTitle = new JLabel("Municipality - NA");
        municipalityTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        municipalityTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        numPropsByMunicipalityLabel = new JLabel("Amount of Properties: - NA");
        numPropsByMunicipalityLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        numPropsByMunicipalityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avgPropsByMunicipalityLabel = new JLabel("Average Area: - NA");
        avgPropsByMunicipalityLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        avgPropsByMunicipalityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        parishTitle = new JLabel("Parish - NA");
        parishTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        parishTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        numPropsByParishLabel = new JLabel("Amount of Properties: - NA");
        numPropsByParishLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        numPropsByParishLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avgPropsByParishLabel = new JLabel("Average Area: - NA");
        avgPropsByParishLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        avgPropsByParishLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator ownerSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        ownerSeparator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1)); // largura total, altura fina
        ownerSeparator.setForeground(new Color(30, 30, 30)); // cor da linha

        // Owner Section
        ownerTitle = new JLabel("Owner - NA");
        ownerTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        ownerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        numPropsByOwnerLabel = new JLabel("Amount of Properties: - NA");
        numPropsByOwnerLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        numPropsByOwnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avgPropsByOwnerLabel = new JLabel("Average Area: - NA");
        avgPropsByOwnerLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        avgPropsByOwnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

// Toggle de visibilidade
        toggleInfoToggle.addActionListener(e -> {
            boolean isSelected = toggleInfoToggle.isSelected();
            graphInfoPanel.setVisible(isSelected);
            toggleInfoToggle.setText(isSelected ? "Hide Info" : "Show Info");
            MainFrame.this.revalidate();
            MainFrame.this.repaint();
        });

// Adiciona os componentes ao painel
        graphInfoPanel.add(Box.createVerticalStrut(20));
        graphInfoPanel.add(infoTitle);
        graphInfoPanel.add(Box.createVerticalStrut(10));

        graphInfoPanel.add(districtTitle);
        graphInfoPanel.add(numPropsByDistrictLabel);
        graphInfoPanel.add(Box.createVerticalStrut(5));
        graphInfoPanel.add(avgPropsByDistrictLabel);
        graphInfoPanel.add(Box.createVerticalGlue());

        graphInfoPanel.add(municipalityTitle);
        graphInfoPanel.add(numPropsByMunicipalityLabel);
        graphInfoPanel.add(Box.createVerticalStrut(5));
        graphInfoPanel.add(avgPropsByMunicipalityLabel);
        graphInfoPanel.add(Box.createVerticalGlue());

        graphInfoPanel.add(parishTitle);
        graphInfoPanel.add(numPropsByParishLabel);
        graphInfoPanel.add(Box.createVerticalStrut(5));
        graphInfoPanel.add(avgPropsByParishLabel);
        graphInfoPanel.add(Box.createVerticalGlue());

        graphInfoPanel.add(Box.createVerticalStrut(10)); // Espaço antes da linha
        graphInfoPanel.add(ownerSeparator);
        graphInfoPanel.add(Box.createVerticalStrut(10)); // Espaço após a linha

        graphInfoPanel.add(ownerTitle);
        graphInfoPanel.add(numPropsByOwnerLabel);
        graphInfoPanel.add(Box.createVerticalStrut(5));
        graphInfoPanel.add(avgPropsByOwnerLabel);
        graphInfoPanel.add(Box.createVerticalGlue());

// Adiciona ao MainFrame
        add(graphInfoPanel, BorderLayout.EAST);
    }

    //FUNÇÕES AUXILIARES
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

    private void updateDistrictInfo(List<PropertyPolygon> propriedades) {
        int total = propriedades.size();
        double media = propriedades.stream()
                .mapToDouble(PropertyPolygon::getShapeArea) // assumes getArea() exists
                .average()
                .orElse(0.0);

        numPropsByDistrictLabel.setText("Amount of Properties: " + total);
        avgPropsByDistrictLabel.setText(String.format("Average Area: %.2f m²", media));
    }

    private void updateMunicipalityInfo(List<PropertyPolygon> propriedades) {
        int total = propriedades.size();
        double media = propriedades.stream()
                .mapToDouble(PropertyPolygon::getShapeArea) // assumes getArea() exists
                .average()
                .orElse(0.0);
        numPropsByMunicipalityLabel.setText("Amount of Properties: " + total);
        avgPropsByMunicipalityLabel.setText(String.format("Average Area: %.2f m²", media));
    }

    private void updateParishInfo(List<PropertyPolygon> propriedades) {
        int total = propriedades.size();
        double media = propriedades.stream()
                .mapToDouble(PropertyPolygon::getShapeArea) // assumes getArea() exists
                .average()
                .orElse(0.0);
        numPropsByParishLabel.setText("Amount of Properties: " + total);
        avgPropsByParishLabel.setText(String.format("Average Area: %.2f m²", media));
    }

    public void updateGraph(List<PropertyPolygon> propriedades) {
        currentDisplayedProperties = propriedades;
        jungGraph = PropertyGraphJungBuilder.buildGraph(propriedades);
        graphPanel = GraphViewer.createGraphPanel(jungGraph, 1024, 1024, showOwnerIds);

        SwingUtilities.invokeLater(() -> {
            contentPanelCenter.removeAll();
            contentPanelCenter.setLayout(new BorderLayout());
            contentPanelCenter.add(graphPanel, BorderLayout.CENTER);
            contentPanelCenter.revalidate();
            contentPanelCenter.repaint();
        });
    }

    public void setDistrictTitle(String text) {
        if (districtTitle != null) districtTitle.setText(text);
    }

    public void setMunicipalityTitle(String text) {
        if (municipalityTitle != null) municipalityTitle.setText(text);
    }

    public void setParishTitle(String text) {
        if (parishTitle != null) parishTitle.setText(text);
    }

    private void clearDistrictInfo() {
        districtTitle.setText("District - NA");
        numPropsByDistrictLabel.setText("Amount of Properties: - NA");
        avgPropsByDistrictLabel.setText("Average Area: - NA");
    }

    private void clearMunicipalityInfo() {
        municipalityTitle.setText("Municipality - NA");
        numPropsByMunicipalityLabel.setText("Amount of Properties: - NA");
        avgPropsByMunicipalityLabel.setText("Average Area: - NA");
    }

    private void clearParishInfo() {
        parishTitle.setText("Parish - NA");
        numPropsByParishLabel.setText("Amount of Properties: - NA");
        avgPropsByParishLabel.setText("Average Area: - NA");
    }

    private void clearOwnerInfo() {
        ownerTitle.setText("Owner - NA");
        numPropsByOwnerLabel.setText("Amount of Properties: - NA");
        avgPropsByOwnerLabel.setText("Average Area: - NA");
    }

    //MAIN
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}