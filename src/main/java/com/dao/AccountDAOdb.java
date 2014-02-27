package com.dao;

import com.model.Account;
import com.model.ClientAccount;
import org.json.JSONArray;

import java.util.List;

public interface AccountDAOdb {

    public Account getAccount(int accountId);

    public List<Account> listAccounts();

    public List<Account> listAccounts(int page, int count);

    public List<ClientAccount> listClientAccounts(int page, int count);

    public Integer getAccountsCount();

    public void updateAccount(Account account);

    public void updateAccountStatus(Account account);

    public JSONArray getAccountJSONArray();

}
