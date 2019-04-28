package com.ezzat.doctoruim.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.ezzat.doctoruim.Control.Utils.Constants.DOCTOR_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Constants.MESSAGE_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;

public class Message implements Serializable{

    private String phone, reported;
    private MessageStatus status;
    private String content;

    public Message(){}

    public Message(String phone, String reported, String content) {
        this.phone = phone;
        this.reported = reported;
        this.status = MessageStatus.UNREAD;
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReported() {
        return reported;
    }

    public void setReported(String reported) {
        this.reported = reported;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", this.phone);
        map.put("reported", this.reported);
        map.put("status", this.status);
        map.put("content", this.content);
        return map;
    }

    public void updateMessage() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> userValues = toMap();
        childUpdates.put("/" + getPhone(), userValues);
        mDatabase.getReference(MESSAGE_TABLE).updateChildren(childUpdates);
    }

    public boolean addMessage() {
        try {
            DatabaseReference base = FirebaseDatabase.getInstance().getReference(MESSAGE_TABLE);
            base.child(phone).setValue(this);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
