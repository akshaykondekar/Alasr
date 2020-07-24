package com.trikwetra.alasr.bean;

public class PrayerBean
{
    String prayerName,prayerTime,prayerRemainigTime,prayerTotalTime;

    public PrayerBean(String prayerName, String prayerTime, String prayerRemainigTime, String prayerTotalTime) {
        this.prayerName = prayerName;
        this.prayerTime = prayerTime;
        this.prayerRemainigTime = prayerRemainigTime;
        this.prayerTotalTime = prayerTotalTime;
    }

    public String getPrayerName() {
        return prayerName;
    }

    public void setPrayerName(String prayerName) {
        this.prayerName = prayerName;
    }

    public String getPrayerTime() {
        return prayerTime;
    }

    public void setPrayerTime(String prayerTime) {
        this.prayerTime = prayerTime;
    }

    public String getPrayerRemainigTime() {
        return prayerRemainigTime;
    }

    public void setPrayerRemainigTime(String prayerRemainigTime) {
        this.prayerRemainigTime = prayerRemainigTime;
    }

    public String getPrayerTotalTime() {
        return prayerTotalTime;
    }

    public void setPrayerTotalTime(String prayerTotalTime) {
        this.prayerTotalTime = prayerTotalTime;
    }
}
