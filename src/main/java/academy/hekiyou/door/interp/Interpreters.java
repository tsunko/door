package academy.hekiyou.door.interp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class to maintain the registry and retrieval of Interpreters.
 * <p>
 * Applications can call {@link Interpreters#register(Class, Interpreter)} to register new {@link Interpreter}s.
 */
public class Interpreters {
    
    /**
     * A {@link Class} to {@link Interpreter} mapping; used internally to manage {@link Interpreter}s,
     *
     * @implNote Not thread safe! {@link Interpreters#register(Class, Interpreter)} would not be synchronized, even
     * if we swapped {@link HashMap} out for {@link ConcurrentHashMap}, as it would be possible for two
     * threads to pass {@link Map#containsKey(Object)} and then have one thread overwrite the other.
     */
    private static final Map<Class<?>, Interpreter<?>> REGISTERED = new HashMap<>();
    
    // register the default whenever Interpreters is access to ensure we at least have primitive support
    static{
        registerDefaults();
    }
    
    /**
     * Fetches an {@link Interpreter} to translate an object of type {@code T}.
     *
     * @param klass the class of type {@code T}
     * @param <T>   the type that requires an {@link Interpreter}
     *
     * @return An {@link Interpreter} instance, or {@code null} if none was found
     */
    @SuppressWarnings("unchecked")
    public static <T> @Nullable Interpreter<T> of(@NotNull Class<T> klass){
        return (Interpreter<T>) REGISTERED.get(klass);
    }
    
    /**
     * Registers an {@link Interpreter} to be associated with the specified {@link Class}, which later can be
     * retrieved and used.
     *
     * @param klass       the {@link Class} to map as a key
     * @param interpreter the {@link Interpreter} to map as a value
     * @param <T>         the type of {@code klass}
     *
     * @throws IllegalArgumentException if {@code klass} is already mapped to an {@link Interpreter}
     * @implNote Since the backing map is a {@link HashMap}, there is a possible race condition if registering
     * multiple {@link Interpreter}s for the same {@link Class}, causing only the last call to be registered.
     */
    public static <T> void register(@NotNull Class<T> klass, @NotNull Interpreter<T> interpreter){
        if(REGISTERED.containsKey(klass))
            throw new IllegalArgumentException(String.format("%s is already registered to %s!",
                    klass.getName(), String.valueOf(REGISTERED.get(klass))));
        
        REGISTERED.put(klass, interpreter);
    }
    
    /**
     * Registers the default interpreters, which cover {@link String} and any primitive data type.
     */
    private static void registerDefaults(){
        register(String.class, InterpreterDefaults::toString);
        register(byte.class, InterpreterDefaults::toByte);
        register(short.class, InterpreterDefaults::toShort);
        register(char.class, InterpreterDefaults::toChar);
        register(int.class, InterpreterDefaults::toInt);
        register(float.class, InterpreterDefaults::toFloat);
        register(long.class, InterpreterDefaults::toLong);
        register(double.class, InterpreterDefaults::toDouble);
        register(boolean.class, InterpreterDefaults::toBoolean);
    }
    
}
