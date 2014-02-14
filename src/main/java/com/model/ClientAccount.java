package com.model;

public class ClientAccount extends Account {

    public ClientAccount(){

    }

    public ClientAccount(int id, long moneyAmount, int userId, int accountNumber, Status status,
                         String ownerFirstName, String ownerLastName) {
        super(id, moneyAmount, userId, accountNumber, status);
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
    }

    public ClientAccount(Account account, User user){
        super(account.getId(), account.getMoneyAmount(), account.getUserId(), account.getAccountNumber(), account.getStatus());
        this.ownerFirstName = user.getFirstName();
        this.ownerLastName = user.getLastName();
    }
    public ClientAccount(Account account, String ownerFirstName, String ownerLastName){
        super(account.getId(), account.getMoneyAmount(), account.getUserId(), account.getAccountNumber(), account.getStatus());
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
    }

    private String ownerFirstName;

    private String ownerLastName;

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

}
