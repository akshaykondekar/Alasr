package com.trikwetra.alasr.bean;

public class MosqueBean
{
    String mosqueName,mosqueCity;

    public MosqueBean(String mosqueName, String mosqueCity) {
        this.mosqueName = mosqueName;
        this.mosqueCity = mosqueCity;
    }

    public String getMosqueName() {
        return mosqueName;
    }

    public void setMosqueName(String mosqueName) {
        this.mosqueName = mosqueName;
    }

    public String getMosqueCity() {
        return mosqueCity;
    }

    public void setMosqueCity(String mosqueCity) {
        this.mosqueCity = mosqueCity;
    }
}
