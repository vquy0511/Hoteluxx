package com.hitori.service;

import com.hitori.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    public List<Account> findAll() ;
    public Account findById(String username) ;
    public List<Account> getAdministrators() ;
    Optional<Account> findByEmail(String username);
    public void delete(Account account) ;
    public List<Account> CustomersWithRoleCUST() ;
}
