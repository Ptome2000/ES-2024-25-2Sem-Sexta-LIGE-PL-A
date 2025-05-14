package Models;

/**
 * This class represents a suggestion for exchanging properties.
 * It contains two properties that are suggested for exchange.
 */
public class ExchangeSuggestion {
    private final int propertyFromA;
    private final int propertyFromB;
    private final double feasibility;
    private double score;


    private double percentChangeA;
    private double percentChangeB;

    public ExchangeSuggestion(int propertyFromA, int propertyFromB, double feasibility) {
        this.propertyFromA = propertyFromA;
        this.propertyFromB = propertyFromB;
        this.feasibility = feasibility;
    }

    public int getPropertyFromA() { return propertyFromA; }
    public int getPropertyFromB() { return propertyFromB; }
    public double getFeasibility() { return feasibility; }

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



    @Override
    public String toString() {
        return "\nSugestão: [Terreno A: " + propertyFromA + "] ↔ [Terreno B: " + propertyFromB + "]" +
                " | Viabilidade: " + String.format("%.2f", feasibility) +
                " | % A: " + formatPercentage(percentChangeA) +
                " | % B: " + formatPercentage(percentChangeB) +
                " | Score: " + String.format("%.2f", score);
    }

    private String formatPercentage(double value) {
        if (Double.isNaN(value)) return "N/A";
        return String.format("%.2f%%", value * 100);
    }
}

