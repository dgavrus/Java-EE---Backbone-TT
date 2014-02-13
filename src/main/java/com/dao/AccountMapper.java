package com.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.model.Account;
import com.model.User;
import org.springframework.jdbc.core.RowMapper;

public class AccountMapper implements RowMapper<Account> {

    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        Account account = new Account();
        account.setId(rs.getInt("id"));
        account.setAccountNumber(rs.getInt("accountNumber"));
        account.setStatus(statusConverter(rs.getString("status")));
        account.setMoneyAmount(rs.getLong("moneyAmount"));
        account.setUserId(rs.getInt("userId"));
        return account;
    }

    private Account.Status statusConverter(String status){
        if(status.equals("New")){
            return Account.Status.New;
        } else if(status.equals("Active")){
            return Account.Status.Active;
        } else return Account.Status.Blocked;
    }
}