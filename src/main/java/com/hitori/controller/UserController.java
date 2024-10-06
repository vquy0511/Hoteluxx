package com.hitori.controller;

import com.hitori.dao.AccountDAO;
import com.hitori.dao.AuthorityDAO;
import com.hitori.dao.RoleDAO;
import com.hitori.entity.Account;
import com.hitori.entity.Authority;
import com.hitori.entity.MailInfo;
import com.hitori.entity.Role;
import com.hitori.service.AccountService;
import com.hitori.service.MailerService;
import com.hitori.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    AccountService accountService;

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    HttpSession session;

    @Autowired
    MailerService sendmail;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthorityDAO authorityDAO;

    @Autowired
    RoleDAO roleDAO;


    @GetMapping("/register/form")
    public String showRegisterForm() {
        return "user/register";
    }
    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String repass,
                         @RequestParam String card,
                         @RequestParam String fullname,
                         @RequestParam String phone, Model model) {

    // Kiểm tra username không rỗng
    if (username.isEmpty()) {
        model.addAttribute("message", "Email cannot be empty!");
        return "user/register";
    }

    // Kiểm tra password không rỗng
    if (password.isEmpty()) {
        model.addAttribute("message", "Password cannot be empty!");
        return "user/register";
    }

    // Kiểm tra password và repass khớp nhau
    if (!password.equals(repass)) {
        model.addAttribute("message", "Passwords do not match!");
        return "user/register";
    }

    // Kiểm tra độ dài password tối thiểu là 8 ký tự
    if (password.length() < 6) {
        model.addAttribute("message", "Password must be at least 6 characters!");
        return "user/register";
    }


    if (!card.matches("\\d{9}")) {
        model.addAttribute("message", "ID card number must have at least 9 digits!");
        return "user/register";
    }

    // Kiểm tra phone có 10 ký tự số
    if (!phone.matches("\\d{10}")) {
        model.addAttribute("message", "Phone number must have at least 10 digits!");
        return "user/register";
    }

    // Kiểm tra username đã tồn tại
    Optional<Account> account = accountDAO.findById(username);
    if (account.isPresent()) {
        model.addAttribute("message", "Account already exists!");
        return "user/register";
    } else if (password.equals(repass)) {
        String encodedPassword = passwordEncoder.encode(password);
        Account acc = new Account();
        acc.setUsername(username);
        acc.setPassword(encodedPassword);
        acc.setFullname(fullname);
        acc.setIdcard(card);
        acc.setPhone(phone);
        acc.setAvailable(true);
        accountDAO.save(acc);
        Optional<Role> custRole = roleDAO.findById("CUST");
        if (custRole.isPresent()) {
            Authority authority = new Authority();
            authority.setAccount(acc); // Gắn tài khoản cho authority
            authority.setRole(custRole.get()); // Gán Role "CUST" cho Authority
            custRole.get().getAuthorities().add(authority); // Gắn authority vào danh sách authorities của Role
            authorityDAO.save(authority); // Lưu authority vào bảng Authorities
            roleDAO.save(custRole.get()); // Lưu Role "CUST" với danh sách authorities mới
            model.addAttribute("message", "Sign Up Success!");
            return "security/login";
        } else {
            model.addAttribute("message", "Không tìm thấy Role 'CUST'");
            return "user/register";
        }
    }
        else{
            model.addAttribute("message", "Mật khẩu chưa chính xác");
            return "user/register";
        }
    }

    @RequestMapping("/forgot/form")
    public String showForgot(){
        return "user/forgot";
    }

    @PostMapping("/forgot")
    public String doForgot(Model model, @RequestParam String username){
        Optional<Account> account = accountService.findByEmail(username);
        if (!account.isPresent()) {
            model.addAttribute("message", "\n" +
                    "Account does not exist");
            return "user/register";
        } else {
            String newPass = ((long) Math.floor(Math.random() * (999999999 - 100000000 + 1) + 100000000)) + "";
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(newPass);
            account.get().setPassword(encodedPassword);
            accountDAO.save(account.get());
            try {
                MailInfo mail = new MailInfo();
                mail.setFrom("hotelhitori@gmail.com");
                mail.setTo(username);
                mail.setSubject("Forgot Password");
                mail.setBody("<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" width=\"100%\" bgcolor=\"#F0F2FC\">\r\n"
                        + "        <tbody>\r\n"
                        + "            <tr>\r\n"
                        + "                <td align=\"center\" valign=\"top\">\r\n"
                        + "                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" width=\"600\" class=\"m_-4516102534817492810container\" style=\"margin:0 auto\">\r\n"
                        + "                        <tbody>\r\n"
                        + "                            <br>\r\n"
                        + "                            <tr>\r\n"
                        + "                                <td valign=\"top\" bgcolor=\"#FFFFFF\">\r\n"
                        + "                                    <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"min-width:100%\" class=\"m_-4516102534817492810stylingblock-content-wrapper\">\r\n"
                        + "                                        <tbody>\r\n"
                        + "                                            <tr>\r\n"
                        + "                                                <td class=\"m_-4516102534817492810stylingblock-content-wrapper\">\r\n"
                        + "                                                    <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\">\r\n"
                        + "                                                        <tbody>\r\n"
                        + "                                                            <tr>\r\n"
                        + "                                                                <td align=\"center\">\r\n"
                        + "                                                                    <a href=\"https://click.send.grammarly.com/?qs=92930daabb985ed9db17fcbde4adb9ce04390b859d1fdd021bdc8ccdd8fa56babc7e0863497126a7ccd941711f996f945ca9f7cfbef078c0976029e55415f2c2\" title=\"\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://click.send.grammarly.com/?qs%3D92930daabb985ed9db17fcbde4adb9ce04390b859d1fdd021bdc8ccdd8fa56babc7e0863497126a7ccd941711f996f945ca9f7cfbef078c0976029e55415f2c2&amp;source=gmail&amp;ust=1655575690820000&amp;usg=AOvVaw3sOKohJzOpE9-EGt7CPj3k\"><img src=\"https://ci3.googleusercontent.com/proxy/mXJ_3wL5CQrfFQEvr4fhPWBcJVSyXXax-eiqcYvueb3K0GnjmzGL3-qauNzZoeANgLUV1zLqTGgv1XKlcPj5AvuiBHKLd1UCgIOzJKcOvNVUphbTOZsygHjsV3foAZAKXS4kGB49LeUGFXM=s0-d-e1-ft#https://image.send.grammarly.com/lib/fe8f12747760017576/m/3/3216-June-Promo-Email-1A.png\"\r\n"
                        + "                                                                            alt=\"\" width=\"1200\" style=\"display:block;padding:0px;text-align:center;height:auto;width:100%\" class=\"CToWUd\"></a>\r\n"
                        + "                                                                </td>\r\n"
                        + "                                                            </tr>\r\n"
                        + "                                                        </tbody>\r\n"
                        + "                                                    </table>\r\n"
                        + "                                                </td>\r\n"
                        + "                                            </tr>\r\n"
                        + "                                        </tbody>\r\n"
                        + "                                    </table>\r\n"
                        + "                                    <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"background-color:transparent;min-width:100%\" class=\"m_-4516102534817492810stylingblock-content-wrapper\">\r\n"
                        + "                                        <tbody>\r\n"
                        + "                                            <tr>\r\n"
                        + "                                                <td style=\"padding:70px 70px 10px\" class=\"m_-4516102534817492810stylingblock-content-wrapper\">\r\n"
                        + "                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
                        + "\r\n"
                        + "                                                        <tbody>\r\n"
                        + "                                                            <tr>\r\n"
                        + "                                                                <td style=\"text-align:start; margin-left:50px;\" valign=\"middle\">\r\n"
                        + "                                                                    <span style=\"font-size:17px;\" class=\"m_-4516102534817492810h1\">Hello, <Strong>" + account.get().getFullname() + "</Strong><br>\r\n"
                        + "				                                                    </span></td>\r\n"
                        + "                                                            </tr>\r\n"
                        + "                                                        </tbody>\r\n"
                        + "                                                    </table>\r\n"
                        + "                                                </td>\r\n"
                        + "                                            </tr>\r\n"
                        + "                                        </tbody>\r\n"
                        + "                                    </table>\r\n"
                        + "                                    <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"background-color:transparent;min-width:100%\" class=\"m_-4516102534817492810stylingblock-content-wrapper\">\r\n"
                        + "                                        <tbody>\r\n"
                        + "                                            <tr>\r\n"
                        + "                                                <td style=\"padding:0px 0px 20px\" class=\"m_-4516102534817492810stylingblock-content-wrapper\">\r\n"
                        + "                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
                        + "\r\n"
                        + "                                                        <tbody>\r\n"
                        + "                                                            <tr>\r\n"
                        + "                                                                <td align=\"center\" style=\"padding-bottom:0px\" valign=\"top\">\r\n"
                        + "                                                                    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"m_-4516102534817492810MobileTableWidth\" width=\"77%\">\r\n"
                        + "\r\n"
                        + "                                                                        <tbody>\r\n"
                        + "                                                                            <tr>\r\n"
                        + "                                                                                <td align=\"left\" style=\"font-size:16px;line-height:24px\" valign=\"top\">\r\n"
                        + "                                                                                    <b>Your request to retrieve your password has been confirmed.</b><br> Your password is: " + newPass + "&nbsp; <br> Please do not give out your password to outside parties. <br> <br> Best regards\r\n"
                        + "                                                                                    ,\r\n"
                        + "                                                                                    <br>\r\n"
                        + "                                                                                    <Strong> Hiroto Hotel</Strong>\r\n"
                        + "                                                                                </td>\r\n"
                        + "                                                                            </tr>\r\n"
                        + "                                                                        </tbody>\r\n"
                        + "                                                                    </table>\r\n"
                        + "                                                                </td>\r\n"
                        + "                                                            </tr>\r\n"
                        + "                                                        </tbody>\r\n"
                        + "                                                    </table>\r\n"
                        + "                                                </td>\r\n"
                        + "                                            </tr>\r\n"
                        + "                                        </tbody>\r\n"
                        + "                                    </table>\r\n"
                        + "                                    <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" role=\"presentation\" style=\"background-color:transparent;min-width:100%\" class=\"m_-4516102534817492810stylingblock-content-wrapper\">\r\n"
                        + "                                        <tbody>\r\n"
                        + "                                            <tr>\r\n"
                        + "                                                <td style=\"padding:0px\" class=\"m_-4516102534817492810stylingblock-content-wrapper\">\r\n"
                        + "                                                    <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"10\" role=\"presentation\">\r\n"
                        + "                                                        <tbody>\r\n"
                        + "                                                            <tr>\r\n"
                        + "                                                                <td align=\"center\">\r\n"
                        + "                                                                    <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\">\r\n"
                        + "                                                                        <tbody>\r\n"
                        + "                                                                            <tr>\r\n"
                        + "                                                                                <td bgcolor=\"#11A683\" style=\"border-radius:5px;background-color:#11a683\">\r\n"
                        + "                                                                                    <a style=\"font-size:14px;font-family:Inter,Helvetica,Arial,sans-serif;font-weight:700;line-height:32px;color:#ffffff;text-align:center;text-decoration:none;display:block;background-color:#11a683;border:1px solid #11a683;padding:8px 32px;border-radius:5px\"\r\n"
                        + "                                                                                        href=\"http://localhost:8080\">Log in</a></td>\r\n"
                        + "                                                                            </tr>\r\n"
                        + "                                                                        </tbody>\r\n"
                        + "                                                                    </table>\r\n"
                        + "                                                                    <br>\r\n"
                        + "                                                                </td>\r\n"
                        + "                                                            </tr>\r\n"
                        + "                                                        </tbody>\r\n"
                        + "                                                    </table>\r\n"
                        + "                                                </td>\r\n"
                        + "                                            </tr>\r\n"
                        + "                                        </tbody>\r\n"
                        + "                                    </table>\r\n"
                        + "                                </td>\r\n"
                        + "                            </tr>\r\n"
                        + "                        </tbody>\r\n"
                        + "                    </table>\r\n"
                        + "                    <br>\r\n"
                        + "                </td>\r\n"
                        + "            </tr>\r\n"
                        + "        </tbody>\r\n"
                        + "    </table>");
                sendmail.send(mail);
                model.addAttribute("message", "Please check your email!");
                return "security/login";
            } catch (MessagingException e) {
                model.addAttribute("message", "Please check the information again");
                return "user/forgot";
            }
        }
    }

    @RequestMapping("/change/form")
    public String showChangePass(){
        return "user/changepass";
    }

    @PostMapping("/changepass")
    public String doChangePass(Model model, @RequestParam String oldPassword, @RequestParam String newPassword, HttpServletRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        Account account = accountService.findById(username);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        if (!account.getPassword().equals(oldPassword)) {
            model.addAttribute("message", "Old password is not correct!");
        } else {
            account.setPassword(encodedNewPassword);
            accountDAO.save(account);
            return "security/login";
        }
        return "user/changepass";
    }
}
