package UserInterface;

import DetectAdjacentProperties.AdjacencyDetector;
import DetectAdjacentProperties.AdjacentPropertyPair;
import Models.*;
import Repository.*;
import Services.*;
import DetectAdjacentProperties.*;

import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.Objects;
import javax.swing.SwingWorker;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;


//TODO: Add javadocs for all methods and fix warnings (maybe if possible transfer some of the methods to a service controller class)

/**
 * The {@code MainFrame} class represents the main graphical user interface (GUI) of the application.
 * It provides a visual representation of properties and allows users to interact with them.
 * This class is responsible for displaying the main window, handling user interactions,
 * and updating the visualization based on user input.
 */
@Layer(LayerType.FRONT_END)
public class MainFrame extends JFrame {
    private final JPanel contentPanelCenter;
    private JPanel graphInfoPanel;
    private PropertyCollector collector;

    private final JLabel districtTitle;
    private final JLabel municipalityTitle;
    private final JLabel parishTitle;
    private final JLabel ownerTitle;

    private final JLabel numPropsByDistrictLabel;
    private final JLabel numPropsByMunicipalityLabel;
    private final JLabel numPropsByParishLabel;
    private final JLabel numPropsByOwnerLabel;

    private final JLabel avgPropsByDistrictLabel;
    private final JLabel avgPropsByMunicipalityLabel;
    private final JLabel avgPropsByParishLabel;
    private final JLabel avgPropsByOwnerLabel;

    private final JComboBox<String> districtJComboBox;
    private final JComboBox<String> municipalityJComboBox;
    private final JComboBox<String> parishJComboBox;
    private final JComboBox<String> ownerJComboBox;

    String activeFilterType = null;
    String activeFilterValue = null;

    private final JLabel currentlyDisplayingLabel = new JLabel();

    private final JCheckBox toggleShowOwnerId;
    private final JCheckBox toggleMergeSameOwnerProperties;

    private JPanel graphPanel;
    private edu.uci.ics.jung.graph.Graph<PropertyPolygon, String> jungGraph;

    private boolean showOwnerIds = false;
    private boolean mergeActive = false;
    private List<PropertyPolygon> currentDisplayedProperties;

    public List<PropertyPolygon> getCurrentDisplayedProperties() {
        return currentDisplayedProperties;
    }

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
        ImageIcon logoIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/logo_Side.png")));
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
                    JButton button = new JButton("▼");
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

// === BOTÃO DE IMPORTAR CSV ===
        JButton changeSuggestions = new JButton("Changes Suggested");
        changeSuggestions.setForeground(Color.WHITE);
        changeSuggestions.setOpaque(true);
        changeSuggestions.setBackground(new Color(30, 30, 30));
        changeSuggestions.setBorderPainted(false);
        changeSuggestions.setFocusPainted(false);
        changeSuggestions.setPreferredSize(new Dimension(180, 50));
        changeSuggestions.setFont(new Font("SansSerif", Font.BOLD, 18));
        changeSuggestions.setAlignmentX(Component.CENTER_ALIGNMENT);
        changeSuggestions.setVisible(false);

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

        // Hover effect
        changeSuggestions.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                changeSuggestions.setBackground(new Color(50, 50, 50));
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                changeSuggestions.setBackground(new Color(30, 30, 30));
            }
        });

        changeSuggestions.addActionListener(e -> {
            LoadingDialogSpinner loading = new LoadingDialogSpinner(MainFrame.this);

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    try {
                        if (collector == null) {
                            System.out.println("⚠️ collector is null");
                            return null;
                        }

                        List<AdjacentPropertyPair> adjacentPairs = AdjacencyDetector.findValidAdjacentPairs(getCurrentDisplayedProperties());

                        SwingUtilities.invokeLater(() -> {
                            showSuccessDialog("Suggestions generated successfully to " + activeFilterValue);
                            ChangeSuggestionsFrame csf = new ChangeSuggestionsFrame(getCurrentDisplayedProperties(), adjacentPairs, "Location");
                            csf.setVisible(true);
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    loading.dispose();
                }
            };

            worker.execute();
            loading.setVisible(true);
        });

// === LÓGICA DE IMPORTAÇÃO DO CSV ===
        importCsvButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a CSV File");
            int result = fileChooser.showOpenDialog(MainFrame.this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().toLowerCase().endsWith(".csv")) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Please select a valid CSV file.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LoadingDialogSpinner loading = new LoadingDialogSpinner(MainFrame.this);

                SwingWorker<Void, Void> worker = new SwingWorker<>() {

                    @Override
                    protected Void doInBackground() {
                        try {
                            districtJComboBox.removeAllItems();
                            municipalityJComboBox.removeAllItems();
                            parishJComboBox.removeAllItems();
                            ownerJComboBox.removeAllItems();

                            districtJComboBox.addItem(null);
                            municipalityJComboBox.addItem(null);
                            parishJComboBox.addItem(null);
                            ownerJComboBox.addItem(null);

                            districtJComboBox.setSelectedItem(null);
                            municipalityJComboBox.setSelectedItem(null);
                            parishJComboBox.setSelectedItem(null);
                            ownerJComboBox.setSelectedItem(null);

                            municipalityLabel.setVisible(false);
                            municipalityJComboBox.setVisible(false);
                            parishLabel.setVisible(false);
                            parishJComboBox.setVisible(false);
                            currentlyDisplayingLabel.setVisible(false);
                            activeFilterType = null;
                            activeFilterValue = null;

                            List<District> properties = CsvProcessor.convertToRegionsAndProperties(selectedFile.getAbsolutePath());
                            collector = new PropertyCollector(properties);
                            updateGraph(collector.collectAllProperties());

                            toggleShowOwnerId.setVisible(true);

                            districtLabel.setVisible(true);
                            districtJComboBox.setVisible(true);

                            ownerLabel.setVisible(true);
                            ownerJComboBox.setVisible(true);

                            collector.getOwnerIds().stream()
                                    .map(Integer::parseInt)
                                    .sorted()
                                    .map(String::valueOf)
                                    .forEach(ownerJComboBox::addItem);

                            ownerJComboBox.addActionListener(e -> {
                                String selectedOwnerId = (String) ownerJComboBox.getSelectedItem();
                                if (selectedOwnerId != null) {
                                    activeFilterType = "Owner";
                                    activeFilterValue = selectedOwnerId;
                                    currentlyDisplayingLabel.setText(
                                            "<html><span style='color: rgb(50,72,75); font-weight: bold;'>" + activeFilterValue + "</span> " +
                                                    "<span style='color: rgb(101,104,69);'>(" + activeFilterType + ")</span></html>"
                                    );

                                    setOwnerTitle("Owner - " + selectedOwnerId);

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
                                    activeFilterType = "District";
                                    activeFilterValue = selectedDistrict;
                                    currentlyDisplayingLabel.setText(
                                            "<html><span style='color: rgb(50,72,75); font-weight: bold;'>" + activeFilterValue + "</span> " +
                                                    "<span style='color: rgb(101,104,69);'>(" + activeFilterType + ")</span></html>"
                                    );                                    currentlyDisplayingLabel.setVisible(true);

                                    setDistrictTitle("District - " + selectedDistrict);
                                    List<PropertyPolygon> p = collector.filterByDistrict(selectedDistrict);
                                    updateGraph(p);

                                    toggleMergeSameOwnerProperties.setVisible(true);

                                    List<String> municipalities = collector.getMunicipalityNames(selectedDistrict);
                                    municipalityLabel.setVisible(true);
                                    municipalityJComboBox.setVisible(true);
                                    municipalityJComboBox.removeAllItems();
                                    municipalityJComboBox.addItem(null);
                                    parishJComboBox.removeAllItems();
                                    parishJComboBox.addItem(null);
                                    for (String m : municipalities) municipalityJComboBox.addItem(m);
                                    municipalityLabel.setVisible(true);
                                    municipalityJComboBox.setVisible(true);
                                    changeSuggestions.setVisible(true);
                                } else {
                                    currentlyDisplayingLabel.setVisible(false);
                                    clearDistrictInfo();
                                    clearMunicipalityInfo();
                                    clearParishInfo();
                                    municipalityLabel.setVisible(false);
                                    municipalityJComboBox.setVisible(false);
                                    municipalityJComboBox.removeAllItems();
                                    municipalityJComboBox.setSelectedItem(null);
                                    parishLabel.setVisible(true);
                                    parishJComboBox.setVisible(true);
                                    parishJComboBox.removeAllItems();
                                    parishJComboBox.addItem(null);
                                    changeSuggestions.setVisible(false);
                                }
                            });

                            municipalityJComboBox.addActionListener(e -> {
                                String selectedMunicipality = (String) municipalityJComboBox.getSelectedItem();

                                ownerJComboBox.setSelectedItem(null);
                                clearOwnerInfo();

                                setMunicipalityTitle("Municipality - " + selectedMunicipality);
                                if (selectedMunicipality != null) {
                                    activeFilterType = "Municipality";
                                    activeFilterValue = selectedMunicipality;
                                    currentlyDisplayingLabel.setText(
                                            "<html><span style='color: rgb(50,72,75); font-weight: bold;'>" + activeFilterValue + "</span> " +
                                                    "<span style='color: rgb(101,104,69);'>(" + activeFilterType + ")</span></html>"
                                    );

                                    List<PropertyPolygon> p = collector.filterByMunicipality(selectedMunicipality);
                                    updateGraph(p);
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
                                    activeFilterType = "Parish";
                                    activeFilterValue = selectedParish;
                                    currentlyDisplayingLabel.setText(
                                            "<html><span style='color: rgb(50,72,75); font-weight: bold;'>" + activeFilterValue + "</span> " +
                                                    "<span style='color: rgb(101,104,69);'>(" + activeFilterType + ")</span></html>"
                                    );
                                    List<PropertyPolygon> p = collector.filterByParish(selectedParish);
                                    updateGraph(p);
                                } else {
                                    clearParishInfo();
                                }
                            });

                        } catch (Exception ex) {
                            CsvLogger.logError("Error importing: " + ex.getMessage());
                            JOptionPane.showMessageDialog(MainFrame.this, "Error: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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

                        showSuccessDialog("CSV imported successfully!");
                    }
                };

                worker.execute();
                loading.setVisible(true);
            }
        });

// === ADICIONA BOTÃO À SIDEBAR ===
        sidebar.add(changeSuggestions);
        sidebar.add(importCsvButton);

//######################################################################################################################//

// === CONTENT PANEL ===
// Content panel (centro da tela)
        JPanel contentPanel = new JPanel(); // Usa o campo da classe
        contentPanel.setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        // === TOPO DO PAINEL COM TEXTO DINÂMICO ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        currentlyDisplayingLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        currentlyDisplayingLabel.setVisible(false);

        topPanel.add(currentlyDisplayingLabel);

        contentPanel.add(topPanel, BorderLayout.NORTH);

// Subpainel central dentro do contentPanel para conteúdo dinâmico (ex: logo ou grafo)
        contentPanelCenter = new JPanel();
        contentPanelCenter.setLayout(new FlowLayout(FlowLayout.CENTER)); // layout inicial
        contentPanel.add(contentPanelCenter, BorderLayout.CENTER);

// === LOGO CENTRAL ===
        JLabel centerLogo = new JLabel();
        logoIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Images/logo.png"))); // Carrega o logo

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

        toggleMergeSameOwnerProperties = new JCheckBox("Merge Same Owner Adjacent Properties");
        toggleMergeSameOwnerProperties.setSelected(false);
        toggleMergeSameOwnerProperties.setVisible(false);

// Estilo básico do checkbox para parecer moderno
        toggleMergeSameOwnerProperties.setForeground(Color.WHITE);
        toggleMergeSameOwnerProperties.setBackground(new Color(30, 30, 30));
        toggleMergeSameOwnerProperties.setFocusPainted(false);
        toggleMergeSameOwnerProperties.setFont(new Font("SansSerif", Font.BOLD, 12));
        toggleMergeSameOwnerProperties.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleMergeSameOwnerProperties.setOpaque(true);

        Dimension toggleMergeSameOwnerPropertiesSize = toggleMergeSameOwnerProperties.getPreferredSize();
        toggleMergeSameOwnerProperties.setPreferredSize(new Dimension(toggleMergeSameOwnerPropertiesSize.width, 30));
        bottomLeftPanel.add(toggleMergeSameOwnerProperties);

        toggleMergeSameOwnerProperties.addActionListener(e -> {
            mergeActive = toggleMergeSameOwnerProperties.isSelected();
            updateGraph(currentDisplayedProperties);
        });

// Right panel (for toggling info visibility)
        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

// Creating the toggle button
        JToggleButton toggleInfoToggle = new JToggleButton("Hide Info");
        toggleInfoToggle.setSelected(true);

// Customizing the toggle button appearance
        toggleInfoToggle.setForeground(Color.WHITE);
        toggleInfoToggle.setOpaque(true);
        toggleInfoToggle.setBackground(new Color(30, 30, 30));
        toggleInfoToggle.setBorderPainted(false);
        toggleInfoToggle.setFocusPainted(false);

// Setting the font and cursor style
        toggleInfoToggle.setFont(new Font("SansSerif", Font.BOLD, 12));
        toggleInfoToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));

// Removing content area fill and setting the UI style
        toggleInfoToggle.setUI(new BasicToggleButtonUI());
        toggleInfoToggle.setContentAreaFilled(false);
        toggleInfoToggle.setOpaque(true);

// Define a minimum width for the button to ensure it fits both "Hide Info" and "Show Info"
        toggleInfoToggle.setMinimumSize(new Dimension(120, 30)); // Set a minimum width
        toggleInfoToggle.setPreferredSize(new Dimension(120, 30)); // Set preferred size with sufficient width

// Dynamically adjusting the size based on the text
        toggleInfoToggle.addChangeListener(e -> {
            toggleInfoToggle.setBackground(new Color(30, 30, 30));
            toggleInfoToggle.revalidate(); // Revalidate to update the button size
            toggleInfoToggle.repaint();    // Repaint to apply the new size
        });

// Adding an action listener to toggle info panel visibility
        toggleInfoToggle.addActionListener(e -> {
            boolean isSelected = toggleInfoToggle.isSelected();
            graphInfoPanel.setVisible(isSelected);  // Show or hide the info panel
            toggleInfoToggle.setText(isSelected ? "Hide Info" : "Show Info");  // Update button text

            // Revalidate and repaint the button to adjust its size
            toggleInfoToggle.revalidate();
            toggleInfoToggle.repaint();

            // Revalidate and repaint the main frame
            MainFrame.this.revalidate();
            MainFrame.this.repaint();
        });

// Adding the button to the right panel
        bottomRightPanel.add(toggleInfoToggle);

// Junta os dois lados ao painel inferior
        bottomPanel.add(bottomLeftPanel, BorderLayout.WEST);
        bottomPanel.add(bottomRightPanel, BorderLayout.EAST);

// Adiciona o painel inferior ao contentPanel
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

//######################################################################################################################//

// === INFO PANEL ===
        graphInfoPanel = new JPanel();
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
    @CyclomaticComplexity(1)
    public void showSuccessDialog(String message) {
        JDialog dialog = new JDialog(this, "Sucesso", true);
        dialog.setSize(350, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Ícone verde de check
        JLabel iconLabel = new JLabel("✔"); // ✔
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
        okButton.setBackground(new Color(30, 30, 30));
        okButton.setForeground(Color.WHITE);
        okButton.setOpaque(true);
        okButton.setBorderPainted(false);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(okButton);

        dialog.add(iconLabel, BorderLayout.NORTH);
        dialog.add(messageLabel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    @CyclomaticComplexity(1)
    private void updateDistrictInfo(List<PropertyPolygon> propriedades) {
        int total = propriedades.size();
        double media = propriedades.stream()
                .mapToDouble(PropertyPolygon::getShapeArea) // assumes getArea() exists
                .average()
                .orElse(0.0);

        numPropsByDistrictLabel.setText("Amount of Properties: " + total);
        avgPropsByDistrictLabel.setText(String.format("Average Area: %.2f m²", media));
    }

    @CyclomaticComplexity(1)
    private void updateMunicipalityInfo(List<PropertyPolygon> propriedades) {
        int total = propriedades.size();
        double media = propriedades.stream()
                .mapToDouble(PropertyPolygon::getShapeArea) // assumes getArea() exists
                .average()
                .orElse(0.0);
        numPropsByMunicipalityLabel.setText("Amount of Properties: " + total);
        avgPropsByMunicipalityLabel.setText(String.format("Average Area: %.2f m²", media));
    }

    @CyclomaticComplexity(1)
    private void updateParishInfo(List<PropertyPolygon> propriedades) {
        int total = propriedades.size();
        double media = propriedades.stream()
                .mapToDouble(PropertyPolygon::getShapeArea) // assumes getArea() exists
                .average()
                .orElse(0.0);
        numPropsByParishLabel.setText("Amount of Properties: " + total);
        avgPropsByParishLabel.setText(String.format("Average Area: %.2f m²", media));
    }

    @CyclomaticComplexity(1)
    private void updateOwnerInfo(List<PropertyPolygon> propriedades) {
        int total = propriedades.size();
        double media = propriedades.stream()
                .mapToDouble(PropertyPolygon::getShapeArea) // assumes getArea() exists
                .average()
                .orElse(0.0);
        numPropsByOwnerLabel.setText("Amount of Properties: " + total);
        avgPropsByOwnerLabel.setText(String.format("Average Area: %.2f m²", media));
    }

    @CyclomaticComplexity(2)
    public void setDistrictTitle(String text) {
        if (districtTitle != null) districtTitle.setText(text);
    }

    @CyclomaticComplexity(2)
    public void setMunicipalityTitle(String text) {
        if (municipalityTitle != null) municipalityTitle.setText(text);
    }

    @CyclomaticComplexity(2)
    public void setParishTitle(String text) {
        if (parishTitle != null) parishTitle.setText(text);
    }

    @CyclomaticComplexity(2)
    public void setOwnerTitle(String text) {
        if (ownerTitle != null) ownerTitle.setText(text);
    }


    @CyclomaticComplexity(1)
    private void clearDistrictInfo() {
        districtTitle.setText("District - NA");
        numPropsByDistrictLabel.setText("Amount of Properties: - NA");
        avgPropsByDistrictLabel.setText("Average Area: - NA");
    }

    @CyclomaticComplexity(1)
    private void clearMunicipalityInfo() {
        municipalityTitle.setText("Municipality - NA");
        numPropsByMunicipalityLabel.setText("Amount of Properties: - NA");
        avgPropsByMunicipalityLabel.setText("Average Area: - NA");
    }

    @CyclomaticComplexity(1)
    private void clearParishInfo() {
        parishTitle.setText("Parish - NA");
        numPropsByParishLabel.setText("Amount of Properties: - NA");
        avgPropsByParishLabel.setText("Average Area: - NA");
    }

    @CyclomaticComplexity(1)
    private void clearOwnerInfo() {
        ownerTitle.setText("Owner - NA");
        numPropsByOwnerLabel.setText("Amount of Properties: - NA");
        avgPropsByOwnerLabel.setText("Average Area: - NA");
    }

    @CyclomaticComplexity(8)
    public void updateGraph(List<PropertyPolygon> propriedades) {
        currentDisplayedProperties = propriedades;

        List<PropertyPolygon> toDisplay = mergeActive
                ? PropertyMerger.mergeOwnerAdjacentProperties(propriedades)
                : propriedades;

        jungGraph = PropertyGraphJungBuilder.buildGraph(toDisplay);
        graphPanel = GraphViewer.createGraphPanel(jungGraph, 1024, 1024, showOwnerIds);

        if (activeFilterType != null) {
            switch (activeFilterType) {
                case "Owner":
                    updateOwnerInfo(toDisplay);
                    break;
                case "District":
                    updateDistrictInfo(toDisplay);
                    break;
                case "Municipality":
                    updateMunicipalityInfo(toDisplay);
                    break;
                case "Parish":
                    updateParishInfo(toDisplay);
                    break;
                default:
                    break;
            }
        }

        SwingUtilities.invokeLater(() -> {
            contentPanelCenter.removeAll();
            contentPanelCenter.setLayout(new BorderLayout());
            contentPanelCenter.add(graphPanel, BorderLayout.CENTER);
            contentPanelCenter.revalidate();
            contentPanelCenter.repaint();
        });
    }

    @CyclomaticComplexity(1)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}