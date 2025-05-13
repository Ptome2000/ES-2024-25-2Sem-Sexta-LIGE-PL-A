package Models;

/**
 * This class represents a suggestion for exchanging properties.
 * It contains two properties that are suggested for exchange.
 */
public class ExchangeSuggestion {
    private final int propertyFromA;
    private final int propertyFromB;

    public ExchangeSuggestion(int propertyFromA, int propertyFromB) {
        this.propertyFromA = propertyFromA;
        this.propertyFromB = propertyFromB;
    }

    public int getPropertyFromA() { return propertyFromA; }
    public int getPropertyFromB() { return propertyFromB; }

    @Override
    public String toString() {
        return "Sugestão: Trocar terreno " + propertyFromA + " ↔ " + propertyFromB;
    }
}

