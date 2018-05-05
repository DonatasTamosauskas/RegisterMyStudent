package com.jjanonis.donatas.registermystudent.models;

import java.time.LocalDate;

public class AbsenceDay {
    private LocalDate date;
    private String field;

    public AbsenceDay(LocalDate date, String field) {
        this.date = date;
        this.field = field;
    }

    public AbsenceDay(LocalDate date) {
        this(date, "No field selected.");
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
