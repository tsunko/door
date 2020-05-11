package academy.hekiyou.door.model;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that is identifiable by, at least, a unique ID. Optionally, the implementing class can choose
 * to also override the {@link Identifiable#getName()}, which are assumed to be non-unique.
 * <p>
 * While the ID itself is not used within door (more-so to provide a means of identifying {@link Invoker}s),
 * it is still suggested to provide a unique ID.
 */
public interface Identifiable {
    
    /**
     * Return the name of the identifiable object.
     *
     * @return A {@link String} representing the name of the object
     *
     * @implSpec The name does not have to be a unique ID.
     * @implSpec The default value is an empty string ("").
     */
    default @NotNull String getName(){
        return "";
    }
    
    /**
     * Return an ID for the identifiable object.
     *
     * @return A {@link String} representation of a ID for the object
     *
     * @implSpec The ID is <i>assumed</i> to be unique.
     */
    @NotNull String getID();
    
}
