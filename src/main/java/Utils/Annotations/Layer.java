package Utils.Annotations;

import java.lang.annotation.*;
import Utils.Enums.LayerType;
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Layer {
    LayerType value();
}
