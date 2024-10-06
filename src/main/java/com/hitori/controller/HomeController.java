package com.hitori.controller;

import com.hitori.dao.RoomTypeDAO;
import com.hitori.entity.RoomType;
import com.hitori.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    RoomTypeService roomTypeService;

    @Autowired
    RoomTypeDAO roomTypeDAO;

    @RequestMapping({"/","/home/index","/home"})
    public String showIndex(Model model){
        Pageable pageable = PageRequest.of(0, 4); // Lấy 4 phần tử đầu tiên (trang 0, kích thước trang 4)
        List<RoomType> rt4 = roomTypeDAO.findFirst4RoomTypes(pageable);
        model.addAttribute("rt4", rt4);
        return "user/index";
    }

    @GetMapping("/roomtype")
    public String showRoomType(){
        return "user/roomtype";
    }

    @RequestMapping("/about")
    public String showAbout() {
        return "user/about";
    }

    @RequestMapping("/contact")
    public String showContact() {
        return "user/contact";
    }

    @RequestMapping("/admin/home/index")
    public String admin() {
        return "admin/adminIndex";
    }

    @RequestMapping("/bookingPage")
    public String showBookingPage(Model model){
        model.addAttribute("bkr", roomTypeService.findAll());
        return "user/bookingPage";
    }

    @RequestMapping("/payment")
    public String showPayment(){
        return "user/payment";
    }

    @RequestMapping("/error-page")
    public String show404(){
        return "user/404";
    }

    @GetMapping("/success-page")
    public String successPage() {
        return "user/success"; // Trả về tên của trang HTML
    }

    @RequestMapping("/cancel")
    public String showPaymentCancel(){
        return "user/cancel";
    }

    @RequestMapping("/statistics")
    public String adminRevenue() {
        return "admin/revenue";
    }
    @RequestMapping("/statistics-by-room")
    public String adminRevenueByRoom() {
        return "admin/revenueroom";
    }
}
