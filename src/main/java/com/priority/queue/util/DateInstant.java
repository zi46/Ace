package com.priority.queue.util;

import static java.time.Clock.systemUTC;

import java.util.function.Supplier;

import com.priority.queue.model.entities.CustomDate;

public class DateInstant {
  private static Supplier<CustomDate> dateProvider = () -> new CustomDate(systemUTC().instant());

  public static void setShippingDateProvider(Supplier<CustomDate> newDateProvider) {
    dateProvider = newDateProvider;
  }

  public static CustomDate now() {
    return dateProvider.get();
  }

  public static void resetShippingDateProvider() {
    dateProvider = () -> new CustomDate(systemUTC().instant());
  }
}
