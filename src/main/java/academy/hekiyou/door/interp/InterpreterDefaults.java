package academy.hekiyou.door.interp;

import academy.hekiyou.door.exception.BadInterpretationException;
import org.jetbrains.annotations.NotNull;

public class InterpreterDefaults {
    
    private static final String NON_NUMERIC_INPUT_MESSAGE = "Non-numeric input: \"%s\"";
    private static final String NON_SINGLE_CHAR_INPUT_MESSAGE = "Non-single-character input: \"%s\"";
    private static final String NON_BOOLEAN_INPUT_MESSAGE = "Non-boolean input: \"%s\"";
    
    /**
     * Identity function for interpreting strings
     *
     * @param string The string to interpret
     *
     * @return The input string
     */
    static @NotNull String toString(@NotNull String string){
        return string;
    }
    
    /**
     * Interprets a string as a byte
     *
     * @param strByte The string representing a byte
     *
     * @return The byte value of the string
     *
     * @throws BadInterpretationException If the input could not be interpreted as a byte
     */
    static byte toByte(@NotNull String strByte){
        try {
            return Byte.parseByte(strByte);
        } catch(NumberFormatException exc) {
            throw new BadInterpretationException(String.format(NON_NUMERIC_INPUT_MESSAGE, strByte), exc);
        }
    }
    
    /**
     * Interprets a string as a short
     *
     * @param strShort The string representing a short
     *
     * @return The short value of the string
     *
     * @throws BadInterpretationException If the input could not be interpreted as a short
     */
    static short toShort(@NotNull String strShort){
        try {
            return Short.parseShort(strShort);
        } catch(NumberFormatException exc) {
            throw new BadInterpretationException(String.format(NON_NUMERIC_INPUT_MESSAGE, strShort), exc);
        }
    }
    
    /**
     * Interprets a string as a char
     *
     * @param strChar The string representing a char
     *
     * @return The char value of the string
     *
     * @throws BadInterpretationException If the input could not be interpreted as a char
     */
    static char toChar(@NotNull String strChar){
        if(strChar.length() != 1){
            throw new BadInterpretationException(String.format(NON_SINGLE_CHAR_INPUT_MESSAGE, strChar));
        }
        return strChar.charAt(0);
    }
    
    /**
     * Interprets a string as a int
     *
     * @param strInt The string representing a int
     *
     * @return The int value of the string
     *
     * @throws BadInterpretationException If the input could not be interpreted as a int
     */
    static int toInt(@NotNull String strInt){
        try {
            return Integer.parseInt(strInt);
        } catch(NumberFormatException exc) {
            throw new BadInterpretationException(String.format(NON_NUMERIC_INPUT_MESSAGE, strInt), exc);
        }
    }
    
    /**
     * Interprets a string as a float
     *
     * @param strFloat The string representing a float
     *
     * @return The float value of the string
     *
     * @throws BadInterpretationException If the input could not be interpreted as a float
     */
    static float toFloat(@NotNull String strFloat){
        try {
            return Float.parseFloat(strFloat);
        } catch(NumberFormatException exc) {
            throw new BadInterpretationException(String.format(NON_NUMERIC_INPUT_MESSAGE, strFloat), exc);
        }
    }
    
    /**
     * Interprets a string as a long
     *
     * @param strLong The string representing a long
     *
     * @return The long value of the string
     *
     * @throws BadInterpretationException If the input could not be interpreted as a long
     */
    static long toLong(@NotNull String strLong){
        try {
            return Long.parseLong(strLong);
        } catch(NumberFormatException exc) {
            throw new BadInterpretationException(String.format(NON_NUMERIC_INPUT_MESSAGE, strLong), exc);
        }
    }
    
    /**
     * Interprets a string as a double
     *
     * @param strDouble The string representing a double
     *
     * @return The double value of the string
     *
     * @throws BadInterpretationException If the input could not be interpreted as a double
     */
    static double toDouble(@NotNull String strDouble){
        try {
            return Double.parseDouble(strDouble);
        } catch(NumberFormatException exc) {
            throw new BadInterpretationException(String.format(NON_NUMERIC_INPUT_MESSAGE, strDouble), exc);
        }
    }
    
    /**
     * Interprets a string as a boolean
     *
     * @param strBoolean The string representing a double
     *
     * @return The boolean value of the string
     *
     * @throws BadInterpretationException If the input could not be interpreted as a boolean
     */
    static boolean toBoolean(@NotNull String strBoolean){
        if(strBoolean.equals("true")){
            return true;
        } else if(strBoolean.equals("false")){
            return false;
        } else {
            throw new BadInterpretationException(String.format(NON_BOOLEAN_INPUT_MESSAGE, strBoolean));
        }
    }
    
}
