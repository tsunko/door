package academy.hekiyou.door;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class SettingsTest {
    
    private static final String testPre  = UUID.randomUUID().toString();
    private static final String testArg  = UUID.randomUUID().toString();
    private static final String testSub  = UUID.randomUUID().toString();
    private static final String testPerm = UUID.randomUUID().toString();
    private static final String testUse  = UUID.randomUUID().toString();
    
    @Test
    public void customSettingsBuilder(){
        Settings.Builder builder = new Settings.Builder();
        builder.errorPrefix(testPre)
               .invalidArgumentPrefix(testArg)
               .invalidSubcommandError(testSub)
               .permissionError(testPerm)
               .usageErrorFormat(testUse);
        
        Settings settings = builder.build();
        
        Assert.assertEquals(testPre, settings.getErrorPrefix());
        Assert.assertEquals(testArg, settings.getInvalidArgumentPrefix());
        Assert.assertEquals(testSub, settings.getInvalidSubcommandError());
        Assert.assertEquals(testPerm, settings.getPermissionError());
        Assert.assertEquals(testUse, settings.getUsageErrorFormat());
    }
    
}
