package com.dao;

import com.model.Transaction;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public interface TransactionDAOdb {

    public List<Transaction> userTransactionList(int accountId);

    public List<Transaction> userTransactionList(int accountId, int page, int count);

    public List<Transaction> userTransactionList(int accountId, int count);

    public int getUserTransactionsCount(int accountId);

    public void addTransaction(Transaction t);

}
