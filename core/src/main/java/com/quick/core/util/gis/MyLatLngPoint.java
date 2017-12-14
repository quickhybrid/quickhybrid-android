package com.quick.core.util.gis;

/**
 * Created by dailichun on 2017/12/7.
 * 坐标
 */
public class MyLatLngPoint {
    private double lat, lng;

    public MyLatLngPoint(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
