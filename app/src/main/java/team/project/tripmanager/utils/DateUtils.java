package team.project.tripmanager.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {
    public static String getFormattedDate(String startDate, String endDate) throws ParseException {
        DateFormat desiredDateFormat = new SimpleDateFormat("dd MMM");
        DateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("%s - %s", desiredDateFormat.format(serverDateFormat.parse(startDate)), desiredDateFormat.format(serverDateFormat.parse(startDate)));
    }
}
