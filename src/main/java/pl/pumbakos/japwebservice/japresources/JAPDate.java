package pl.pumbakos.japwebservice.japresources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JAPDate {
    public static Date of(String s) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.ISO);
            return dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
