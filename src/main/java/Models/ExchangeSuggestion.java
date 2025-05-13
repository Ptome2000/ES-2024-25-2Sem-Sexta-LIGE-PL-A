package Models;

/**
 * This class represents a suggestion for exchanging properties.
 * It contains two properties that are suggested for exchange.
 */
public class ExchangeSuggestion {
    private final int propertyFromA;
    private final int propertyFromB;
    private final double feasibility;


    public ExchangeSuggestion(int propertyFromA, int propertyFromB, double feasibility) {
        this.propertyFromA = propertyFromA;
        this.propertyFromB = propertyFromB;
        this.feasibility = feasibility;
    }

    public int getPropertyFromA() { return propertyFromA; }
    public int getPropertyFromB() { return propertyFromB; }
    public double getFeasibility() { return feasibility; }

    @Override
    public String toString() {
        return "Sugestão: Trocar terreno " + propertyFromA + " ↔ " + propertyFromB;
    }
}

