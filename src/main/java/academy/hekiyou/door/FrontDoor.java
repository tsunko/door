package academy.hekiyou.door;

import academy.hekiyou.door.house.House;
import academy.hekiyou.door.house.SimpleRegister;
import academy.hekiyou.door.model.Channel;
import academy.hekiyou.door.model.Invoker;
import academy.hekiyou.door.model.Register;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;

public final class FrontDoor {
    
    private static final Logger __logger = Logger.getLogger("door");
    private static final FrontDoor __instance = new FrontDoor();
    private Settings __settings;
    private House __house;
    
    /**
     * Initializes door with the default values
     * @see Settings
     */
    public static void initialize(){
        initialize(new Settings.Builder().build(), new SimpleRegister());
    }
    
    /**
     * Initializes door with the given {@link Settings} and default {@link Register}
     */
    public static void initialize(@NotNull Settings settings){
        initialize(settings, new SimpleRegister());
    }
    
    /**
     * Initializes door with default {@link Settings} and the given {@link Register}
     */
    public static void initialize(@NotNull Register commandRegister){
        initialize(new Settings.Builder().build(), commandRegister);
    }
    
    /**
     * Initializes door with the given {@link Settings} and {@link Register}
     */
    public static void initialize(@NotNull Settings settings, Register commandRegister){
        __instance.__settings = settings;
        __instance.__house = new House(commandRegister);
    }
    
    /**
     * Loads a class through House
     * @see House#load(Class)
     * @param klass The class to load
     * @param <T> The type of the class
     * @return An instance of {@code klass}
     */
    public static <T> @NotNull T load(@NotNull Class<T> klass){
        return __instance.__house.load(klass);
    }
    
    /**
     * Unloads a class through House
     * @see House#unload(Class)
     * @param klass The class to load
     * @param <T> The type of the class
     * @return A {@link List} containing the name of every command (represented as a {@link String})
     */
    public static <T> @NotNull List<String> unload(@NotNull Class<T> klass){
        return __instance.__house.unload(klass);
    }
    
    /**
     * The main method to be called for when a command is detected and needs to be invoked.
     * Simply call this in place for where normal command look-up and execution logic would be.
     * @param command The command name to invoke
     * @param invoker An instance/implementation of {@link Invoker}
     * @param chan The {@link Channel} that this command was processed in
     * @param args A {@link String} array of all arguments to pass
     * @return {@code true} if the command was found and processed successfully, {@code false} otherwise
     */
    public static boolean process(@NotNull String command, @NotNull Invoker invoker,
                                  @NotNull Channel chan, @NotNull String[] args){
        return __instance.__house.findAndExecute(command, invoker, chan, args);
    }
    
    /**
     * A variant of the {@link #process(String, Invoker, Channel, String[])} that does
     * not require a channel to be supplied.
     * @param command The command name to invoke
     * @param invoker An instance/implementation of {@link Invoker}
     * @param args A {@link String} array of all arguments to pass
     * @return {@code true} if the command was found and processed successfully, {@code false} otherwise
     */
    public static boolean process(@NotNull String command, @NotNull Invoker invoker, @NotNull String[] args){
        return __instance.__house.findAndExecute(command, invoker, args);
    }
    
    /**
     * Return the current settings that is loaded for Door
     *
     * @return A {@link Settings} instance
     */
    @NotNull
    public static Settings getSettings(){
        return __instance.__settings;
    }
    
    /**
     * Return the logger that door uses
     *
     * @return A {@link Logger}
     */
    @NotNull
    public static Logger getLogger(){
        return __logger;
    }
    
}
