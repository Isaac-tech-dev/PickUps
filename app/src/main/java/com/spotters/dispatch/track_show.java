package com.spotters.dispatch;

public class track_show {
    private String rider_name;
    private String reference;
    private String rider_phone;
    private String sender_address;
    private String receiver_address;
    private String amount;

    public String getReference() {
        return reference;
    }

    public String getRider_name() {
        return rider_name;
    }
    public String getRider_phone() {
        return rider_phone;
    }

    public String getSender_address() {
        return sender_address;
    }

    public String getReceiver_address() {
        return receiver_address;
    }

    public String getAmount() {
        return amount;
    }

    public track_show(String rider_name, String rider_phone, String reference, String sender_address, String receiver_address, String amount) {
        this.reference = reference;
        this.rider_name = rider_name;
        this.rider_phone = rider_phone;
        this.sender_address = sender_address;
        this.receiver_address = receiver_address;
        this.amount = amount;
    }
}
