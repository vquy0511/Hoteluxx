package com.hitori.service;

import com.hitori.entity.Authority;

import java.util.List;

public interface AuthorityService {
    public List<Authority> findAll() ;

    public Authority create(Authority auth) ;

    public void delete(Integer id) ;

    public List<Authority> findAuthoritiesOfAdministrators() ;
}
