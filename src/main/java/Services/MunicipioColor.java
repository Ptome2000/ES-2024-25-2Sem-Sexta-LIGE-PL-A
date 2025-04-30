package Services;

import java.awt.*;

public enum MunicipioColor {
    PASTEL_GRAY(new Color(220, 220, 220)),
    PASTEL_RED(new Color(255, 179, 186)),
    PASTEL_ORANGE(new Color(255, 223, 186)),
    PASTEL_YELLOW(new Color(255, 255, 186)),
    PASTEL_GREEN(new Color(186, 255, 201)),
    PASTEL_TEAL(new Color(186, 255, 255)),
    PASTEL_BLUE(new Color(186, 225, 255)),
    PASTEL_PURPLE(new Color(209, 186, 255)),
    PASTEL_PINK(new Color(255, 186, 241)),
    PASTEL_BROWN(new Color(210, 180, 140));

    private final Color color;

    MunicipioColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}