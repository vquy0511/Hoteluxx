package com.hitori.service.impl;

import com.hitori.dao.RoleDAO;
import com.hitori.entity.Role;
import com.hitori.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDAO dao;

    public List<Role> findAll() {
        return dao.findAll();
    }
}
