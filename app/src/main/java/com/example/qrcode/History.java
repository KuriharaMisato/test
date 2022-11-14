package com.example.qrcode;

import java.util.Date;

public class History {
    public int id;
    public String content, strDate, color;
    public Date date;

    public History() {
    }

    public History(int id, String content, String strDate, String color, Date date) {
        this.id = id;
        this.content = content;
        this.strDate = strDate;
        this.color = color;
        this.date = date;
    }
}
