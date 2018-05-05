package com.jjanonis.donatas.registermystudent.models;

public class AbsenceDay {
    private String date;
    private String field;

    public AbsenceDay(String date, String field) {
        this.date = date;
        this.field = field;
    }

    public AbsenceDay(String date) {
        this(date, "No field selected.");
    }

    public AbsenceDay() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
