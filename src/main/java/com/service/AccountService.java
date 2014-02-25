package com.service;

import com.dao.AccountDAOdb;
import com.model.Account;
import com.model.ClientAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountDAOdb accountDAOdb;

    public Account getAccount(int accountId){
        return accountDAOdb.getAccount(accountId);
    }

    public List<ClientAccount> listClientAccounts(int pageNumber, int count){
        return accountDAOdb.listClientAccounts(pageNumber, count);
    }

    public int getAccountsCount(){
        return accountDAOdb.getAccountsCount();
    }

    public void updateAccountStatus(Account account){
        accountDAOdb.updateAccountStatus(account);
    }

}
