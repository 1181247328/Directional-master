package com.cdqf.dire_txmap;

/**
 * Created by liu on 2018/7/4.
 */

public class LoadFind {
    public String load;

    public int i;

    public double longitude;

    public double latitude;

    public String address;

    public LoadFind(String load, int i) {
        this.load = load;
        this.i = i;
    }

    public LoadFind(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public LoadFind(double longitude, double latitude, String address) {
        this.load = load;
        this.i = i;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
    }
}
