package com.hitori.service.impl;

import com.hitori.dao.AccountDAO;
import com.hitori.dao.AuthorityDAO;
import com.hitori.entity.Account;
import com.hitori.entity.Authority;
import com.hitori.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    AuthorityDAO dao;
    @Autowired
    AccountDAO accountDAO;

    public List<Authority> findAll() {
        return dao.findAll();
    }

    public Authority create(Authority auth) {
        return dao.save(auth);
    }

    public void delete(Integer id) {
        dao.deleteById(id);
    }

    public List<Authority> findAuthoritiesOfAdministrators() {
        List<Account> accounts = accountDAO.getAdministrators();
        return dao.authoritiesOf(accounts);
    }

    public Long getCountAuthoritiesByRoleCus() {
        return dao.countAuthoritiesByRoleCus();
    }
}
