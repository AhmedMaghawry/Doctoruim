package com.ezzat.doctoruim.Model;

import java.io.Serializable;

public class Message implements Serializable {

    private User owner;
    private String message;
    private String id;
    private  String reportedUserId;
    public Message(){

    }



    public Message(User owner, String message, String reportedUserId) {
        this.owner = owner;
        this.message = message;
        this.id = "";
        this.reportedUserId=reportedUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(String reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

}
