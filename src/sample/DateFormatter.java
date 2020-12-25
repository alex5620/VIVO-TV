package sample;

import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateFormatter {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static StringConverter stringConverter = new StringConverter<LocalDate>() {
        @Override public String toString(LocalDate date) {
            if (date != null) {
                return formatter.format(date);
            } else {
                return "";
            }
        }

        @Override public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, formatter);
            } else {
                return null;
            }
        }
    };

    public static java.sql.Date getDatabaseFormat(String dateString)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
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
