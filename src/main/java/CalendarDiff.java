import java.text.*;
import java.util.*;

public class CalendarDiff {
  static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static void main(String ...args) throws Exception {
    Calendar cal1 = getCalendar("2019-02-27 00:00:00");
    Calendar cal2 = getCalendar("2019-03-26 23:59:59");
    System.out.printf("daily span = %d days\n", diffDate(cal1, cal2));

    String result = (isMonthlySpan(cal1, cal2)) ? "OK" : "NG";
    System.out.println(result);

    cal1 = getCalendar("2019-01-01 00:00:00");
    cal2 = getCalendar("2019-01-01 23:59:59");
    System.out.printf("Hour span = %d hours\n", diffHour(cal1, cal2));

    result = (isDailySpan(cal1, cal2)) ? "OK" : "NG";
    System.out.println(result);
  }

  public static Calendar getCalendar(final String dateStr) throws Exception {
    Calendar cal = Calendar.getInstance();
    cal.setTime(sdf.parse(dateStr));
    return cal;
  }

  public static int diffHour(final Calendar cal1, final Calendar cal2) {
    long diff = diff(cal1, cal2);
    return (int) (diff / (60L * 60L * 1000L));
  }

  public static int diffDate(final Calendar cal1, final Calendar cal2) {
    long diff = diff(cal1, cal2);
    return (int) (diff / (24L * 60L * 60L * 1000L));
  }

  public static long diff(final Calendar cal1, final Calendar cal2) {
    long time1 = cal1.getTimeInMillis();
    long time2 = cal2.getTimeInMillis();

    return (time2 - time1) + 1000L;
  }

  public static boolean isMonthlySpan(final Calendar cal, final int span) throws Exception {
    int fromDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    return (fromDayOfMonth == span);
  }

  public static boolean isMonthlySpan(final Calendar cal1, final Calendar cal2) throws Exception {
    int fromDayOfMonth = cal1.getActualMaximum(Calendar.DAY_OF_MONTH);
    int monthlySpan = diffDate(cal1, cal2);
    return isMonthlySpan(cal1, monthlySpan);
  }

  public static boolean isDailySpan(final int span) throws Exception {
    return (24 == span);
  }

  public static boolean isDailySpan(final Calendar cal1, final Calendar cal2) throws Exception {
    return isDailySpan(diffHour(cal1, cal2));
  }
}
// vim: set ts=2 sw=2 sts=2 expandtab ff=unix fenc=utf-8:
