package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.utils.DateSerializer;
import org.json.JSONObject;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {

    public Transaction(int from, int to, long money, Date date){
        this.sourceAccountId = from;
        this.destAccountId = to;
        this.moneyAmount = money;
        this.date = date;
    }

    public Transaction(){

    }

    private int sourceAccountId;

    private int destAccountId;

    private Date date;

    private long moneyAmount;

    public int getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(int sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public int getDestAccountId() {
        return destAccountId;
    }

    public void setDestAccountId(int destAccountId) {
        this.destAccountId = destAccountId;
    }

    @JsonSerialize (using = DateSerializer.class)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(long moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @JsonIgnore
    public JSONObject getJSONObject(){
        JSONObject json = new JSONObject();
        json.put("sourceAccountId",getSourceAccountId());
        json.put("destAccountId", getDestAccountId());
        json.put("moneyAmount",getMoneyAmount());
        json.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getDate()));
        return json;
    }
}
