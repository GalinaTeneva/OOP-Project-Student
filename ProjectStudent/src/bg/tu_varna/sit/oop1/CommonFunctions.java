package bg.tu_varna.sit.oop1;

import bg.tu_varna.sit.oop1.enums.UserMessages;

/**
 * The CommonFunctions class provides utility methods for common operations
 * like checking if a string is a number and parsing string to int or double number.
 */
public class CommonFunctions {
    
    /**
     * Checks if the given string is an integer or a double number.
     *
     * @param value The string to be checked.
     * @return true if the string is number and false if it is not.
     */
    public static boolean isNumber(String value) {
        String pattern = "\\d+(.\\d+)?";
        return value.matches(pattern);
    }

    /**
     * Parses a string into an integer.
     * Throws a NumberFormatException if the string cannot be parsed.
     *
     * @param value The string to be parsed.
     * @return The integer value.
     * @throws NumberFormatException If the string is not a valid integer.
     */
    public static int intParser(String value) {
        //Checking if the value can be parsed
        boolean isNumber = isNumber(value);
        //Exception if  the value can not be parsed
        if (!isNumber) {
            throw new NumberFormatException(String.format(UserMessages.WRONG_NUMBER_DATA.message, value));
        }

        return Integer.parseInt(value);
    }

    /**
     * Parses a string into a double.
     * Throws a NumberFormatException if the string cannot be parsed.
     *
     * @param value The string to be parsed.
     * @return The double value.
     * @throws NumberFormatException If the string is not a valid double.
     */
    public static double doubleParser(String value) {
        //Checking if the value can be parsed
        boolean isNumber = isNumber(value);
        //Exception if the value can not be parsed
        if (!isNumber) {
            throw new NumberFormatException(String.format(UserMessages.WRONG_NUMBER_DATA.message, value));
        }

        return Double.parseDouble(value);
    }
}
