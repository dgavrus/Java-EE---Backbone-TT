package com.service;

import com.dao.TransactionDAOdb;
import com.model.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaginationService {

    public static final String ROWS_PER_PAGE_PARAM = "rowsPerPage";

    public static final String PAGES_FOR_VIEW_PARAM = "pagesForView";

    public static final String MAX_PAGES = "7";

    public static final String DEFAULT_ROWS_PER_PAGE = "10";

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

    public PaginationInfo updatePagination(PaginationInfo pagination, int rowsPerPage, int pagesForView, String url){
        int maxPages = Integer.parseInt(MAX_PAGES);
        int defaultDowsPerPage = Integer.parseInt(DEFAULT_ROWS_PER_PAGE);
        if(pagesForView < 1){
            pagesForView = maxPages;
        }
        if(rowsPerPage < 1){
            rowsPerPage = defaultDowsPerPage;
        }
        int pages = getPagesCount(url, rowsPerPage);
        if(pagination == null){
            pagination = new PaginationInfo(pages, 1, Math.min(Math.min(pages, pagesForView), maxPages), rowsPerPage, url);
        }
        pagination.setPagesForView(Math.min(Math.min(pages, pagesForView), maxPages));
        pagination.setPagesCount(pages);
        pagination.setRowsPerPage(rowsPerPage);
        return pagination;
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
