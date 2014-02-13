package com.dao;

import com.model.Account;
import org.json.JSONArray;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public interface AccountDAOdb {

    /*public void setDataSource(DataSource ds);*/

    public Account getAccount(int accountId);

    public List<Account> listAccounts();

    public List<Account> listAccounts(int page);

    public Integer getAccountsCount();

    public void updateAccount(Account account);

    public void updateAccountStatus(Account account);

    public JSONArray getAccountJSONArray();

}
