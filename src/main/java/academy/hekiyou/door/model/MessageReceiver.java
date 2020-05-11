package academy.hekiyou.door.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an object capable of receiving messages (i.e chat messages).
 */
public interface MessageReceiver {
    
    /**
     * Sends a message to the receiver.
     *
     * @param message the message to send
     */
    void sendMessage(@NotNull String message);
    
    /**
     * Sends a formatted message to the receiver.
     *
     * @param format the format of the message
     * @param args   the arguments to use {@code format} with
     */
    default void sendMessage(@NotNull String format, @Nullable Object... args){
        sendMessage(String.format(format, args));
    }
    
}
