package academy.hekiyou.door.annotations.optional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates the parameter in a method is an optional {@link Double}.
 * If the parameter cannot be fulfilled, the default value (as specified by {@link OptionalDouble#value()})
 * will be used to fill it in.
 */
@Optional
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface OptionalDouble {
    
    double value();
    
}
