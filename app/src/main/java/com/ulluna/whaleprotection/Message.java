package com.ulluna.whaleprotection;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public Message(JSONArray array){
        try {
            this.text = (String)array.get(0);
            this.id = ((String)array.get(1)).equals("user") ? USER_ID : CONSULTANT_ID;
        } catch (Exception e){
            e.printStackTrace();
        }
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
