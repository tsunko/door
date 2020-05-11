package academy.hekiyou.door.mock;

import academy.hekiyou.door.model.Invoker;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MockInvoker implements Invoker {
    
    private final String name = UUID.randomUUID().toString();
    private final String id = UUID.randomUUID().toString();
    private final Queue<String> messagesRecv = new LinkedList<>();
    private final Logger logger = Logger.getLogger("Invoker-" + id);
    
    @Override
    public @NotNull String getName(){
        return name;
    }
    
    @Override
    public @NotNull String getID(){
        return id;
    }
    
    @Override
    public void sendMessage(@NotNull String message){
        logger.log(Level.INFO, "Received new message: {0}", message);
        messagesRecv.add(message);
    }
    
    @Override
    public boolean hasPermission(@NotNull String permission){
        logger.log(Level.INFO, "Checking permission: {0}", permission);
        return permission.equals("mock.allowed");
    }
    
    public String getMessage(){
        return messagesRecv.isEmpty() ? null : messagesRecv.remove();
    }
    
}
