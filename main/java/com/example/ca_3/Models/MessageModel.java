package com.example.ca_3.Models;

public class MessageModel {
    //userid ,usser message
    String uId;
    String message,messageId;
    //updated time
    Long timestamp;
//3 parameter(constructor->generate)
    public MessageModel(String uId, String message, Long timestamp) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
    }
//2 parameter(constructor->generate)
    public MessageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }
//create model
    public MessageModel(){
    }
//getter and setter method of messageId
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    //3 method create of getter and setter(generate)
    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


}
