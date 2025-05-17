package Models;

/**
 * This class represents a suggestion for exchanging properties.
 * It contains two properties that are suggested for exchange.
 */
public class ExchangeSuggestion {
    private final int propertyFromA;
    private final int propertyFromB;
    private final double areafeasibility;
    private double valueSimilarity;
    private double score;


    private double percentChangeA;
    private double percentChangeB;

    public ExchangeSuggestion(int propertyFromA, int propertyFromB, double areafeasibility) {
        this.propertyFromA = propertyFromA;
        this.propertyFromB = propertyFromB;
        this.areafeasibility = areafeasibility;
    }

    public int getPropertyFromA() { return propertyFromA; }
    public int getPropertyFromB() { return propertyFromB; }
    public double getAreafeasibility() { return areafeasibility; }

    public double getPercentChangeA() { return percentChangeA; }
    public double getPercentChangeB() { return percentChangeB; }

    public void setPercentChangeA(double percentChangeA) {
        this.percentChangeA = percentChangeA;
    }
    public void setPercentChangeB(double percentChangeB) {
        this.percentChangeB = percentChangeB;
    }

    public void setScore(double score) {
        this.score = score;
    }
    public double getScore() {
        return score;
    }

    public double getValueSimilarity() { return valueSimilarity; }
    public void setValueSimilarity(double valueSimilarity) { this.valueSimilarity = valueSimilarity;}




    @Override
    public String toString() {
        return "\nSugestão: [Terreno A: " + propertyFromA + "] ↔ [Terreno B: " + propertyFromB + "]" +
                " | Viabilidade: " + String.format("%.2f", areafeasibility) +
                " | % A: " + formatPercentage(percentChangeA) +
                " | % B: " + formatPercentage(percentChangeB) +
                " | Score: " + String.format("%.2f", score);
    }

    private String formatPercentage(double value) {
        if (Double.isNaN(value)) return "N/A";
        return String.format("%.2f%%", value * 100);
    }

    public void computeValueSimilarity(PropertyPolygon a, PropertyPolygon b) {
        double urbanDiff = Math.abs(a.getUrbanizationScore() - b.getUrbanizationScore());
        double tourismDiff = Math.abs(a.getTourismScore() - b.getTourismScore());
        this.valueSimilarity = 0.6 * (1 - urbanDiff) + 0.4 * (1 - tourismDiff);
        //TODO tirar depois de testar
        System.out.println("Value Similarity: " + this.valueSimilarity);
    }
}

