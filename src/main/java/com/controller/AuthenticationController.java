package com.controller;

import com.model.LoginStatus;
import com.model.User;
import com.security.CustomAuthenticationProvider;
import com.service.UserService;
import com.validator.TransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping(value = "/rest/login")
@RestController
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    TransactionValidator transactionValidator;

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT},
            consumes = "application/json")
    public
    ResponseEntity<LoginStatus> login(@RequestBody LoginStatus loginStatus) {
        //Logout
        if(loginStatus.isLoggedIn() ||
                (loginStatus.getUsername().isEmpty() && loginStatus.getPassword().isEmpty())){
            customAuthenticationProvider.logout();
            return new ResponseEntity<>(new LoginStatus("sign in please", null, false, null), HttpStatus.OK);
        }
        loginStatus = customAuthenticationProvider.authenticate(loginStatus);
        if(loginStatus.getUsername() == null || !loginStatus.isLoggedIn()){
            return new ResponseEntity<>(loginStatus, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(loginStatus, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public LoginStatus login(){
        return customAuthenticationProvider.getLoginStatus();
    }

}
