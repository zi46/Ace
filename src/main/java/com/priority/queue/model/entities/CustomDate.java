package com.priority.queue.model.entities;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static java.time.Duration.between;
import static java.util.Date.from;
import static java.util.TimeZone.getTimeZone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.priority.queue.util.DateInstant;

public class CustomDate implements Comparable<CustomDate> {
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX";
  public static final String DATE_ONLY_FORMAT = "yyyy-MM-dd";
  private static TimeZone timeZoneInUTC = null;

  public static SimpleDateFormat getDateFormat() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
    simpleDateFormat.setTimeZone(getUtcTimezone());
    return simpleDateFormat;
  }

  public static SimpleDateFormat getDateFormat(String dateFormat) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    simpleDateFormat.setTimeZone(getUtcTimezone());
    return simpleDateFormat;
  }

  private static TimeZone getUtcTimezone() {
    if (timeZoneInUTC == null) {
      timeZoneInUTC = getTimeZone("UTC");
    }
    return timeZoneInUTC;
  }

  @JsonUnwrapped
  @JsonFormat(shape = STRING, pattern = DATE_FORMAT)
  private Date date;

  public CustomDate() {}

  @JsonCreator
  public static CustomDate fromDate(Date date) {
    if (date == null) {
      return null;
    }
    return new CustomDate(date);
  }

  public static CustomDate parse(String dateAsString) throws ParseException {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date convertedDate = simpleDateFormat.parse(dateAsString);
    return new CustomDate(convertedDate);
  }

  public CustomDate(Date date) {
    this.date = date;
  }

  public CustomDate(Instant date) {
    this.date = from(date);
  }

  public static CustomDate now() {
    return DateInstant.now();
  }

  public static CustomDate nowPlus(long durationAmount, ChronoUnit durationUnit) {
    return now().plus(durationAmount, durationUnit);
  }

  public static CustomDate nowMinus(int durationAmount, ChronoUnit durationUnit) {
    return now().minus(durationAmount, durationUnit);
  }


  public Date getDate() {
    return date;
  }


  public CustomDate plus(long duration, ChronoUnit unit) {
    return new CustomDate(date.toInstant().plus(duration, unit));
  }

  public CustomDate minus(int duration, ChronoUnit unit) {
    return plus(-duration, unit);
  }



  public ZonedDateTime getUtcDateTime() {
    return date.toInstant().atZone(ZoneId.of("UTC"));
  }

  public LocalDate utcLocalDate() {
    return getUtcDateTime().toLocalDate();
  }

  public static Comparator<CustomDate> latestFirst() {
    return (date1, date2) -> date1.compareTo(date2) * -1;
  }

  public Duration durationTo(CustomDate other) {
    return between(date.toInstant(), other.date.toInstant());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    CustomDate shippingDate = (CustomDate) o;
    return Objects.equals(date, shippingDate.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date);
  }

  @Override
  public int compareTo(CustomDate other) {
    return date.compareTo(other.date);
  }

  @Override
  public String toString() {
    return getDateFormat().format(date);
  }
}
