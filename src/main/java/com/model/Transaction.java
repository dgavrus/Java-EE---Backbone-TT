package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.utils.DateSerializer;
import org.joda.time.DateTime;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {

    public Transaction(int from, int to, long money, DateTime date){
        this.sourceAccountId = from;
        this.destAccountId = to;
        this.moneyAmount = money;
        this.date = date;
    }

    public Transaction(){

    }

    private int sourceAccountId;

    private int destAccountId;

    private DateTime date;

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
    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
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
        json.put("date", new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(getDate()));
        return json;
    }
}
