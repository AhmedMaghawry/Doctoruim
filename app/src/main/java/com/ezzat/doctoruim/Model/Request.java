package com.ezzat.doctoruim.Model;

import java.io.Serializable;

public class Request implements Serializable {

    private User owner;
    private String message;
    private String cardURL, id;

    public Request(){

    }

    public Request(User owner, String message, String cardURL) {
        this.owner = owner;
        this.message = message;
        this.cardURL = cardURL;
        this.id = "";
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

    public String getCardURL() {
        return cardURL;
    }

    public void setCardURL(String cardURL) {
        this.cardURL = cardURL;
    }
}
