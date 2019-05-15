package com.example.myapplication2;


public class Message implements java.io.Serializable {
    public static enum Message_Type {Text}

    public Message.Message_Type type;
    public Object content;

    public Message(Message.Message_Type t) {
        this.type = t;
    }
}



