package assist;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateAssist {
    public static String getCurrentDate() {
        return getDaysAgoDate(0);
    }

    public static String getDaysAgoDate(int days) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date());
        cal.add(Calendar.DATE, -days);
        String date = sdf.format(cal.getTime());
        return date;
    }
}
