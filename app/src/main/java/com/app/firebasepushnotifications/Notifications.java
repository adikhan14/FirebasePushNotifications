package com.app.firebasepushnotifications;

/**
 * Created by Adil khan on 2/22/2018.
 */

public class Notifications {

    String from, message;

    public Notifications(String from, String message) {
        this.from = from;
        this.message = message;
    }

    public Notifications() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "from='" + from + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
