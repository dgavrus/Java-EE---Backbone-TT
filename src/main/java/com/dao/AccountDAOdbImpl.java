package com.dao;

import com.model.Account;
import com.model.ClientAccount;
import com.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountDAOdbImpl extends AbstactDAOImpl implements AccountDAOdb {

    @Autowired
    UserDAOdbImpl userDAOdb;

    public Account getAccount(int accountId){
        String query = "select * from accounts where accountNumber = ?";
        Account account;
        try {
            account = jdbcTemplateObject.queryForObject(query, new Object[]{accountId}, new AccountMapper());
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        return account;
    }

    public List<Account> listAccounts(){
        String query = "select * from accounts";
        return jdbcTemplateObject.query(query, new AccountMapper());
    }

    public Integer getAccountsCount() {
        String query = "select count(*) from accounts";
        int rowsCount = jdbcTemplateObject.queryForObject(query, Integer.class);
        return rowsCount;
    }

    public List<Account> listAccounts(int page, int count) {
        String query = "select * from accounts limit ?, ?";
        List<Account> accountList = jdbcTemplateObject.query(query, new Object[]{(page - 1) * count, count}, new AccountMapper());
        return accountList;
    }

    public List<ClientAccount> listClientAccounts(int page, int count) {
        String query = "select a.*, u.firstname, u.lastname from accounts as a " +
                "inner join users as u on a.accountNumber=u.accountid " +
                "limit ?, ?";
        List<ClientAccount> clientAccountsList = jdbcTemplateObject.query(query, new Object[]{(page - 1) * count, count},
                new ClientAccountMapper());
        return clientAccountsList;
    }

    public void updateAccount(Account account){
        String query = "update accounts set moneyAmount=? where id=?";
        jdbcTemplateObject.update(query, new Object[]{account.getMoneyAmount(), account.getId()});
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
        String query = "update accounts set status=? where accountNumber=?";
        jdbcTemplateObject.update(query, new Object[]{String.valueOf(account.getStatus()), account.getAccountNumber()});
    }

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

    public class ClientAccountMapper implements RowMapper<ClientAccount> {

        public ClientAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
            ClientAccount account = new ClientAccount(new AccountMapper().mapRow(rs, rowNum),
                    rs.getString("firstname"), rs.getString("lastname"));
            return account;
        }
    }


}
