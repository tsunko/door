package academy.hekiyou.door.mock;

import academy.hekiyou.door.annotations.BranchingCommand;
import academy.hekiyou.door.annotations.GlobAll;
import academy.hekiyou.door.annotations.Module;
import academy.hekiyou.door.annotations.RegisterCommand;
import academy.hekiyou.door.annotations.optional.OptionalInteger;
import academy.hekiyou.door.model.Channel;
import academy.hekiyou.door.model.Invoker;

@Module
public class MockModuleSimpleCommands {
    
    @RegisterCommand(
            permission = "mock.allowed",
            description = "Mock command",
            alias = "mockAlias"
    )
    public void mockCommand(Invoker invoker){
        invoker.sendMessage(invoker.getID());
    }

    @RegisterCommand(
            permission = "mock.allowed",
            description = "Mock command with argument"
    )
    public void mockArgument(Invoker invoker, String message, int integerMessage){
        invoker.sendMessage(message);
        invoker.sendMessage(String.valueOf(integerMessage));
    }
    
    @RegisterCommand(
            permission = "mock.allowed",
            description = "Mock command with optional argument"
    )
    public void mockOptionalArgument(Invoker invoker, @OptionalInteger(5678) int integerMessage){
        invoker.sendMessage(String.valueOf(integerMessage));
    }
    
    @RegisterCommand(
            permission = "mock.disallowed",
            description = "Mock command that should be denied"
    )
    public void mockNoPermission(Invoker invoker){
        throw new IllegalStateException("should not reach here!");
    }
    
    @RegisterCommand(
            permission = "mock.allowed",
            description = "A branching mock command"
    )
    @BranchingCommand
    public void mockBranch(Invoker invoker){
        invoker.sendMessage(invoker.getID());
    }
    
    public void mockBranch$branch1(Invoker invoker){
        invoker.sendMessage(invoker.getName() + "1");
    }
    
    public void mockBranch$branch2(Invoker invoker){
        invoker.sendMessage(invoker.getName() + "2");
    }
    
    public void mockBranch$branch3(Invoker invoker){
        invoker.sendMessage(invoker.getName() + "3");
    }
    
    @RegisterCommand(
            permission = "mock.allowed",
            description = "Mock command that uses a channel",
            requiresChannelSupport = true
    )
    public void mockChannel(Invoker invoker, Channel channel){
        channel.sendMessage(invoker.getID());
    }
    
    @RegisterCommand(
            permission = "mock.allowed",
            description = "Mock command with mixed optional and required"
    )
    public void mockMixed(Invoker invoker, String str, @OptionalInteger(5678) int integer, boolean bool){
        invoker.sendMessage(String.format("%s%d%b", str, integer, bool));
    }
    
    @RegisterCommand(
            permission = "mock.allowed",
            description = "Mock command with globbing"
    )
    public void mockGlob(Invoker invoker, @GlobAll String globbed){
        invoker.sendMessage(globbed);
    }
    
}
