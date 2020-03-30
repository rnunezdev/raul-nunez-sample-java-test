package com.clip.assesment.utils;

import java.time.DayOfWeek;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.DAYS;

public class CustomWeeklyTemporalAdjuster implements TemporalAdjuster {

    @Override
    public Temporal adjustInto(Temporal temporal) {
        int dowValue = DayOfWeek.FRIDAY.getValue();
        int calDow = temporal.get(DAY_OF_WEEK);
        if (calDow == dowValue || temporal.get(DAY_OF_MONTH) == 1) {
            return temporal;
        }
        int daysDiff = dowValue - calDow;

        Temporal minus = temporal.minus(daysDiff >= 0 ? 7 - daysDiff : -daysDiff, DAYS);

        if(temporal.get(MONTH_OF_YEAR) != minus.get(MONTH_OF_YEAR)) {
            return temporal.minus(temporal.get(DAY_OF_MONTH)-1, DAYS);
        }

        return minus;

    }

}
