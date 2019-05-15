package com.example.myapplication2;

public class Message implements java.io.Serializable {
    public static enum Message_Type {Bitis, Dur, Geri, Ileri, Name, None, RivalConnected, Sag, Selected, Sol, Start, Text}

    public Message.Message_Type type;
    public Object content;

    public Message(Message.Message_Type t) {
        this.type = t;
    }
}
