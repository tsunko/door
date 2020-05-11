package academy.hekiyou.door.model;

import org.jetbrains.annotations.NotNull;

public interface Executor {
    
    /**
     * Finds the appropriate command (if any) and executes the it with the given Invoker and flow (assuming no channel)
     * @param commandName The command to invoke
     * @param invoker The invoker for the command
     * @param args The arguments to pass
     * @return {@code true} if the command was found and (was attempted to be) executed,
     *         {@code false} otherwise
     */
    default boolean findAndExecute(@NotNull String commandName, @NotNull Invoker invoker, @NotNull String[] args){
        return findAndExecute(commandName, invoker, Channel.NULL_CHANNEL, args);
    }
    
    /**
     * Finds the appropriate command (if any) and executes the it with the given Invoker and flow.
     * @param commandName The {@link Command} to invoke
     * @param invoker The {@link Invoker} for the command
     * @param chan The {@link Channel} the command was executed in
     * @param args The arguments to pass
     * @return {@code true} if the command was found and (was attempted to be) executed,
     *         {@code false} otherwise
     */
    boolean findAndExecute(@NotNull String commandName, @NotNull Invoker invoker,
                           @NotNull Channel chan, @NotNull String[] args);
    
}
