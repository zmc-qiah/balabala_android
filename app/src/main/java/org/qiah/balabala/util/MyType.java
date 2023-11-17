package org.qiah.balabala.util;

public abstract class MyType implements MultipleType{
    public static final int MAIN_NIKKE = 1018;
    public static final int NIKKE = 1019;
    public static final int CHAT_LEFT_TEXT = 1020;
    public static final int CHAT_RIGHT_TEXT = 1021;
    public static final int CHAT_RIGHT_IMAGE = 1022;
    public static final int CHAT_LEFT_IMAGE = 1023;
    public static final int ADD_NIKKE = 1024;
    public static final int CHAT_SPLIT_LINE = 1025;
    public static final int CHAT_NARRATION = 1026;
    public String data = null;

    public MyType() {
    }

    public MyType(String data) {
        this.data = data;
    }
}
