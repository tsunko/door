package academy.hekiyou.door.exception;

/**
 * Represents when {@link academy.hekiyou.door.model.Invoker#as(Class)} ran into a ClassCastException
 */
public class BadCastException extends ClassCastException {
    
    private Class<?> expected;
    
    /**
     * Constructs a {@link BadCastException}, indicating we were expected a specific {@link Class} and
     * the reason being {@code message}
     * @param expected The {@link Class} we tried to cast to
     * @param message The reason of failure
     */
    public BadCastException(Class<?> expected, String message){
        super(message);
        this.expected = expected;
    }
    
    /**
     * Constructs a {@link BadCastException}, indicating we were expected a specific {@link Class}
     * @param expected The {@link Class} we tried to cast to
     */
    public BadCastException(Class<?> expected){
        this(expected, null);
    }
    
    /**
     * Return the expected {@link Class} when trying to cast
     * @return The expected {@link Class}
     */
    public Class<?> getExpected(){
        return expected;
    }

}
