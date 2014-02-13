package com.dao;

import com.model.Account;
import com.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountDAOdbImpl implements AccountDAOdb {

    @Autowired
    UserDAOdbImpl userDAOdb;

    private JdbcTemplate jdbcTemplateObject;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplateObject = jdbcTemplate;
    }

    public Account getAccount(int accountId){
        String query = "select * from accounts where accountNumber = " + accountId;
        Account account = jdbcTemplateObject.queryForObject(query, new AccountMapper());
        return account;
    }

    public List<Account> listAccounts(){
        String query = "select * from accounts";
        return (ArrayList<Account>) jdbcTemplateObject.query(query, new AccountMapper());
    }

    public Integer getAccountsCount() {
        String query = "select count(*) from accounts";
        int rowsCount = jdbcTemplateObject.queryForObject(query, Integer.class);
        return rowsCount;
    }

    public List<Account> listAccounts(int page) {
        String query = "select * from accounts limit " + ((page - 1) * 10) + ", 10";
        List<Account> accountList = jdbcTemplateObject.query(query, new AccountMapper());
        return accountList;
    }

    public void updateAccount(Account account){
        String query = "update accounts set moneyAmount=" + account.getMoneyAmount() + " where id=" + account.getId();
        jdbcTemplateObject.update(query);
    }

    public JSONArray getAccountJSONArray(){
        JSONArray result = new JSONArray();
        for(Account account : listAccounts()){
            JSONObject accountJSON = account.getJSONObject();
            User owner = userDAOdb.getUserById(account.getUserId());
            accountJSON.put("OwnerName", owner.getFirstName() + " " + owner.getLastName());
            accountJSON.remove("UserId");
            result.put(accountJSON);
        }
        return result;
    }

    public void updateAccountStatus(Account account){
        String query = "update accounts set status=\"" + account.getStatus() + "\" where accountNumber=" + account.getAccountNumber();
        jdbcTemplateObject.update(query);
    }

}
