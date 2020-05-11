package academy.hekiyou.door.house;

import academy.hekiyou.door.FrontDoor;
import academy.hekiyou.door.annotations.RegisterCommand;
import academy.hekiyou.door.model.Channel;
import academy.hekiyou.door.model.Command;
import academy.hekiyou.door.model.Invoker;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * An extension to {@link HouseCommand} that can branch into different sub-commands.
 * @see academy.hekiyou.door.annotations.BranchingCommand
 */
public class HouseBranchingCommand extends HouseCommand {
    
    private final Map<String, Command> branches;
    private String branchesVal = null;
    
    /**
     * Constructs a branching command object
     * @param name Unused within {@link House}.
     * @param invokeRef The object that {@code method} depends on
     * @param method The method to execute as a base command
     * @param metadata The metadata of the base command
     * @param branches All branches the command can take
     */
    public HouseBranchingCommand(@NotNull String name, @NotNull Object invokeRef,
                                 @NotNull Method method, @NotNull RegisterCommand metadata,
                                 @NotNull Map<String, Command> branches){
        super(name, invokeRef, method, metadata);
        this.branches = branches;
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public void execute(@NotNull String commandName, @NotNull Invoker invoker, @NotNull Channel channel, @NotNull String[] arguments){
        if(!invoker.hasPermission(getMetadata().permission())){
            invoker.sendMessage(FrontDoor.getSettings().getPermissionError(), metadata.permission());
            return;
        }
        
        Command branchToExecute;
        
        // check if the user input a valid branch
        if(arguments.length < 1 || (branchToExecute = branches.get(arguments[0])) == null){
            invoker.sendMessage(FrontDoor.getSettings().getInvalidSubcommandError(), getBranchesVal());
            return;
        }
        
        // the first argument isn't needed anymore since it's just the branch we need to take
        arguments = Arrays.copyOfRange(arguments, 1, arguments.length);
        branchToExecute.execute(commandName, invoker, channel, arguments);
    }
    
    /**
     * Returns a list of branches that the user can take.
     *
     * @return A list of branches in a {@link String}, separated by commas
     *
     * @implSpec {@link HouseBranchingCommand#branchesVal} is lazily generated
     */
    private @NotNull String getBranchesVal(){
        if(branchesVal == null){
            StringBuilder sb = new StringBuilder();
            branches.keySet().forEach(branch -> {
                sb.append(branch).append(", ");
            });
            branchesVal = sb.substring(0, sb.length() - 2);
        }
        return branchesVal;
    }
    
}
