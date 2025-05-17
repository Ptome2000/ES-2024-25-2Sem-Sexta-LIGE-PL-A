package Models;

import static org.junit.jupiter.api.Assertions.*;

import Utils.Mocks.MockedPropertyPolygon;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * This class contains unit tests for the {@link ExchangeSuggestion} class.
 * It validates the behavior of the ExchangeSuggestion class, including
 * its ability to store property IDs, feasibility, and calculate percentage changes.
 * Each test case is designed to cover specific functionality
 * and edge cases, ensuring the robustness of the ExchangeSuggestion implementation.
 *
 * <p><strong>Author:</strong> Ptome2000</p>
 * <p><strong>Date:</strong> 17/05/2025</p>
 *
 * <p><strong>Cyclomatic Complexity:</strong></p>
 * <ul>
 *     <li>Constructor: 1</li>
 *     <li>getPropertyFromA: 1</li>
 *     <li>getPropertyFromB: 1</li>
 *     <li>getPercentChangeA: 1</li>
 *     <li>getPercentChangeB: 1</li>
 *     <li>getFeasibility: 1</li>
 *     <li>getScore: 1</li>
 *     <li>setPercentChangeA: 1</li>
 *     <li>setPercentChangeB: 1</li>
 *     <li>setScore: 1</li>
 *     <li>toString: 1</li>
 *     <li>formatPercentage: 2</li>
 * </ul>
 */
@Feature("Object Models")
class ExchangeSuggestionTests {

    @Test
    @DisplayName("Constructor initializes ExchangeSuggestion with correct values")
    @Description("Ensures that the ExchangeSuggestion constructor sets the property IDs and feasibility correctly.")
    @Severity(SeverityLevel.NORMAL)
    void constructor() {
        ExchangeSuggestion suggestion = new ExchangeSuggestion(1, 2, 0.85);

        assertEquals(1, suggestion.getPropertyFromA(), "PropertyFromA should be initialized correctly.");
        assertEquals(2, suggestion.getPropertyFromB(), "PropertyFromB should be initialized correctly.");
        assertEquals(0.85, suggestion.getAreafeasibility(), "Feasibility should be initialized correctly.");
    }

    @Test
    @DisplayName("setPercentChangeA and getPercentChangeA work correctly")
    @Description("Ensures that the setPercentChangeA and getPercentChangeA methods work as expected.")
    @Severity(SeverityLevel.NORMAL)
    void percentChangeA() {
        ExchangeSuggestion suggestion = new ExchangeSuggestion(1, 2, 0.85);
        suggestion.setPercentChangeA(0.15);

        assertEquals(0.15, suggestion.getPercentChangeA(), "PercentChangeA should be set and retrieved correctly.");
    }

    @Test
    @DisplayName("setPercentChangeB and getPercentChangeB work correctly")
    @Description("Ensures that the setPercentChangeB and getPercentChangeB methods work as expected.")
    @Severity(SeverityLevel.NORMAL)
    void percentChangeB() {
        ExchangeSuggestion suggestion = new ExchangeSuggestion(1, 2, 0.85);
        suggestion.setPercentChangeB(0.25);

        assertEquals(0.25, suggestion.getPercentChangeB(), "PercentChangeB should be set and retrieved correctly.");
    }

    @Test
    @DisplayName("setScore and getScore work correctly")
    @Description("Ensures that the setScore and getScore methods work as expected.")
    @Severity(SeverityLevel.NORMAL)
    void score() {
        ExchangeSuggestion suggestion = new ExchangeSuggestion(1, 2, 0.85);
        suggestion.setScore(95.5);

        assertEquals(95.5, suggestion.getScore(), "Score should be set and retrieved correctly.");
    }

    @Test
    @DisplayName("toString includes value similarity in the output")
    @Description("Ensures that the toString method includes the value similarity in the formatted string.")
    @Severity(SeverityLevel.MINOR)
    void toStringIncludesValueSimilarity() {
        ExchangeSuggestion suggestion = new ExchangeSuggestion(1, 2, 0.85);
        suggestion.setPercentChangeA(0.15);
        suggestion.setPercentChangeB(0.25);
        suggestion.setScore(95.5);
        suggestion.setValueSimilarity(0.75);

        String expected = "\nSugestão: [Terreno A: 1] ↔ [Terreno B: 2] | Viabilidade: 0.85 | % A: 15.00% | % B: 25.00% | Score: 95.50 | Value Similarity: 0.75";
        assertEquals(expected, suggestion.toString(), "toString should include value similarity in the output.");
    }

    @Test
    @DisplayName("formatPercentage formats values correctly")
    @Description("Ensures that the formatPercentage method formats percentage values as expected.")
    @Severity(SeverityLevel.NORMAL)
    void formatPercentage() {
        ExchangeSuggestion suggestion = new ExchangeSuggestion(1, 2, 0.85);

        assertEquals("50.00%", suggestion.formatPercentage(0.5), "Should format 0.5 as 50.00%");
        assertEquals("0.00%", suggestion.formatPercentage(0.0), "Should format 0.0 as 0.00%");
        assertEquals("N/A", suggestion.formatPercentage(Double.NaN), "Should return N/A for NaN values");
        assertEquals("-25.00%", suggestion.formatPercentage(-0.25), "Should format -0.25 as -25.00%");
    }

    @Test
    @DisplayName("computeValueSimilarity calculates value similarity correctly")
    @Description("Ensures that the computeValueSimilarity method calculates the value similarity based on urbanization and tourism scores.")
    @Severity(SeverityLevel.NORMAL)
    void computeValueSimilarity() {
        PropertyPolygon propertyA = new MockedPropertyPolygon(1, "OwnerA", new Polygon(List.of(new VertexCoordinate(0.8, 0.6))));
        PropertyPolygon propertyB = new MockedPropertyPolygon(2, "OwnerB", new Polygon(List.of(new VertexCoordinate(0.7, 0.5))));

        ExchangeSuggestion suggestion = new ExchangeSuggestion(1, 2, 0.85);
        suggestion.computeValueSimilarity(propertyA, propertyB);

        double expectedSimilarity = 0.6 * (1 - Math.abs(0.8 - 0.7)) + 0.4 * (1 - Math.abs(0.6 - 0.5));
        assertEquals(expectedSimilarity, suggestion.getValueSimilarity(), 0.0001, "Value similarity should be calculated correctly.");
    }

}