package sample;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    public static java.sql.Date getDatabaseFormat(String dateString)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/d/yyyy");
        Date date;
        try
        {
            date = dateFormat.parse(dateString);
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return new java.sql.Date(date.getTime());
    }
}
