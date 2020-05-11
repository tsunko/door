package academy.hekiyou.door.model;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Represents the idea of a chat channel.
 * <p>
 * A chat channel, on a basic level, is a container of {@link Invoker}s who can receive messages as if the messages
 * were broadcasted. If no such feature is required, {@link Channel#NULL_CHANNEL} can be used.
 */
public interface Channel extends Identifiable, MessageReceiver {
    
    /**
     * An instance of {@link NullChannel} for the purpose of situations that do not require a {@link Channel}.
     * Its functions have been stubbed out to either: do nothing (impure functions), or return empty values (pure).
     */
    NullChannel NULL_CHANNEL = new NullChannel();
    
    /**
     * Return a {@link Stream} of all invokers in this channel currently.
     *
     * @return A {@link Stream} with all containing invokers
     */
    @NotNull Stream<Invoker> getAllMembers();
    
    /**
     * Adds the given {@link Invoker} to this {@link Channel}.
     *
     * @param invoker the {@link Invoker} to add
     */
    void addInvoker(@NotNull Invoker invoker);
    
    /**
     * Removes the given {@link Invoker} from this {@link Channel}.
     *
     * @param invoker the {@link Invoker} to remove
     */
    void removeInvoker(@NotNull Invoker invoker);
    
    /**
     * Return the number of {@link Invoker} in this channel
     *
     * @return An {@link int} representing the number of invokers present
     */
    int getInvokerCount();
    
    /**
     * A {@link Channel} implementation that has stubbed-out methods.
     */
    class NullChannel implements Channel {
        
        @Override
        public @NotNull String getName(){
            return "";
        }
        
        @Override
        public @NotNull String getID(){
            return "";
        }
        
        @Override
        public @NotNull Stream<Invoker> getAllMembers(){
            return Stream.empty();
        }
        
        @Override
        public void sendMessage(@NotNull String message){
        }
        
        @Override
        public void addInvoker(@NotNull Invoker invoker){
        }
        
        @Override
        public void removeInvoker(@NotNull Invoker invoker){
        }
    
        @Override
        public int getInvokerCount(){
            return 0;
        }
    
    }
    
}
