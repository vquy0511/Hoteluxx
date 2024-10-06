package com.hitori.service.impl;

import com.hitori.dao.AccountDAO;
import com.hitori.entity.Account;
import com.hitori.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountDAO dao;

    public List<Account> findAll() {
        return dao.findAll();
    }

    public Account findById(String username) {
        return dao.findById(username).get();
    }

    public List<Account> getAdministrators() {
        return dao.getAdministrators();
    }

    @Override
    public Optional<Account> findByEmail(String username) {
        return dao.findById(username);
    }

    @Override
    public void delete(Account account) {
        dao.delete(account);
    }

    @Override
    public List<Account> CustomersWithRoleCUST() {
        return dao.CustomersWithRoleCUST();
    }
}
