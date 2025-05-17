package Models;

import Utils.Annotations.Layer;
import Utils.Enums.LayerType;

/**
 * The {@code Region} interface represents a geographical region with a name.
 * It is implemented by various classes representing different levels of regions,
 * such as districts, municipalities, and parishes.
 */
@Layer(LayerType.BACK_END)
public interface Region {
    String name();
}
