package academy.hekiyou.door;

import academy.hekiyou.door.mock.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ModuleTest {
    
    private final String mockMessage = "mock";
    private final String mockIntegerString = "1234";
    private MockInvoker mockInvoker;
    private MockInvoker mockOtherInvoker;
    private MockChannel mockChannel;
    
    @Before
    public void setup(){
        FrontDoor.initialize();
        this.mockInvoker = new MockInvoker();
        this.mockOtherInvoker = new MockInvoker();
        this.mockChannel = new MockChannel();
        this.mockChannel.addInvoker(this.mockInvoker);
        this.mockChannel.addInvoker(this.mockOtherInvoker);
    }
    
    private void checkForRecvMessage(String command, String[] args, String expected){
        FrontDoor.process(command, mockInvoker, args);
        String recvMessage = mockInvoker.getMessage();
        Assert.assertNotNull(recvMessage);
        Assert.assertEquals(expected, recvMessage);
    }
    
    @Test
    public void testLoad(){
        Assert.assertNotNull(FrontDoor.load(MockModuleNoCommands.class));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testLoadFail(){
        FrontDoor.load(MockBadModule.class);
        Assert.fail("Loaded bad module");
    }
    
    @Test
    public void testCommandLoad(){
        Assert.assertNotNull(FrontDoor.load(MockModuleSimpleCommands.class));
    }
    
    @Test
    public void testCommandExecution(){
        Assert.assertNotNull(FrontDoor.load(MockModuleSimpleCommands.class));
        checkForRecvMessage("mockCommand", new String[0], mockInvoker.getID());
    }
    
    @Test
    public void testCommandArgumentExecution(){
        Assert.assertNotNull(FrontDoor.load(MockModuleSimpleCommands.class));
        FrontDoor.process("mockArgument", mockInvoker, new String[]{ mockMessage, mockIntegerString });
        
        String recvMessage = mockInvoker.getMessage();
        Assert.assertNotNull(recvMessage);
        Assert.assertEquals(mockMessage, recvMessage);
        
        String recvIntegerMessage = mockInvoker.getMessage();
        Assert.assertNotNull(recvIntegerMessage);
        Assert.assertEquals(mockIntegerString, recvIntegerMessage);
    }
    
    @Test
    public void testCommandBadArgumentExecution(){
        Assert.assertNotNull(FrontDoor.load(MockModuleSimpleCommands.class));
        FrontDoor.process("mockArgument", mockInvoker, new String[]{ mockMessage, mockMessage });
        
        String recvMessage = mockInvoker.getMessage();
        Assert.assertNotNull(recvMessage);
        Assert.assertEquals("Usage: mockArgument <message> --><integerMessage>", recvMessage);
    }
    
    @Test
    public void testCommandOptionalArgumentExecution(){
        Assert.assertNotNull(FrontDoor.load(MockModuleSimpleCommands.class));
        
        // first test for supplying optional parameter
        checkForRecvMessage("mockOptionalArgument", new String[]{ mockIntegerString }, mockIntegerString);
    
        // now test for no optional parameter
        checkForRecvMessage("mockOptionalArgument", new String[0], "5678");
    }
    
    @Test
    public void testNoPermissionExecution(){
        Assert.assertNotNull(FrontDoor.load(MockModuleSimpleCommands.class));
        checkForRecvMessage("mockNoPermission", new String[0], FrontDoor.getSettings().getPermissionError());
    }
    
    @Test
    public void testUnload(){
        Assert.assertNotNull(FrontDoor.load(MockModuleOneCommand.class));
        List<String> unloaded = FrontDoor.unload(MockModuleOneCommand.class);
        Assert.assertEquals(1, unloaded.size());
        Assert.assertEquals("mockOnlyCommand", unloaded.get(0));
    
        boolean shouldFail = FrontDoor.process("mockOnlyCommand", mockInvoker, new String[0]);
        Assert.assertFalse(shouldFail);
    }
    
    @Test
    public void testBranchingExecution(){
        Assert.assertNotNull(FrontDoor.load(MockModuleSimpleCommands.class));
        checkForRecvMessage("mockBranch", new String[]{ "branch1" }, mockInvoker.getName() + "1");
        checkForRecvMessage("mockBranch", new String[]{ "branch2" }, mockInvoker.getName() + "2");
        checkForRecvMessage("mockBranch", new String[]{ "branch3" }, mockInvoker.getName() + "3");
    }
    
    @Test
    public void testBadBranchExecution(){
        Assert.assertNotNull(FrontDoor.load(MockModuleSimpleCommands.class));
        checkForRecvMessage("mockBranch", new String[]{ "branchBad" },
                String.format(FrontDoor.getSettings().getInvalidSubcommandError(), "branch3, branch2, branch1"));
    }
    
    @Test
    public void testChannelCommand(){
        Assert.assertNotNull(FrontDoor.load(MockModuleSimpleCommands.class));
        
        FrontDoor.process("mockChannel", mockInvoker, mockChannel, new String[0]);
        String message = mockOtherInvoker.getMessage();
        Assert.assertNotNull(message);
        Assert.assertEquals(mockInvoker.getID(), message);
    }
    
    @Test
    public void testMixedArguments(){
        Assert.assertNotNull(FrontDoor.load(MockModuleSimpleCommands.class));
        
        checkForRecvMessage("mockMixed", new String[]{
                mockInvoker.getID(), "true"
        }, String.format("%s%s", mockInvoker.getID(), "5678true"));
    }
    
    @Test
    public void testGlobbingArguments(){
        Assert.assertNotNull(FrontDoor.load(MockModuleSimpleCommands.class));
        
        checkForRecvMessage("mockGlob", new String[]{"h", "e", "l", "l", "o"}, "h e l l o");
    }
    
}
