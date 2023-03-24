package com.fastcampus.schedule.schedules.util;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Period {
    private  final LocalDate startDate;
    private final LocalDate endDate;

    private Period(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }


    public static Period of(LocalDate startDate, LocalDate endDate) {
        return new Period(startDate, endDate == null ? startDate : endDate);
    }




    public boolean isOverlapped(Period period) {
        return isOverlapped(period.startDate, period.endDate);
    }

    private boolean isOverlapped(LocalDate startDate, LocalDate endDate) {
        return this.startDate.isBefore(endDate) && startDate.isBefore(this.endDate);

    }
}


