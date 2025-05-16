package Annotations;

import java.lang.annotation.*;
import Enums.LayerType;
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Layer {
    LayerType value();
}
