package UserInterface;

import DetectAdjacentProperties.AdjacentPropertyPair;
import Services.SuggestionGenerator;
import Models.ExchangeSuggestion;
import Models.PropertyPolygon;
import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The {@code ChangeSuggestionsFrame} class represents a JFrame that displays suggested changes
 * for property exchanges based on adjacent properties.
 * It provides a user interface to visualize the suggestions.
 */
@Layer(LayerType.FRONT_END)
public class ChangeSuggestionsFrame extends JFrame {

    /**
     * Constructor to initialize the ChangeSuggestionsFrame.
     *
     * @param displayedProperties The list of properties to be displayed.
     * @param adjacentPairs      The list of adjacent property pairs.
     * @param location           The location for which suggestions are generated.
     */
    public ChangeSuggestionsFrame(List<PropertyPolygon> displayedProperties, List<AdjacentPropertyPair> adjacentPairs, String location) {
        setTitle("Suggested changes to: " + location);
        setSize(500, 600);
        setLocationRelativeTo(null); // centra a janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // fecha só esta janela

        List<ExchangeSuggestion> suggestions = SuggestionGenerator.generateSuggestions(adjacentPairs, displayedProperties);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.LIGHT_GRAY);

        if (suggestions.isEmpty()) {
            JLabel noSuggestionsLabel = new JLabel("No suggestions available.");
            noSuggestionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(noSuggestionsLabel);

        } else {
            for (ExchangeSuggestion sugestao : suggestions) {
                JPanel sugestaoPanel = createSuggestionPanel(sugestao);
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

        for (ExchangeSuggestion sugestao : suggestions) {
            JPanel sugestaoPanel = createSuggestionPanel(sugestao);
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

    /**
     * Creates a panel to display the details of a suggestion.
     *
     * @param sugestao The suggestion to be displayed.
     * @return A JPanel containing the suggestion details.
     */
    @CyclomaticComplexity(1)
    private JPanel createSuggestionPanel(ExchangeSuggestion sugestao) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel("📌 Suggestion:"));
        panel.add(new JLabel("• Terrain A: " + sugestao.getPropertyFromA()));
        panel.add(new JLabel("• Terrain B: " + sugestao.getPropertyFromB()));
        panel.add(new JLabel("• Area Feasibility: " + format2Decimals(sugestao.getAreafeasibility())));
        panel.add(new JLabel("• % A: " + formatPercentage(sugestao.getPercentChangeA())));
        panel.add(new JLabel("• % B: " + formatPercentage(sugestao.getPercentChangeB())));
        panel.add(new JLabel("• Value Feasibility: " + format2Decimals(sugestao.getValueSimilarity())));
        panel.add(new JLabel("• Feasibility: " + format2Decimals(sugestao.getFeability())));
        panel.add(new JLabel("• Score: " + format2Decimals(sugestao.getScore())));

        return panel;
    }

    /**
     * Formats a double value as a percentage string with two decimal places.
     *
     * @param value The double value to format.
     * @return A string representing the formatted percentage.
     */
    @CyclomaticComplexity(2)
    private String formatPercentage(double value) {
        if (Double.isNaN(value)) return "N/A";
        return String.format("%.2f%%", value * 100);
    }

    /**
     * Formats a double value to two decimal places.
     *
     * @param value The double value to format.
     * @return A string representing the formatted value.
     */
    @CyclomaticComplexity(1)
    private String format2Decimals(double value) {
        return String.format("%.2f", value);
    }
}