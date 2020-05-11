package academy.hekiyou.door.mock;

import academy.hekiyou.door.annotations.Module;
import academy.hekiyou.door.annotations.RegisterCommand;
import academy.hekiyou.door.model.Invoker;

@Module
public class MockModuleOneCommand {
    
    @RegisterCommand(
            permission = "mock.allowed",
            description = "The only mock command"
    )
    public void mockOnlyCommand(Invoker invoker){
        invoker.sendMessage(invoker.getID());
    }
    
}
