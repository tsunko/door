package academy.hekiyou.door.interp;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a functional interface (or object) capable of taking a {@link String} input and mapping/transforming it
 * into a desired object type.
 *
 * @param <T> the type to convert a {@link String} into
 */
@FunctionalInterface
public interface Interpreter<T> {
    
    /**
     * Applies the interpreter to the given input.
     *
     * @param input the input to attempt to interpret
     *
     * @return The interpreted object, if possible.
     */
    @NotNull T apply(@NotNull String input);
    
}
