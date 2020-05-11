package academy.hekiyou.door.exception;

/**
 * Signals that an attempt at interpreting a value has failed.
 *
 * @implSpec We do not fully populate the stack trace at all; though this can be controlled by using
 * {@link BadInterpretationException#BadInterpretationException(String, boolean)} or
 * {@link BadInterpretationException#BadInterpretationException(String, Throwable, boolean)}
 */
public class BadInterpretationException extends RuntimeException {
    
    /**
     * Constructs a {@link BadInterpretationException} with no message.
     */
    public BadInterpretationException(){
        this("");
    }
    
    /**
     * Constructs a {@link BadInterpretationException} with the given message.
     */
    public BadInterpretationException(String message){
        this(message, null);
    }
    
    /**
     * Constructs a {@link BadInterpretationException} with the given message and cause of error.
     */
    public BadInterpretationException(String message, Throwable cause){
        this(message, cause, false);
    }
    
    /**
     * Constructs a {@link BadInterpretationException} with the given message and either enabling or disabling the
     * filling of stack traces.
     */
    public BadInterpretationException(String message, boolean fillStack){
        this(message, null, fillStack);
    }
    
    /**
     * Constructs a {@link BadInterpretationException} with the given message and cause of error, either enabling or
     * disabling the filling of stack traces.
     */
    public BadInterpretationException(String message, Throwable cause, boolean fillStack){
        super(message, cause, false, fillStack);
    }
    
}
