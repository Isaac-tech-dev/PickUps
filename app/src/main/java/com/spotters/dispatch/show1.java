package com.spotters.dispatch;

public class show1 {
    private String pickup, destination, amount;
    //private int amount;

    public String getAmount() {
        return amount;
    }

    public String getPickup() {
        return pickup;
    }

    public String getDestination() {
        return destination;
    }

    public show1(String pickup, String destination, String amount) {
        this.amount = amount;
        this.pickup = pickup;
        this.destination = destination;
    }
}
