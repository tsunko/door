package academy.hekiyou.door.mock;

import academy.hekiyou.door.model.Channel;
import academy.hekiyou.door.model.Invoker;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class MockChannel implements Channel {
    
    private final String name = UUID.randomUUID().toString();
    private final String id = UUID.randomUUID().toString();
    private final Logger logger = Logger.getLogger("Channel-" + id);
    
    private final List<Invoker> inChannel = new ArrayList<>();
    
    @Override
    public @NotNull Stream<Invoker> getAllMembers(){
        return inChannel.stream();
    }
    
    @Override
    public void addInvoker(@NotNull Invoker invoker){
        logger.log(Level.INFO, "Adding invoker: {0}", invoker.getID());
        inChannel.add(invoker);
    }
    
    @Override
    public void removeInvoker(@NotNull Invoker invoker){
        logger.log(Level.INFO, "Removing invoker: {0}", invoker.getID());
        inChannel.remove(invoker);
    }
    
    @Override
    public @NotNull String getID(){
        return id;
    }
    
    @Override
    public @NotNull String getName(){
        return name;
    }
    
    @Override
    public int getInvokerCount(){
        return inChannel.size();
    }
    
    @Override
    public void sendMessage(@NotNull String message){
        logger.log(Level.INFO, "Broadcasting message: {0}", message);
        for(Invoker invoker : inChannel)
            invoker.sendMessage(message);
    }
    
}
