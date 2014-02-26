package com.service;

import com.dao.TransactionDAOdb;
import com.model.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Service
public class PaginationService {

    public final String ROWS_PER_PAGE_PARAM = "rowsPerPage";

    public final String PAGES_FOR_VIEW_PARAM = "pagesForView";

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

    public HashMap<String, Integer> parsePaginationParams(HttpServletRequest request){
        int rowsPerPage;
        int pagesForView;
        try {
            rowsPerPage = Integer.parseInt(request.getParameter(ROWS_PER_PAGE_PARAM));
            if(rowsPerPage < 1){
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e){
            rowsPerPage = DEFAULT_ROWS_PER_PAGE;
        }
        try {
            pagesForView = Integer.parseInt(request.getParameter(PAGES_FOR_VIEW_PARAM));
            if(pagesForView < 1){
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e){
            pagesForView = MAX_PAGES;
        }
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        result.put(ROWS_PER_PAGE_PARAM, rowsPerPage);
        result.put(PAGES_FOR_VIEW_PARAM, pagesForView);
        return result;
    }

    public PaginationInfo updatePagination(PaginationInfo pagination, int rowsPerPage, int pagesForView, String url){
        int pages = getPagesCount(url, rowsPerPage);
        if(pagination == null){
            pagination = new PaginationInfo(pages, 1, Math.min(Math.min(pages, pagesForView), MAX_PAGES), rowsPerPage, url);
        }
        pagination.setPagesForView(Math.min(Math.min(pages, pagesForView), MAX_PAGES));
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
