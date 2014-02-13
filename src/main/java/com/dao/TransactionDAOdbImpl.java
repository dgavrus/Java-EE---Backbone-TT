package com.dao;

import com.model.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionDAOdbImpl implements TransactionDAOdb {

    private JdbcTemplate jdbcTemplateObject;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplateObject = jdbcTemplate;
    }

    public List<Transaction> userTransactionList(int accountId) {
        String query = "select * from transactions where source = ? or dest = ?";
        List<Transaction> transactionList = jdbcTemplateObject.query(
                query, new Object[]{accountId, accountId}, new TransactionMapper());
        return transactionList;
    }

    public List<Transaction> userTransactionList(int accountId, int page) {
        String query = "select * from transactions where source = ? or dest = ? limit " + ((page - 1) * 10) + ", 10";
        List<Transaction> transactionList = jdbcTemplateObject.query(
                query, new Object[]{accountId, accountId}, new TransactionMapper());
        return transactionList;
    }

    public int getUserTransactionsCount(int accountId) {
        String query = "select count(*) from transactions where source = ? or dest = ?";
        int rowsCount = jdbcTemplateObject.queryForObject(query, Integer.class, new Object[]{accountId, accountId});
        return rowsCount;
    }

    public void addTransaction(Transaction t) {
        String query = "insert into transactions (source, dest, moneyamount, date) values(?, ?, ?, ?)";
        jdbcTemplateObject.update(query,
                new Object[]{
                        t.getSourceAccountId(),
                        t.getDestAccountId(),
                        t.getMoneyAmount(),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(t.getDate())});
    }

    public JSONArray getTransactionJSONArray(int accountId){
        JSONArray result = new JSONArray();
        for(Transaction transaction : userTransactionList(accountId)){
            JSONObject transactionJSON = transaction.getJSONObject();
            result.put(transactionJSON);
        }
        return result;
    }

}
