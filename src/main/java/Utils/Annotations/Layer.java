package Utils.Annotations;

import java.lang.annotation.*;
import Utils.Enums.LayerType;

/**
 * The {@code Layer} annotation is used to specify the layer type of a class.
 * It can be used for documentation and analysis purposes.
 * <p>
 * This annotation helps in categorizing classes into different layers of an application,
 * such as back-end or front-end.
 * </p>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Layer {
    LayerType value();
}
