package academy.hekiyou.door.house;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a system that can load classes annotated with {@link academy.hekiyou.door.annotations.Module}
 */
public interface ModuleLoader {
    
    /**
     * Loads a class and then returns an instance of it. This will also automatically register any commands defined.
     *
     * @param klass the class to load
     * @param <T>   The type of the loading class
     *
     * @return An instance of the class
     */
    <T> @NotNull T load(@NotNull Class<T> klass);
    
    /**
     * Unloads a class, unregistering all of its commands.
     *
     * @param klass The class to unload
     *
     * @return A list (possibly empty) containing all commands unregistered.
     */
    @NotNull List<String> unload(@NotNull Class<?> klass);
    
}
