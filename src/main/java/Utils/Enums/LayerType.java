package Utils.Enums;

/**
 * The {@code LayerType} enum represents the different layers of an application.
 * It is used to categorize components into either the back-end or front-end layer.
 * Each enum constant provides a custom string representation.
 */
public enum LayerType {
    BACK_END {
        @Override
        public String toString() {
            return "Back End";
        }
    },
    FRONT_END {
        @Override
        public String toString() {
            return "Front End";
        }
    },
}