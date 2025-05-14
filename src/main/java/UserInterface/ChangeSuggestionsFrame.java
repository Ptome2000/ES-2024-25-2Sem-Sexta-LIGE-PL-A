package UserInterface;

import DetectAdjacentProperties.AdjacentPropertyPair;
import ExchangeSuggestionEngine.SuggestionGenerator;
import Models.ExchangeSuggestion;
import Models.PropertyPolygon;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChangeSuggestionsFrame extends JFrame {

    public ChangeSuggestionsFrame(List<PropertyPolygon> displayedProperties, List<AdjacentPropertyPair> adjacentPairs, String location) {
        setTitle(location);
        setSize(500, 600);
        setLocationRelativeTo(null); // centra a janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // fecha só esta janela

        List<ExchangeSuggestion> suggestions = SuggestionGenerator.generateSuggestions(adjacentPairs, displayedProperties);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.LIGHT_GRAY);

        for (ExchangeSuggestion sugestao : suggestions) {
            JPanel sugestaoPanel = criarSugestaoPanel(sugestao);
            sugestaoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            sugestaoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, sugestaoPanel.getPreferredSize().height));
            sugestaoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            mainPanel.add(sugestaoPanel);
            mainPanel.add(Box.createVerticalStrut(10)); // espaço entre sugestões
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel criarSugestaoPanel(ExchangeSuggestion sugestao) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel("📌 Suggestion:"));
        panel.add(new JLabel("• Terrain A: " + sugestao.getPropertyFromA()));
        panel.add(new JLabel("• Terrain B: " + sugestao.getPropertyFromB()));
        panel.add(new JLabel("• Feasibility: " + format2Decimals(sugestao.getFeasibility())));
        panel.add(new JLabel("• % A: " + formatPercentage(sugestao.getPercentChangeA())));
        panel.add(new JLabel("• % B: " + formatPercentage(sugestao.getPercentChangeB())));
        panel.add(new JLabel("• Score: " + format2Decimals(sugestao.getScore())));

        return panel;
    }
    private String formatPercentage(double value) {
        if (Double.isNaN(value)) return "N/A";
        return String.format("%.2f%%", value * 100);
    }

    private String format2Decimals(double value) {
        return String.format("%.2f", value);
    }
}