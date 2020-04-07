package com.clip.assesment.utils;

import java.time.DayOfWeek;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.DAYS;

public class CustomWeekFinishTemporalAdjuster implements TemporalAdjuster {

    @Override
    public Temporal adjustInto(Temporal temporal) {
        Temporal newDate = temporal.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));

        if (temporal.get(DAY_OF_WEEK) == 4) {
            return temporal;
        }

        if (newDate.get(MONTH_OF_YEAR) != temporal.get(MONTH_OF_YEAR)) {
            return newDate.minus(newDate.get(DAY_OF_MONTH), DAYS);
        }

        return newDate;

    }

}
