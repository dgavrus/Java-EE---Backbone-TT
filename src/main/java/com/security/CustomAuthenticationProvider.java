package com.security;

import com.dao.UserDAOdb;
import com.model.LoginStatus;
import com.model.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserDAOdb userDAOdb;

    @Autowired
    UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userDAOdb.getUserByLogin(authentication.getName());
        if(user == null){
            throw new BadCredentialsException("Username not found");
        }
        if(!user.getPassword().equals(authentication.getCredentials())){
            throw new BadCredentialsException("Wrong password");
        }
        Collection<User> authorities = new ArrayList<User>();
        authorities.add(user);
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getLogin(),
                        user.getPassword(), authorities);
        return token;
    }

    private LoginStatus authenticate(String username, String password){
        User user;
        user = userDAOdb.getUserByLogin(username);
        if(user == null){
            return new LoginStatus(null, null, false, null);
        }
        else if(!user.getPassword().equals(password)){
            return new LoginStatus(user.getLogin(), null, false, null);
        }
        Collection<User> authorities = new ArrayList<User>();
        authorities.add(user);
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username,
                        password, authorities);
        SecurityContextHolder.getContext().setAuthentication(token);
        return new LoginStatus(user.getLogin(), null, true, user.getRole());
    }

    public LoginStatus authenticate(LoginStatus loginStatus){
        return authenticate(loginStatus.getUsername(), loginStatus.getPassword());
    }

    public LoginStatus getLoginStatus(){
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

    public void logout(){
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public LoginStatus checkLoginStatus(LoginStatus loginStatus){
        if(loginStatus.isLoggedIn() ||
                (loginStatus.getUsername().isEmpty() && loginStatus.getPassword().isEmpty())){
                    logout();
            return new LoginStatus("sign in please", null, false, null);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
