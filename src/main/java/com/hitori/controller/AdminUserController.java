package com.hitori.controller;

import com.hitori.dao.AccountDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminUserController {
    @Autowired
    AccountDAO accountDAO;

    @GetMapping("/userlist")
    public String showUserList(Model model){
        model.addAttribute("Users", accountDAO.CustomersWithRoleCUST());
        return "admin/listuser";
    }
}
