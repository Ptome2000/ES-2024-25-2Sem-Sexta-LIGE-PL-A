package Models;

import Utils.Annotations.CyclomaticComplexity;
import Utils.Annotations.Layer;
import Utils.Enums.LayerType;

/**
 * The {@code ExchangeSuggestion} class represents a suggestion for an exchange between two properties.
 * It contains information about the properties involved, their area feasibility, and various scores.
 */
@Layer(LayerType.BACK_END)
public class ExchangeSuggestion {
    private final int propertyFromA;
    private final int propertyFromB;
    private final double areafeasibility;
    private double valueSimilarity;
    private double feability;
    private double score;

    private double percentChangeA;
    private double percentChangeB;

    /**
     * Constructor to initialize the ExchangeSuggestion object.
     *
     * @param propertyFromA      The ID of the property from A.
     * @param propertyFromB      The ID of the property from B.
     * @param areafeasibility    The area feasibility score for the exchange.
     */
    public ExchangeSuggestion(int propertyFromA, int propertyFromB, double areafeasibility) {
        this.propertyFromA = propertyFromA;
        this.propertyFromB = propertyFromB;
        this.areafeasibility = areafeasibility;
    }


    /**
     * Gets the ID of the property from A.
     *
     * @return The ID of the property from A.
     */
    @CyclomaticComplexity(1)
    public int getPropertyFromA() { return propertyFromA; }

    /**
     * Gets the ID of the property from B.
     *
     * @return The ID of the property from B.
     */
    @CyclomaticComplexity(1)
    public int getPropertyFromB() { return propertyFromB; }

    /**
     * Gets the area feasibility score for the exchange.
     *
     * @return The area feasibility score.
     */
    @CyclomaticComplexity(1)
    public double getAreafeasibility() { return areafeasibility; }

    /**
     * Gets the percentage change for property A.
     *
     * @return The percentage change for property A.
     */
    @CyclomaticComplexity(1)
    public double getPercentChangeA() { return percentChangeA; }

    /**
     * Gets the percentage change for property B.
     *
     * @return The percentage change for property B.
     */
    @CyclomaticComplexity(1)
    public double getPercentChangeB() { return percentChangeB; }

    /**
     * Sets the percentage change for property A.
     *
     * @param percentChangeA The percentage change for property A.
     */
    @CyclomaticComplexity(1)
    public void setPercentChangeA(double percentChangeA) {
        this.percentChangeA = percentChangeA;
    }

    /**
     * Sets the percentage change for property B.
     *
     * @param percentChangeB The percentage change for property B.
     */
    @CyclomaticComplexity(1)
    public void setPercentChangeB(double percentChangeB) {
        this.percentChangeB = percentChangeB;
    }

    /**
     * Sets the score for the exchange suggestion.
     *
     * @param score The score for the exchange suggestion.
     */
    @CyclomaticComplexity(1)
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Gets the score for the exchange suggestion.
     *
     * @return The score for the exchange suggestion.
     */
    @CyclomaticComplexity(1)
    public double getScore() {
        return score;
    }

    /**
     * Gets the value similarity score for the exchange suggestion.
     *
     * @return The value similarity score.
     */
    @CyclomaticComplexity(1)
    public double getValueSimilarity() { return valueSimilarity; }

    /**
     * Sets the value similarity score for the exchange suggestion.
     *
     * @param valueSimilarity The value similarity score.
     */
    @CyclomaticComplexity(1)
    public void setValueSimilarity(double valueSimilarity) { this.valueSimilarity = valueSimilarity;}

    public double getFeability() { return feability; }
    public void setFeability(double feability) { this.feability = feability; }

    /**
     * Returns a string representation of the ExchangeSuggestion object.
     *
     * @return A string representation of the ExchangeSuggestion.
     */
    @CyclomaticComplexity(1)
    @Override
    public String toString() {
        return "\nSugestão: [Terreno A: " + propertyFromA + "] ↔ [Terreno B: " + propertyFromB + "]" +
                " | Viabilidade: " + String.format("%.2f", areafeasibility) +
                " | % A: " + formatPercentage(percentChangeA) +
                " | % B: " + formatPercentage(percentChangeB) +
                " | Score: " + String.format("%.2f", score);
    }

    /**
     * Formats a double value as a percentage string.
     *
     * @param value The double value to format.
     * @return A string representation of the percentage.
     */
    @CyclomaticComplexity(2)
    String formatPercentage(double value) {
        if (Double.isNaN(value)) return "N/A";
        return String.format("%.2f%%", value * 100);
    }

    /**
     * Computes the value similarity between two PropertyPolygon objects based on their urbanization and tourism scores.
     *
     * @param a The first PropertyPolygon object.
     * @param b The second PropertyPolygon object.
     */
    @CyclomaticComplexity(1)
    public void computeValueSimilarity(PropertyPolygon a, PropertyPolygon b) {
        double urbanDiff = Math.abs(a.getUrbanizationScore() - b.getUrbanizationScore());
        double tourismDiff = Math.abs(a.getTourismScore() - b.getTourismScore());
        this.valueSimilarity = 0.6 * (1 - urbanDiff) + 0.4 * (1 - tourismDiff);
    }
}

