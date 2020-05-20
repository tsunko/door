package academy.hekiyou.door.house;

import academy.hekiyou.door.FrontDoor;
import academy.hekiyou.door.Settings;
import academy.hekiyou.door.annotations.GlobAll;
import academy.hekiyou.door.annotations.RegisterCommand;
import academy.hekiyou.door.annotations.optional.Optional;
import academy.hekiyou.door.annotations.optional.OptionalDouble;
import academy.hekiyou.door.annotations.optional.OptionalLong;
import academy.hekiyou.door.annotations.optional.*;
import academy.hekiyou.door.exception.BadInterpretationException;
import academy.hekiyou.door.interp.Interpreter;
import academy.hekiyou.door.interp.Interpreters;
import academy.hekiyou.door.model.Channel;
import academy.hekiyou.door.model.Command;
import academy.hekiyou.door.model.Invoker;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A {@link Command} implementation that will automatically determine the argument types and execute functions.
 */
public class HouseCommand implements Command {
    
    private static final Logger logger = FrontDoor.getLogger();
    private static final Map<Class<?>, Method> DEFAULT_VALUE_METHOD = new HashMap<>();
    
    static{
        initDefaultMap();
    }
    
    final RegisterCommand metadata;
    
    private final Object invokeRef;
    private final String name;
    private final Method method;
    private final int minArguments;
    
    private Parameter[] cachedParameters;
    private String[] usage;
    private Annotation[] optionals;
    
    /**
     * Constructs a {@link HouseCommand} with the given context
     *
     * @param name      The primary name of the command
     * @param invokeRef An instance of the class that we execute {@code method} with
     * @param method    The method for the command (for execution)
     * @param metadata  The annotation metadata attached to {@code method}
     */
    public HouseCommand(@NotNull String name, @NotNull Object invokeRef,
                        @NotNull Method method, @NotNull RegisterCommand metadata){
        this.name = name;
        this.invokeRef = invokeRef;
        this.metadata = metadata;
        this.method = method;
        
        // figure out if we have just the invoker or invoker and channel; in either case:
        // we need to ignore those arguments
        int argOffset = metadata.requiresChannelSupport() ? 2 : 1;
        
        this.cachedParameters = Arrays.copyOfRange(method.getParameters(), argOffset, method.getParameterCount());
        this.optionals = initOptionals(cachedParameters);
        this.minArguments = (int) Arrays.stream(optionals).filter(Objects::isNull).count();
        
        if(this.metadata.usage().length == 0 && this.cachedParameters.length > 0){
            this.usage = buildUsage();
        } else {
            this.usage = this.metadata.usage();
        }
    }
    
    /**
     * Initializes {@link HouseCommand#DEFAULT_VALUE_METHOD} with some default values. Somewhat ironic, isn't it?
     * TODO: Allow the ability to use other optional annotations
     *
     * @throws ClassCastException if a given class did not have a {@code defaultValue} method
     */
    private static void initDefaultMap(){
        String methodName = "value";
        Class<?>[] optionalAnnotations = {
                OptionalByte.class, OptionalCharacter.class, OptionalDouble.class,
                OptionalFloat.class, OptionalInteger.class, OptionalLong.class,
                OptionalShort.class, OptionalBoolean.class, OptionalString.class
                // we would add OptionalObject here, but we have special logic to handle it
        };
        
        for(Class<?> klass : optionalAnnotations){
            try {
                Method method = klass.getDeclaredMethod(methodName);
                method.setAccessible(true);
                DEFAULT_VALUE_METHOD.put(klass, method);
            } catch(NoSuchMethodException exc) {
                throw new ClassCastException("bad optional class: " + klass.getName());
            }
        }
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public void execute(@NotNull String commandName, @NotNull Invoker invoker,
                        @NotNull Channel channel, @NotNull String[] arguments){
        if(!invoker.hasPermission(metadata.permission())){
            invoker.sendMessage(FrontDoor.getSettings().getPermissionError(), metadata.permission());
            return;
        }
        
        if(arguments.length < minArguments){
            // pass -1 because we don't want to highlight any specific error; just give usage
            invoker.sendMessage(FrontDoor.getSettings().getUsageErrorFormat(),
                    formatError(commandName, -1));
            return;
        }
        
        Object[] methodArguments = new Object[cachedParameters.length];
        Class<?> paramType;
        Interpreter<?> interpreter;
        for(int i = 0, j = 0; i < methodArguments.length; i++){
            paramType = cachedParameters[i].getType();
            interpreter = Interpreters.of(paramType);
            if(interpreter == null)
                throw new IllegalStateException(paramType.getName() + " has no interpreter");
            
            // got a GlobAll, just glob everything
            if(cachedParameters[j].isAnnotationPresent(GlobAll.class)){
                StringBuilder sb = new StringBuilder();
                for(; j < arguments.length; j++)
                    sb.append(arguments[j]).append(' ');
                methodArguments[i] = sb.substring(0, sb.length() - 1).trim();
                break;
            }
            
            if(j < arguments.length){
                try {
                    methodArguments[i] = interpreter.apply(arguments[j]);
                    j++; // increment j to indicate we successfully interpreted the arg at j
                    continue;
                } catch(BadInterpretationException exc) {
                    // let it execute to supplying defaults
                }
            }
            
            Annotation optional = optionals[i];
            if(optional != null){
                // if we have an optional object, just set to null
                // otherwise try to get the default value
                methodArguments[i] = (optional instanceof OptionalObject ? null : getDefaultVal(optional));
            } else {
                invoker.sendMessage(FrontDoor.getSettings().getUsageErrorFormat(),
                        formatError(commandName, i));
                return;
            }
        }
        
        try {
            // insert invoker and channel (if needed) at the front
            List<Object> args = front(invoker, methodArguments);
            if(metadata.requiresChannelSupport())
                args.add(1, channel);
            method.invoke(invokeRef, args.toArray(new Object[0]));
        } catch(IllegalAccessException exc) {
            throw new IllegalStateException(exc);
        } catch(InvocationTargetException exc) {
            throw new RuntimeException(exc);
        }
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public @NotNull String getName(){
        return name;
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public @NotNull RegisterCommand getMetadata(){
        return metadata;
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public @NotNull String getOwningClass(){
        return method.getDeclaringClass().getName();
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public @NotNull Parameter[] getParameters(){
        return cachedParameters;
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public @NotNull String[] getUsage(){
        return usage;
    }
    
    /**
     * A simple wrapper function that throws runtime exceptions if a reflection-related error happened
     *
     * @param defaultAnnotation The annotation whose {@code defaultValue} we should invoke
     *
     * @return The return value of the annotation's {@code defaultValue} method
     *
     * @throws SecurityException     if we catch an {@link IllegalAccessException} while invoking {@code defaultValue}
     * @throws IllegalStateException if we catch an {@link InvocationTargetException} while invoking {@code defaultValue}
     */
    private static @NotNull Object getDefaultVal(@NotNull Annotation defaultAnnotation){
        try {
            Method defaultFunc = DEFAULT_VALUE_METHOD.get(defaultAnnotation.annotationType());
            if(defaultFunc == null)
                throw new IllegalStateException("don't know how to handle " + defaultAnnotation.annotationType());
            return defaultFunc.invoke(defaultAnnotation);
        } catch(IllegalAccessException exc) {
            throw new SecurityException("bad access?");
        } catch(InvocationTargetException exc) {
            throw new IllegalStateException(exc);
        }
    }
    
    /**
     * Initializes the optionals array, used to figure out which parameters are optional
     *
     * @param params an array of {@link Parameter}, typically from {@link Method#getParameters()}
     *
     * @return An array of {@link Annotation}, null positions indicating required
     */
    private @NotNull Annotation[] initOptionals(@NotNull Parameter[] params){
        Annotation[] optionals = new Annotation[params.length];
        for(int i = 0; i < params.length; i++){
            for(Annotation annotation : params[i].getAnnotations()){
                if(annotation.annotationType().isAnnotationPresent(Optional.class)){
                    optionals[i] = annotation;
                    break;
                }
            }
        }
        return optionals;
    }
    
    /**
     * Formats an error message to emphasize the erroneous parameter
     *
     * @param commandName The name of the command
     * @param errorIndex  The index of the erroneous parameter
     *
     * @return The formatted string, emphasizing a bad parameter
     */
    private @NotNull String formatError(@NotNull String commandName, int errorIndex){
        StringBuilder builder = new StringBuilder(commandName);
        Settings settings = FrontDoor.getSettings();
        
        for(int i = 0; i < usage.length; i++){
            builder.append(' ');
            if(i == errorIndex){
                builder.append(settings.getInvalidArgumentPrefix());
                builder.append(usage[i]);
            } else {
                builder.append(settings.getErrorPrefix());
                builder.append(usage[i]);
            }
        }
        
        return builder.toString().trim();
    }
    
    /**
     * Constructs a {@link String} array that will be presented when the user incorrectly uses the command
     */
    private @NotNull String[] buildUsage(){
        if(cachedParameters.length == 0){
            logger.log(Level.WARNING, "attempted to build usage array for no-arg command {0}", name);
            return new String[]{};
        }
        
        boolean useTypeNames = false;
        // check if class was built without parameters; if it was, use parameter types
        if(cachedParameters[0].getName().equals("arg1")){
            logger.log(Level.WARNING, "default parameter names for {0}; \"-parameters\" missing?", name);
            useTypeNames = true;
        }
        
        List<String> parameterNames = new ArrayList<>();
        
        try {
            for(int i = 0; i < cachedParameters.length; i++){
                Parameter param = cachedParameters[i];
                String argName = useTypeNames ? param.getType().getSimpleName() : param.getName();
        
                if(param.isAnnotationPresent(GlobAll.class))
                    argName = "... " + argName + " ...";
        
                if(optionals[i] != null){
                    Object def;
                    if(optionals[i] instanceof OptionalObject){
                        def = ((OptionalObject) optionals[i]).value();
                    } else {
                        def = getDefaultVal(optionals[i]);
                    }
                    argName = "[" + argName + "=" + def + "]";
                } else {
                    argName = "<" + argName + ">";
                }
                parameterNames.add(argName);
            }
        } catch (Exception exc){
            logger.log(Level.SEVERE, "Failed to build usage for {0}", name);
        }
        return parameterNames.toArray(new String[0]);
    }
    
    /**
     * Creates a new List with the given front element being in the forefront of the new list.
     *
     * @param frontElem The front-most element that should be used
     * @param arr       The array to base the list off of
     * @param <T>       The type of elements in the list
     *
     * @return A list with frontElem as the 0th item
     */
    private <T> @NotNull List<T> front(@NotNull T frontElem, @NotNull T[] arr){
        List<T> list = new ArrayList<>(Arrays.asList(arr));
        list.add(0, frontElem);
        return list;
    }
    
}
