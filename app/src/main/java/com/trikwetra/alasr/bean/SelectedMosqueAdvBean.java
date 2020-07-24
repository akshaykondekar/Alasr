package com.trikwetra.alasr.bean;

public class SelectedMosqueAdvBean {

    String mosqueName,mosqueDistance,mosqueReachTime;
    int advOrMosImage;

    public SelectedMosqueAdvBean(String mosqueName, String mosqueDistance, String mosqueReachTime, int advOrMosImage) {
        this.mosqueName = mosqueName;
        this.mosqueDistance = mosqueDistance;
        this.mosqueReachTime = mosqueReachTime;
        this.advOrMosImage = advOrMosImage;
    }

    public String getMosqueName() {
        return mosqueName;
    }

    public void setMosqueName(String mosqueName) {
        this.mosqueName = mosqueName;
    }

    public String getMosqueDistance() {
        return mosqueDistance;
    }

    public void setMosqueDistance(String mosqueDistance) {
        this.mosqueDistance = mosqueDistance;
    }

    public String getMosqueReachTime() {
        return mosqueReachTime;
    }

    public void setMosqueReachTime(String mosqueReachTime) {
        this.mosqueReachTime = mosqueReachTime;
    }

    public int getAdvOrMosImage() {
        return advOrMosImage;
    }

    public void setAdvOrMosImage(int advOrMosImage) {
        this.advOrMosImage = advOrMosImage;
    }
}
