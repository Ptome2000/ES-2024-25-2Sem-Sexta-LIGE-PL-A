package Utils.Annotations;

import java.lang.annotation.*;

/**
 * The {@code CyclomaticComplexity} annotation is used to specify the cyclomatic complexity of a method.
 * It can be used for documentation and analysis purposes.
 * <p>
 * Cyclomatic complexity is a software metric used to measure the complexity of a program.
 * It is calculated based on the control flow graph of the program, where each node represents a block of code
 * and each edge represents a control flow path.
 * </p>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CyclomaticComplexity {
    int value();
}