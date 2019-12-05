package com.casper.itime.Data;

import android.net.Uri;

import java.io.Serializable;

public class ListViewData implements Serializable {
    private String Date; //日期
    private String Time; //时间
    private String Title; //标题
    private String Note; //备注
    private long countDown; //倒计时的天数
    private long year;
    private long month;
    private long monthOfDay;
    private long hour;
    private long min;
    private long s ;

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    private Uri imageUri;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }



    public long getCountDown() {
        return countDown;
    }

    public void setCountDown(long countDown) {
        this.countDown = countDown;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }


    public long getHour() {
        return hour;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getS() {
        return s;
    }

    public void setS(long s) {
        this.s = s;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public long getMonthOfDay() {
        return monthOfDay;
    }

    public void setMonthOfDay(long monthOfDay) {
        this.monthOfDay = monthOfDay;
    }
}
