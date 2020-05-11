package academy.hekiyou.door.model;

import academy.hekiyou.door.annotations.RegisterCommand;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Parameter;

/**
 * Represents a command that can be executed by a user.
 */
public interface Command {
    
    /**
     * Attempts to execute the function this command points to, given environmental context (i.e {@link Invoker})
     *
     * @param commandName The current name of the command being executed (can be an alias)
     * @param invoker     A {@link Invoker} representing who is executing the command
     * @param channel     A {@link Channel} representing the channel this being
     * @param arguments   A {@link String} array containing all arguments to execute with
     */
    void execute(@NotNull String commandName, @NotNull Invoker invoker,
                 @NotNull Channel channel, @NotNull String[] arguments);
    
    /**
     * Return the primary name for this command
     *
     * @return The primary name for this command
     */
    @NotNull String getName();
    
    /**
     * Return the metadata associated with this command
     *
     * @return The associated metadata
     */
    @NotNull RegisterCommand getMetadata();
    
    /**
     * Return the name of the class that owns this command
     *
     * @return The class name of this command's owner
     */
    @NotNull String getOwningClass();
    
    /**
     * Return a {@link Parameter} array this {@link Command} takes
     *
     * @return A {@link Parameter} array
     */
    @NotNull Parameter[] getParameters();
    
    /**
     * Return a {@link String} array representing the parameter names that
     * would come from {@link Command#getParameters()}.
     *
     * @return A {@link String} array
     */
    @NotNull String[] getUsage();
    
}
