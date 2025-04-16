package util;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME) // necessary to use reflection during runtime to retrieve value
@Target(ElementType.PARAMETER)
public @interface DefaultParameter {
    String value(); 
}
