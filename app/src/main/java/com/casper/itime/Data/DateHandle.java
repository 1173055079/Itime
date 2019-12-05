package com.casper.itime.Data;

import android.os.Handler;
import android.os.Message;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class DateHandle implements Serializable { //倒计时计算得到的数据存储到这里

    private long countdown;
    private long between;
    private long day;
    private long hour;
    private long min;
    private long s ;
    private String Title; //标题
    private String Note; //备注
    private String Date; //日期
    private String Time; //时间

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



    public DateHandle() {
    }


    public long getCountdown() {
        return countdown;
    }

    public void setCountdown(long countdown) {
        this.countdown = countdown;
    }

    public long getBetween() {
        return between;
    }

    public void setBetween(long between) {
        this.between = between;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
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
}

