package com.controller;

import com.model.LoginStatus;
import com.model.User;
import com.security.CustomAuthenticationProvider;
import com.service.UserService;
import com.validator.TransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;
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
    LoginStatus login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginStatus loginStatus) {

        //Logout
        if(loginStatus.isLoggedIn() ||
                (loginStatus.getUsername().isEmpty() && loginStatus.getPassword().isEmpty())){
            SecurityContextHolder.getContext().setAuthentication(null);
            return new LoginStatus("sign in please", null, false, null);
        }

        String username = loginStatus.getUsername();
        String password = loginStatus.getPassword();
        loginStatus = customAuthenticationProvider.authenticate(username, password);
        if(loginStatus.getUsername() == null || !loginStatus.isLoggedIn()){
            response.setStatus(500);
        }
        return loginStatus;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public LoginStatus login(){
        String login;
        String role;
        try {
            login = userService.getAuthenticatedUser().getLogin();
            role = userService.getAuthenticatedUser().getAuthority();
        } catch (NullPointerException e){
            login = role = null;
        }
        return new LoginStatus(login, null, login != null, role != null ? User.Role.valueOf(role) : null);
    }

}
