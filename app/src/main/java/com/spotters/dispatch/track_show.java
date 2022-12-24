package com.spotters.dispatch;

public class track_show {
    private String rider_name;

    public String getReference() {
        return reference;
    }

    private String reference;
    private String rider_phone;


    public String getRider_name() {
        return rider_name;
    }

    public String getRider_phone() {
        return rider_phone;
    }

    public track_show(String rider_name, String rider_phone, String reference) {
        this.rider_name = rider_name;
        this.rider_phone = rider_phone;
        this.reference = reference;
    }
}
