package com.controller;

import com.dao.*;
import com.model.*;
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
import java.util.List;

@Controller
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

    @RequestMapping(value = "/rest/userslist", method = RequestMethod.GET, produces="application/json")
    public @ResponseBody
    List<ClientAccount> getUsersList(HttpServletRequest request) throws JSONException, IOException {
        int pageNumber;
        try {
            pageNumber = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e){
            pageNumber = 1;
        }
        return accountService.listClientAccounts(pageNumber, pagination.getRowsPerPage());
    }

    @RequestMapping(value = "/rest/userslist/pagination", method = RequestMethod.GET)
    public @ResponseBody PaginationInfo pagination(HttpServletRequest request){
        int rowsPerPage;
        int pagesForView;
        try {
            rowsPerPage = Integer.parseInt(request.getParameter("rowsPerPage"));
            if(rowsPerPage < 1){
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e){
            rowsPerPage = paginationService.DEFAULT_ROWS_PER_PAGE;
        }
        try {
            pagesForView = Integer.parseInt(request.getParameter("pagesForView"));
            if(pagesForView < 1){
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e){
            pagesForView = paginationService.MAX_PAGES;
        }
        int pages = (int)Math.ceil(accountService.getAccountsCount() / (float)rowsPerPage);
        if(this.pagination == null){
            this.pagination = new PaginationInfo(pages, 1, Math.min(Math.min(pages, pagesForView), paginationService.MAX_PAGES), rowsPerPage, url);
        }
        this.pagination.setPagesForView(Math.min(Math.min(pages, pagesForView), paginationService.MAX_PAGES));
        this.pagination.setPagesCount(pages);
        this.pagination.setRowsPerPage(rowsPerPage);
        return this.pagination;
    }

    @RequestMapping(value = "/rest/userslist/pagination", method = RequestMethod.POST)
    public @ResponseBody void paginationPost(@RequestBody PaginationInfo pagination){
        this.pagination = pagination;
    }

    @RequestMapping(value = "/rest/userslist", method = {RequestMethod.POST, RequestMethod.PUT})
    public @ResponseBody void updateAccountStatus(@RequestBody Account account){
        accountService.updateAccountStatus(account);
    }

    @RequestMapping(value = "/rest/userslist/transaction", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Transaction> getUserTransactions(HttpServletRequest request){
        int accountNumber = Integer.parseInt(request.getParameter("accountNumber"));
        List<Transaction> result = transactionService.userTransactionListDesc(accountNumber, 5);
        return result;
    }

    private PaginationInfo pagination;

}
