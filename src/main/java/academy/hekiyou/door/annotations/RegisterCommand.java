package academy.hekiyou.door.annotations;

import academy.hekiyou.door.model.Channel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that is used to indicate the method is a command - that is, it is destined to be registered,
 * executed, and utilized by users.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RegisterCommand {
    
    /**
     * Return the permission node required to execute this command
     *
     * @return a {@link String} representing the permission node required
     */
    String permission() default "operator.only";
    
    /**
     * Return a simple description of the command for documentation purposes
     *
     * @return a {@link String} describing the command
     */
    String description();
    
    /**
     * Return an array of {@link String} to represent the arguments for this command.
     *
     * @return an array of {@link String}, representing arguments
     */
    String[] usage() default {};
    
    /**
     * Return an array of {@link String} to represent aliases for this command.
     *
     * @return an array of {@link String}, representing aliases or an empty {@link String} array if none
     */
    String[] alias() default {};
    
    /**
     * Return weather or not to override existing commands.
     *
     * @return {@code true} if we should override and overwrite other commands,
     *         {@code false} otherwise
     */
    boolean override() default false;
    
    /**
     * If set to {@code true}, the command will require a {@link Channel} (or subclass of).
     * If {@code false} (default), the command does not require a {@link Channel} (or subclass of),
     * and therefore, its method does not require one.
     *
     * @return {@code true} if the command requires a Channel, {@code false} otherwise
     */
    boolean requiresChannelSupport() default false;
    
}
