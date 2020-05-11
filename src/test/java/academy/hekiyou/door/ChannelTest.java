package academy.hekiyou.door;

import academy.hekiyou.door.mock.MockChannel;
import academy.hekiyou.door.mock.MockInvoker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class ChannelTest {
    
    private static final int MOCK_INVOKER_SET_SIZE = 3;
    
    private MockChannel   mockChannel;
    private MockInvoker[] mockInvokers;
    
    @Before
    public void setup(){
        mockChannel = new MockChannel();
        mockInvokers = new MockInvoker[MOCK_INVOKER_SET_SIZE];
        for(int i=0; i < MOCK_INVOKER_SET_SIZE; i++){
            MockInvoker mock = new MockInvoker();
            mockInvokers[i] = mock;
            mockChannel.addInvoker(mock);
        }
    }
    
    @Test
    public void testAdd(){
        MockInvoker mock = new MockInvoker();
        mockChannel.addInvoker(mock);
        Assert.assertEquals(MOCK_INVOKER_SET_SIZE + 1, mockChannel.getInvokerCount());
    }
    
    @Test
    public void testRemove(){
        for(MockInvoker invoker : mockInvokers)
            mockChannel.removeInvoker(invoker);
        Assert.assertEquals(0, mockChannel.getInvokerCount());
    }
    
    @Test
    public void testChannelMessage(){
        String message = UUID.randomUUID().toString();
        mockChannel.sendMessage(message);
        
        for(int i=0; i < MOCK_INVOKER_SET_SIZE; i++){
            MockInvoker invoker = mockInvokers[i];
            String recvMessage = invoker.getMessage();
            Assert.assertNotNull(recvMessage);
            Assert.assertEquals(message, recvMessage);
        }
    }
    
}
