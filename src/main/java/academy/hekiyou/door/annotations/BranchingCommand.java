package academy.hekiyou.door.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tags a method as being a "command method" that branches into sub-commands.
 * For instance, a command "/members" can be split into "/members add" and "/members remove".
 * Using {@link BranchingCommand}, we can define the given commands as:
 * <pre>
 * {@code
 * @RegisterCommand( ... values ... )
 * @BranchingCommand
 * public void members(Invoker i, Channel c){
 *      // expected to be empty
 * }
 *
 * public void members$add(Invoker i, Channel c, String name){
 *      // ... do member adding logic ...
 * }
 *
 * public void members$remove(Invoker i, Channel c, String name){
 *      // ... do member removing logic ...
 * }
 * }
 * </pre>
 * Commands that utilize {@link BranchingCommand} must:
 * <ul>
 * <li>Declare a {@link RegisterCommand} (to create the root command)
 *       along with {@link BranchingCommand} (signal to the loader that this command branches)</li>
 * <li>Separate valid sub-commands into their own methods, using {@code $} as a splitting token.</li>
 * <li>Branching/sub-commands must not declare {@link RegisterCommand} or {@link BranchingCommand}</li>
 * </ul>
 * @implSpec Note that the root function will not be called; this is currently a design flaw as a result of the
 *           challenges of getting mapping method signatures to user-invokable commands.
 * <p>
 * TODO: Solve the issue written in @implSpec
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface BranchingCommand {

}
