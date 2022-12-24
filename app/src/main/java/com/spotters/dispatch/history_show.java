package com.spotters.dispatch;

public class history_show {
    private String pickup_location,destination,reference;

    public String getPickup_location() {
        return pickup_location;
    }

    public String getDestination() {
        return destination;
    }

    public String getReference() {
        return reference;
    }

    public history_show(String pickup_location, String destination, String reference) {
        this.pickup_location = pickup_location;
        this.destination = destination;
        this.reference = reference;
    }
}
