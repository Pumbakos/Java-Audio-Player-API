package pl.pumbakos.japwebservice.japresources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
    /**
     * Method parses date from string to Date object in default format 'yyyy-MM-dd'T'HH:mm:ss.SSS'.
     * @param s date in string format to be parsed. String should be in ISO format.
     * @return Date object if date is valid, null otherwise
     * @see DateFormat
     */
    public static Date of(String s) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.ISO);
            return dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method parses date from string to Date object in format given by parameter of type DateFormat.
     * @param s date in string format to be parsed.
     * @param format format of date.
     * @return Date object if date is valid, null otherwise
     * @see DateFormat
     */
    public static Date of(String s, DateFormat format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format.toString());
            return dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method parses date from string to Date object in custom format given by parameter.
     * @param s date in string format to be parsed.
     * @param format format of date.
     * @return Date object if date is valid, null otherwise
     */
    public static Date of(String s, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
