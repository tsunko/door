package academy.hekiyou.door.house;

import academy.hekiyou.door.annotations.RegisterCommand;
import academy.hekiyou.door.model.Command;
import academy.hekiyou.door.model.Register;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SimpleRegister implements Register {

    private final Map<String, Command> registered = new HashMap<>();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void register(@NotNull Command command) {
        RegisterCommand meta = command.getMetadata();
        if(isRegistered(command.getName()) && !meta.override())
            throw new IllegalStateException("already registered " + command.getName());
        
        registered.put(command.getName(), command);
        
        for(String alias : meta.alias()){
            if(!isRegistered(alias) || meta.override()) // only register aliases that haven't actually been taken already
                registered.put(alias, command);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void unregister(@NotNull Command command) {
        if(!isRegistered(command.getName()))
            return;
        
        Collection<Command> values = registered.values();
        while(values.remove(command));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable Command getCommand(@NotNull String commandName) {
        return registered.get(commandName);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Map<String, String> getRegistered() {
        Map<String, String> map = new HashMap<>();
        for(String commandName : registered.keySet())
            map.put(commandName, registered.get(commandName).getOwningClass());
        return map;
    }
    
}
