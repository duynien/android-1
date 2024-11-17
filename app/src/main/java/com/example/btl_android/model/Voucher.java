package com.example.btl_android.model;

import java.io.Serializable;

public class Voucher implements Serializable {
    private int id;
    private String code, title, percentage, start, end;
    public Voucher(){

    }

    public Voucher(String code, String title, String percentage, String start, String end) {
        this.code = code;
        this.title = title;
        this.percentage = percentage;
        this.start = start;
        this.end = end;
    }

    public Voucher(int id, String code, String title, String percentage, String start, String end) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.percentage = percentage;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", percentage='" + percentage + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
