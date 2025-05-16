package Annotations;

import java.lang.annotation.*;
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CyclomaticComplexity {
    int value();
}