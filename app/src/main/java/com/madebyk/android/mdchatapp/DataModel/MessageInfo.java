package com.madebyk.android.mdchatapp.DataModel;

class MessageInfo {

    private String sender, receiver, message;
    private Long timeStamp;

    MessageInfo(String to, String from, String text, Long time){
        sender = to;
        receiver = from;
        message = text;
        timeStamp = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
