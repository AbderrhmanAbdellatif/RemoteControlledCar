package com.example.myapplication2;


public class Message implements java.io.Serializable {
    public static enum Message_Type {Name,None,Start,Ileri,Geri,Sag,Sol,Dur,RivalConnected,Selected,Bitis ,Text}

    public Message_Type type;
    public Object content;

    public Message(Message_Type t) {
        this.type = t;
    }
}