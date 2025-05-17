package Utils.Enums;

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