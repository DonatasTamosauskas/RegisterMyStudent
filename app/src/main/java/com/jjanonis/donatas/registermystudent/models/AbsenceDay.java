package com.jjanonis.donatas.registermystudent.models;

import java.time.LocalDate;

public class AbsenceDay {
    private String date;
    private String lecture;
    private String reason;

    public AbsenceDay(String date, String lecture, String reason) {
        this.date = date;
        this.lecture = lecture;
        this.reason = reason;
    }

    public AbsenceDay(String date, String lecture) {
        this(date, lecture, " ");
    }

    public AbsenceDay(String date) {
        this(date, "No lecture selected.", " ");
    }

    public AbsenceDay() {
        this(LocalDate.now().toString());
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
