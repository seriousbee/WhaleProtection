package com.ulluna.whaleprotection;

/**
 * Created by tomaszczernuszenko on 04/03/2017.
 */

public class Message {

    public static final int USER_ID = 1;
    public static final int CONSULTANT_ID = 2;


    String text;
    String timestamp;
    int id;

    public Message(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }
}
