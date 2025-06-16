package util;

import java.lang.annotation.*;

/**
 * Annotation to specify a default value for a method parameter.
 * <p>
 * This annotation can be used to indicate that a particular parameter
 * should assume a default value, if necessary.
 * </p>
 * 
 * @author Chiara E. Zarrella
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface DefaultParameter {
	/**
     * The default value to be used if the parameter is not supplied.
     *
     * @return the default value as a string
     */
    String value(); 
}
