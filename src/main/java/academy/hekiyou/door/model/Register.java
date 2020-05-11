package academy.hekiyou.door.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface Register {
    
    /**
     * Registers a {@link Command} into the register
     *
     * @param command The {@link Command} to register
     */
    void register(@NotNull Command command);
    
    /**
     * Checks if a command is registered or not
     *
     * @param commandName The command name to check
     *
     * @return {@code true} if {@code commandName} is registered, {@code false} otherwise
     */
    default boolean isRegistered(@NotNull String commandName){
        return getCommand(commandName) != null;
    }
    
    /**
     * @implSpec Changes to the map are not reflected.
     * @return A {@link Map} whose entries consist of the command name and class owning said command.
     */
    @NotNull Map<String, String> getRegistered();
    
    /**
     * Fetches a {@link Command} from the register
     *
     * @param commandName The name of the command to get
     *
     * @return A {@link Command} instance, or {@code null} if none was found
     */
    @Nullable Command getCommand(@NotNull String commandName);
    
    /**
     * Unregisters the {@link Command} given
     *
     * @param command The {@link Command} to unregister
     */
    void unregister(@NotNull Command command);
    
}
