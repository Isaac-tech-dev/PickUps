package com.spotters.dispatch;

public class show1 {
    private String name, pickup, destination, amount;
    //private int amount;

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public String getPickup() {
        return pickup;
    }

    public String getDestination() {
        return destination;
    }

    public show1(String name, String pickup, String destination, String amount) {
        this.name = name;
        this.amount = amount;
        this.pickup = pickup;
        this.destination = destination;
    }
}
