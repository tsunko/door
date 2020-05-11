package academy.hekiyou.door.annotations.optional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates the parameter in a method is an optional unknown type.
 * If the parameter cannot be interpreted using the class derived from the parameter information,
 * then the passed parameter is {@code null}.
 * <p>
 * @see academy.hekiyou.door.interp.Interpreter
 */
@Optional
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface OptionalObject {
    
    String value();
    
}
