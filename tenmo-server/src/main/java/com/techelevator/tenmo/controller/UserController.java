package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("user")
public class UserController
{
    UserDao userDao;
    AccountDao accountDao;

    @Autowired
    public UserController(UserDao userDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @GetMapping("{username}")
    public Account getAccount(@PathVariable String username)
    {
        List<User> users = userDao.findAll();

        for (User user: users)
        {
            if(user.getUsername().equals(username))
            {
                return accountDao.getAccountByUser(user.getId());
            }
        }
        return null;
    }

    @GetMapping("username")
    public List<String> getAllUsernames()
    {
        List<String> usernames = new ArrayList<>();
        List<User> users = userDao.findAll();

       for(User user : users)
       {
           usernames.add(user.getUsername());
       }

        return usernames;
    }
}
