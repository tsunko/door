package academy.hekiyou.door.model;

import academy.hekiyou.door.exception.BadCastException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Represents an object (which can be a user, or some autonomous system) that is capable of executing commands.
 */
public interface Invoker extends Identifiable, MessageReceiver {
    
    /**
     * Check if the invoker has a given permission node.
     *
     * @param permission the permission node to check for
     *
     * @return {@code true} if the invoker has the requested permission,
     * {@code false} otherwise
     */
    boolean hasPermission(@NotNull String permission);
    
    /**
     * Return a raw representation of this invoker, downcasted to {@link Object}
     * @return An {@link Object} representing the Invoker.
     * @implNote Classes that wrap around another and implement Invoker there can override this as a means of accessing
     *           the underlying, wrapped value.
     */
    default @Nullable Object raw(){
        return this;
    }
    
    /**
     * Attempts to cast this or its underlying object to type {@code T}, using {@code T}'s class.
     * @see Invoker#raw()
     * @param castTo The {@link Class} of {@code T}
     * @param <T> The type to cast to
     * @return This object (or whatever {@link Invoker#raw()} returns) casted to {@code T} or {@code null} if casting
     *         is not possible or if {@link Invoker#raw()} returned null
     */
    default @Nullable <T> T asNullable(Class<T> castTo){
        Object raw = raw();
        if(raw == null)
            return null;
        return castTo.isAssignableFrom(raw.getClass()) ? castTo.cast(raw()) : null;
    }
    
    default @NotNull <T> T as(Class<T> castTo){
        return Optional.ofNullable(asNullable(castTo)).orElseThrow(() -> new BadCastException(castTo));
    }
    
}
