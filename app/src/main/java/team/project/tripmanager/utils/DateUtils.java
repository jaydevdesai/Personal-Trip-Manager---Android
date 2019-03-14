package team.project.tripmanager.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {
    public static String getFormattedDatePeriod(String startDate, String endDate) throws ParseException {
        DateFormat desiredDateFormat = new SimpleDateFormat("dd MMM");
        DateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("%s - %s", desiredDateFormat.format(serverDateFormat.parse(startDate)), desiredDateFormat.format(serverDateFormat.parse(startDate)));
    }

    public static String getFormattedDate(String date, String pattern) throws ParseException {
        DateFormat desiredDateFormat = new SimpleDateFormat(pattern);
        DateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return desiredDateFormat.format(serverDateFormat.parse(date));
    }
}
