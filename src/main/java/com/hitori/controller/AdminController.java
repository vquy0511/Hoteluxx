package com.hitori.controller;

import com.hitori.dao.*;
import com.hitori.entity.Account;
import com.hitori.entity.Authority;
import com.hitori.entity.Role;
import com.hitori.service.AccountService;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    AccountService accountService;

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    RoomTypeDAO roomTypeDAO;

    @Autowired
    BookingDAO bookingDAO;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    HttpSession session;

    @Autowired
    RoleDAO roleDAO;

    @Autowired
    RoomDAO roomDAO;

    @Autowired
    AuthorityDAO authorityDAO;

    @GetMapping("/adminlogout")
    public String doGetLogout() {
        session.removeAttribute("accounts");
        return "user/index";
    }

    @RequestMapping("/adregister")
    public String Showadregister(Model model) {
        return "admin/register";
    }

    @PostMapping("/addAD")
    public String doAddStaff(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String repass,
                             @RequestParam String phone,
                             @RequestParam String fullname,
                             @RequestParam String idcard, Model model) {
        if (username.isEmpty()) {
            model.addAttribute("message", "Email cannot be empty!");
            return "admin/register";
        }
        if (password.isEmpty()) {
            model.addAttribute("message", "Password cannot be empty!");
            return "admin/register";
        }

        if (!password.equals(repass)) {
            model.addAttribute("message", "Passwords do not match!");
            return "admin/register";
        }

        if (password.length() < 6) {
            model.addAttribute("message", "Password must be at least 6 characters!");
            return "admin/register";
        }

        if (!idcard.matches("\\d{9}")) {
            model.addAttribute("message", "ID card number must have at least 9 digits!");
            return "admin/register";
        }

        if (!phone.matches("\\d{10}")) {
            model.addAttribute("message", "\n" +
                    "Phone number must be at least 10 digits!");
            return "admin/register";
        }
        Optional<Account> accounts = accountService.findByEmail(username);
        if (accounts.isPresent()) {
            model.addAttribute("message", "Account already exists!!");
            return "admin/register";
        } else if (password.equals(repass)) {
            String encodedPassword = passwordEncoder.encode(password);
            Account account = new Account();
            account.setUsername(username);
            account.setPassword(password);
            account.setFullname(fullname);
            account.setPhone(phone);
            account.setIdcard(idcard);
            accountDAO.save(account);
            Optional<Role> staffRole = roleDAO.findById("STAF");
            if (staffRole.isPresent()) {
                Authority authority = new Authority();
                authority.setAccount(account); // Gắn tài khoản cho authority
                authority.setRole(staffRole.get()); // Gán Role "CUST" cho Authority
                staffRole.get().getAuthorities().add(authority);
                roleDAO.save(staffRole.get());
                return "redirect:/stafflist";
            }else {
                return "admin/register";
            }
        } else {
            model.addAttribute("message", "Password is not correct");
            return "admin/register";
        }
    }


    @RequestMapping("/stafflist")
    public String showStaffList(Model model) {
        model.addAttribute("accounts", accountDAO.findAll());
        return "admin/adminlist";
    }

    @GetMapping("/edit/{username}")
    public String doGetEditProfile(@PathVariable("username") String username, Model model) {
        Account account = accountService.findById(username);
        model.addAttribute("account", account);
        return "admin/edit";
    }

    @PostMapping("/edit")
    public String doPostEditProfile(@ModelAttribute("account") @Validated Account updatedAccount,
                                    BindingResult result) {
        if (result.hasErrors()) {
            return "admin/edit";
        }
        accountDAO.save(updatedAccount);
        return "redirect:/stafflist";
    }

    @GetMapping("/del/{username}")
    public String doDelStaff(@PathVariable("username") String username, Model model) {
        Account account = accountService.findById(username);
        model.addAttribute("account", account);
        account.setAvailable(false);
        accountDAO.save(account);
        return "redirect:/stafflist";
    }

    @GetMapping("/admin/home/index")
    public String countData(Model model) {
        Long bookingCount = bookingDAO.countPaidBookings();
        Long userCount = authorityDAO.countAuthoritiesByRoleCus();
        int roomCount = roomDAO.countRoomCheckIn();
        int roomTypeCount = roomTypeDAO.countAllRoomTypes();
        model.addAttribute("bookingCount", bookingCount);
        model.addAttribute("userCount", userCount);
        model.addAttribute("roomCount", roomCount);
        model.addAttribute("roomTypeCount", roomTypeCount);
        return "admin/adminIndex";
    }
}
