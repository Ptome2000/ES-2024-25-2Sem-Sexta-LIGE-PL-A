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
        setSize(400, 300);
        setLocationRelativeTo(null); // centra a janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // fecha só esta janela

        List<ExchangeSuggestion> suggestions = SuggestionGenerator.generateSuggestions(adjacentPairs, displayedProperties);
        JTextArea area = new JTextArea(String.join("\n", suggestions.toString()));
        add(new JScrollPane(area), BorderLayout.CENTER);
    }
}