package academy.hekiyou.door;

import academy.hekiyou.door.exception.BadInterpretationException;
import academy.hekiyou.door.interp.Interpreter;
import academy.hekiyou.door.interp.Interpreters;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;
import java.util.UUID;

public class InterpreterTest {
    
    @Test
    public void testStockInterpreters(){
        Interpreter<String> stringInterp = Interpreters.of(String.class);
        Interpreter<Byte> byteInterp = Interpreters.of(byte.class);
        Interpreter<Short> shortInterp = Interpreters.of(short.class);
        Interpreter<Character> charInterp = Interpreters.of(char.class);
        Interpreter<Integer> intInterp = Interpreters.of(int.class);
        Interpreter<Long> longInterp = Interpreters.of(long.class);
    
        // all of these are built-in; they should not be null
        Assert.assertNotNull(stringInterp);
        Assert.assertNotNull(byteInterp);
        Assert.assertNotNull(shortInterp);
        Assert.assertNotNull(charInterp);
        Assert.assertNotNull(intInterp);
        Assert.assertNotNull(longInterp);
        
        // we don't test float/double as it can cause inaccuracies
        Assert.assertEquals("String", stringInterp.apply("String"));
        Assert.assertEquals(127, (byte)byteInterp.apply("127"));
        Assert.assertEquals(32767, (short)shortInterp.apply("32767"));
        Assert.assertEquals(65535, (char)charInterp.apply("\uffff"));
        Assert.assertEquals(2147483647, (int)intInterp.apply("2147483647"));
        Assert.assertEquals(9223372036854775807L, (long)longInterp.apply("9223372036854775807"));
    }
    
    @Test(expected = BadInterpretationException.class)
    public void testBadInterpretation(){
        Interpreter<Byte> byteInterp = Objects.requireNonNull(Interpreters.of(byte.class));
        byteInterp.apply("bad input");
        Assert.fail("String was interpreted as bytes");
    }
    
    @Test
    public void testCustomInterpreter(){
        UUID testUUID = UUID.randomUUID();
        
        Interpreters.register(UUID.class, UUID::fromString);
        Interpreter<UUID> uuidInterp = Interpreters.of(UUID.class);
        
        Assert.assertNotNull(uuidInterp);
        Assert.assertEquals(testUUID, uuidInterp.apply(testUUID.toString()));
    }
    
}
