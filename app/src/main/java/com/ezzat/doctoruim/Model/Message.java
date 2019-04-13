package com.ezzat.doctoruim.Model;

import java.io.Serializable;

public class Message implements Serializable{

    private User owner, reported;
    private MessageStatus status;
    private String content;

    public Message(){}

    public Message(User owner, User reported, String content) {
        this.owner = owner;
        this.reported = reported;
        this.status = MessageStatus.UNREAD;
        this.content = content;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getReported() {
        return reported;
    }

    public void setReported(User reported) {
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
}
