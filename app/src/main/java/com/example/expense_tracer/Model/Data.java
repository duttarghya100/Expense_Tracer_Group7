package com.example.expense_tracer.Model;

public class Data {
    private double amount;
    private String type;
    private String note;
    private String id;
    private String date;

    public Data(String id,String type,double amount, String note,  String date) {

        this.id = id;
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.date = date;
    }

    public Data() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
