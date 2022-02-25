package com.example.smartchat;

public class Messages {

    public String senderID;
    public String receiverID;
    public String Room;
    public String message;
    public long timeStamp;
    public String senderName;
    public String receiverName;



    public Messages()
    {

    }
    public Messages(String message, String senderID, long timeStamp, String receiverID, String senderName, String receiverName) {
        this.message = message;
        this.senderID = senderID;
        this.timeStamp = timeStamp;
        this.receiverID = receiverID;
        this.senderName = senderName;

        this.receiverName = receiverName;
//        this.Room = Room;

    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }



    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

//    public String getRoom() {
//        return Room;
//    }
//
//    public void setRoom(String room) {
//        this.Room = room;
//    }

//    public HashMap<String, String> getDetailed() {
//        return detailed;
//    }
//
//    public void setDetailed(HashMap<String, String> detailed) {
//        this.detailed = detailed;
//    }
}
