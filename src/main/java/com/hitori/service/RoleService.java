package com.hitori.service;

import com.hitori.entity.Account;
import com.hitori.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    public List<Role> findAll();
}
