package com.trikwetra.alasr.bean;

import com.google.android.gms.maps.model.LatLng;

public class Place {
    public String name;
    public LatLng latlng;

    public Place(String name, LatLng latlng) {
        this.name = name;
        this.latlng = latlng;
    }
}
