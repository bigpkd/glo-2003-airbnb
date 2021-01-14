package ca.ulaval.glo2003.time.domain;

import ca.ulaval.glo2003.admin.domain.Configuration;
import java.time.ZonedDateTime;
import java.util.Calendar;

public class TimeMonth extends TimeCalendar {

  public TimeMonth(ZonedDateTime zonedDateTime) {
    super(zonedDateTime);
  }

  @Override
  protected TimeDate firstDate() {
    int firstDayOfMonth = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
    return thatDate(firstDayOfMonth);
  }

  @Override
  protected TimeDate lastDate() {
    int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    return thatDate(lastDayOfMonth);
  }

  private TimeDate thatDate(int dayOfMonth) {
    Calendar date = Calendar.getInstance();
    date.set(Calendar.YEAR, getYear());
    date.set(Calendar.MONTH, getMonth());
    date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    setAtMidnight(date);
    return new TimeDate(date.getTime());
  }

  @Override
  public String toString() {
    return calendar
        .getDisplayName(Calendar.MONTH, Calendar.LONG, Configuration.instance().getLocale())
        .toLowerCase();
  }

  @Override
  public int compareTo(TimeCalendar other) {
    return getYearMonth() - other.getYearMonth();
  }
}
