package DetectAdjacentProperties;

public class AdjacentPropertyPair {
    private final int propertyId1;
    private final int propertyId2;

    public AdjacentPropertyPair(int propertyId1, int propertyId2) {
        this.propertyId1 = propertyId1;
        this.propertyId2 = propertyId2;
    }

    public int getPropertyId1() {
        return propertyId1;
    }

    public int getPropertyId2() {
        return propertyId2;
    }

    @Override
    public String toString() {
        return "AdjacentPropertyPair{" +
                "propertyId1=" + propertyId1 +
                ", propertyId2=" + propertyId2 +
                '}';
    }
}
