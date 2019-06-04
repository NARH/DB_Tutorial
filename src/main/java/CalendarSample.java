import java.text.*;
import java.util.*;

public class CalendarSample {
  public static void main(String ...args) throws Exception {
    int executeDay = Integer.valueOf(args[0]);

    int fromDay = Integer.valueOf(args[1]);
    String fromTime = args[2];

    int toDay = Integer.valueOf(args[3]);
    String toTime = args[4];

    CalendarSample bean = new CalendarSample(executeDay, fromDay, fromTime, toDay, toTime);
    System.out.println(bean.toString());
    System.out.println("valid: " + bean.isValid());
  }

  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  /** 実行日 */
  final int executeDay;
  /** 開始日 */
  final int fromDay;
  /** 開始時間 */
  final String fromTime;
  /** 終了日 */
  final int toDay;
  /** 終了時間 */
  final String toTime;

  /** 実行日カレンダー */
  private Calendar executeCalendar;

  /** 開始日カレンダー */
  private Calendar fromCalendar;

  /** 終了日カレンダー */
  private Calendar toCalendar;

  public CalendarSample(final int executeDay
      , final int fromDay, final String fromTime
      , final int toDay, final String toTime ) {

    this.executeDay = executeDay;
    this.executeCalendar = getCalendar(executeDay);

    this.fromDay = fromDay;
    this.fromTime = fromTime;
    this.fromCalendar = getCalendar(fromDay, fromTime);
    this.fromCalendar.set(Calendar.SECOND, 0);

    this.toDay = toDay;
    this.toTime = toTime;
    this.toCalendar = getCalendar(toDay, toTime);
    this.toCalendar.set(Calendar.SECOND, 59);
  }

  private Calendar getCalendar() {
    return Calendar.getInstance();
  }

  private Calendar getCalendar(final int day) {
    Calendar calendar = getCalendar();
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    return calendar;
  }

  private Calendar getCalendar(final int day, final String time) {
    Calendar calendar = getCalendar(day);
    calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.substring(0,2)));
    calendar.set(Calendar.MINUTE, Integer.valueOf(time.substring(2,4)));
    return calendar;
  }

  public boolean isValid() {
    if(executeCalendar.compareTo(fromCalendar) < 1) return false;
    if(executeCalendar.compareTo(toCalendar) < 1) return false;
    if(toCalendar.compareTo(fromCalendar) < 1) return false;
    return true;
  }

  @Override
  public String toString() {
    return String.format("Execute\tdate:\t%s\nFrom\tdate:\t%s\nTo\tdate:\t%s\n"
        , simpleDateFormat.format(executeCalendar.getTime())
        , simpleDateFormat.format(fromCalendar.getTime())
        , simpleDateFormat.format(toCalendar.getTime()));
  }
}
// vim: set ts=2 sw=2 sts=2 expandtab ff=unix fenc=utf-8:
