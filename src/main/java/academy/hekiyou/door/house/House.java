package academy.hekiyou.door.house;

import academy.hekiyou.door.annotations.BranchingCommand;
import academy.hekiyou.door.annotations.Module;
import academy.hekiyou.door.annotations.RegisterCommand;
import academy.hekiyou.door.model.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A full fledged module loading system to simplify command registration and execution
 */
public class House implements ModuleLoader, Executor {
    
    private Register commandRegister;
    
    /**
     * Constructs a House and uses the default register system.
     */
    public House(){
        this.commandRegister = new SimpleRegister();
    }
    
    /**
     * Constructs a House and uses the provided register system.
     * @param register The command register to use
     */
    public House(Register register){
        this.commandRegister = register;
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public <T> @NotNull T load(@NotNull Class<T> klass){
        if(!klass.isAnnotationPresent(Module.class))
            throw new IllegalArgumentException(klass.getName() + " is not annotated with Module");
        
        T inst;
        try {
            Constructor<T> constructor = klass.getConstructor();
            if(constructor == null)
                throw new IllegalArgumentException(klass.getName() + " did not have a no-arg constructor.");
            inst = constructor.newInstance();
        } catch(NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException exc) {
            throw new RuntimeException(exc);
        }
        
        for(Method method : klass.getDeclaredMethods()){
            if(!Modifier.isPublic(method.getModifiers()) || !method.isAnnotationPresent(RegisterCommand.class))
                continue;
            
            RegisterCommand meta = method.getDeclaredAnnotation(RegisterCommand.class);
            String commandName = method.getName();
            
            checkParameters(method, meta.requiresChannelSupport());
            
            Command cmd;
            if(method.isAnnotationPresent(BranchingCommand.class)){
                Map<String, Command> branches = generateBranches(commandName, inst, klass, meta);
                cmd = new HouseBranchingCommand(commandName, inst, method, meta, branches);
            } else {
                cmd = new HouseCommand(commandName, inst, method, meta);
            }
            
            commandRegister.register(cmd);
        }
        
        return inst;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean findAndExecute(@NotNull String commandName, @NotNull Invoker invoker,
                                  @NotNull Channel chan, @NotNull String[] args) {
        Command cmd = commandRegister.getCommand(commandName);
        if(cmd == null)
            return false;
        cmd.execute(commandName, invoker, chan, args);
        return true;
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public @NotNull List<String> unload(@NotNull Class<?> klass){
        List<String> unloadedCommands = new ArrayList<>();
        for(String key : commandRegister.getRegistered().keySet()){
            Command command = commandRegister.getCommand(key);
            if(command == null)
                continue;
            if(command.getOwningClass().equals(klass.getName())){
                unloadedCommands.add(key);
                commandRegister.unregister(command);
            }
        }
        return unloadedCommands;
    }
    
    /**
     * Generates a mapping of all possible branches given a base command
     * @param base The base command to check for
     * @param ref A reference to the method for execution
     * @param klass The class to check for
     * @param meta The metadata for the root object
     * @return A {@link Map} map containing all possible branches, where keys are the branch name and values are
     *         a {@link HouseCommand} object
     */
    private @NotNull Map<String, Command> generateBranches(@NotNull String base, @NotNull Object ref,
                                                           @NotNull Class<?> klass, @NotNull RegisterCommand meta){
        Map<String, Command> branches = new HashMap<>();
        for(Method method : klass.getDeclaredMethods()){
            if(!Modifier.isPublic(method.getModifiers()) || !method.getName().contains("$"))
                continue;
            
            String[] splitName = method.getName().split("\\$");
            if(splitName.length < 2)
                continue;
            
            if(base.equals(splitName[0]))
                branches.put(splitName[1], new HouseCommand(method.getName(), ref, method, meta));
        }
        return branches;
    }
    
    /**
     * Verifies method parameters matches what is required for House to call
     *
     * @param method           The method to validate
     * @param requiresChannels If the method requires channels to run (i.e {@link RegisterCommand#requiresChannelSupport()})
     *
     * @throws IllegalArgumentException If the method did not match what was expected
     */
    private void checkParameters(@NotNull Method method, boolean requiresChannels){
        Class<?>[] paramTypes = method.getParameterTypes();
        if(method.getParameterCount() < 1 || !Invoker.class.isAssignableFrom(paramTypes[0]))
            throw new IllegalArgumentException("ill-formatted method signature; missing Invoker as first argument");
        
        if(requiresChannels && (method.getParameterCount() < 2 || !Channel.class.isAssignableFrom(paramTypes[1])))
            throw new IllegalArgumentException("ill-formatted method signature; missing Channel as second argument");
    }
    
}
