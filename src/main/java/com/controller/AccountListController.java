package com.controller;

import com.model.Account;
import com.model.ClientAccount;
import com.model.PaginationInfo;
import com.model.Transaction;
import com.service.AccountService;
import com.service.PaginationService;
import com.service.TransactionService;
import com.service.UserService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/rest/userslist")
@RestController
public class AccountListController {

    private final String url = "/rest/userslist/pagination";

    @Autowired
    AccountService accountService;

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    PaginationService paginationService;

    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    public
    List<ClientAccount> getUsersList(HttpServletRequest request) throws JSONException, IOException {
        int pageNumber;
        try {
            pageNumber = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e){
            pageNumber = 1;
        }
        return accountService.listClientAccounts(pageNumber, pagination.getRowsPerPage());
    }

    @RequestMapping(value = "/pagination", method = RequestMethod.GET)
    public PaginationInfo pagination(HttpServletRequest request){
        Map parameters = request.getParameterMap();
        HashMap<String, Integer> paginationParams = paginationService.parsePaginationParams(parameters);
        int rowsPerPage = paginationParams.get(paginationService.ROWS_PER_PAGE_PARAM);
        int pagesForView = paginationParams.get(paginationService.PAGES_FOR_VIEW_PARAM);
        this.pagination = paginationService.updatePagination(pagination, rowsPerPage, pagesForView, url);
        return this.pagination;
    }

    @RequestMapping(value = "/pagination", method = RequestMethod.POST)
    public void paginationPost(@RequestBody PaginationInfo pagination){
        this.pagination = pagination;
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public void updateAccountStatus(@RequestBody Account account){
        accountService.updateAccountStatus(account);
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.GET, produces = "application/json")
    public
    List<Transaction> getUserTransactions(HttpServletRequest request){
        int accountNumber = Integer.parseInt(request.getParameter("accountNumber"));
        List<Transaction> result = transactionService.userTransactionListDesc(accountNumber, 5);
        return result;
    }

    private PaginationInfo pagination;

}
