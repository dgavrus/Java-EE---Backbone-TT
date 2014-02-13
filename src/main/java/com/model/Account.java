package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.JSONObject;

public class Account {

    public Account(int id, long moneyAmount, int userId, int accountNumber,
                   boolean isLocked, boolean isActivated){
        this.id = id;
        this.moneyAmount = moneyAmount;
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.status = Status.Active;
    }

    public Account(){

    }

    private int id;

    private long moneyAmount;

    private int userId;

    private int accountNumber;

    private Status status;

    public enum Status {New, Active, Blocked};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(long moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    @JsonIgnore
    public boolean isActive(){
        return status == Status.Active;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    @JsonIgnore
    public JSONObject getJSONObject(){
        JSONObject result = new JSONObject();
        result.put("AccountNumber", getAccountNumber());
        result.put("MoneyAmount", getMoneyAmount());
        result.put("UserId", getUserId());
        result.put("Status", getStatus());
        return result;
    }
}
