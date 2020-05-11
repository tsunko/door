package academy.hekiyou.door.annotations.optional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents that the annotation given is one of the OptionalX types (i.e {@link OptionalByte})
 * <p>
 * Since annotations are locked, in terms of inheritance, we just use another annotation and inspect at runtime.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Optional {

}
