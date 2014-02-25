package com.service;

import com.dao.TransactionDAOdb;
import com.model.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class PaginationService {

    public final int MAX_PAGES = 7;

    public final int DEFAULT_ROWS_PER_PAGE = 10;

    private final String TRANSACTION_URL = "/rest/transaction/pagination";

    private final String ACCOUNTS_URL = "/rest/userslist/pagination";

    @Autowired
    TransactionDAOdb transactionDAOdb;

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserService userService;

    public int getUserTransactionsCount(int accountId){
        return transactionDAOdb.getUserTransactionsCount(accountId);
    }

    private Integer getPagesCount(String url, int rowsPerPage){
        if(url.equals(TRANSACTION_URL)){
            return (int)Math.ceil(getUserTransactionsCount(userService.getAuthenticatedUser().getAccountId()) / (float)rowsPerPage);
        } else if(url.equals(ACCOUNTS_URL)){
            return (int)Math.ceil(accountService.getAccountsCount() / (float)rowsPerPage);
        } else {
            return null;
        }
    }



}
