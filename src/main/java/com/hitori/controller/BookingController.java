package com.hitori.controller;

import com.hitori.dao.BookingDAO;
import com.hitori.dao.BookingDetailDAO;
import com.hitori.entity.BookingDetail;
import com.hitori.entity.RoomType;
import com.hitori.service.BookingDetailService;
import com.hitori.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class BookingController {

    @Autowired
    BookingService bookingService;

    @Autowired
    BookingDAO bookingDAO;

    @Autowired
    BookingDetailDAO bookingDetailDAO;

    @Autowired
    BookingDetailService bookingDetailService;

    //My booking của user sau khi user login
    @RequestMapping("/booking/list")
    public String myBooking(Model model, HttpServletRequest request) {
        String username = request.getRemoteUser();
        model.addAttribute("bookings", bookingService.findByUsername(username));
        return "user/list";
    }

    //Danh sách booking
    @RequestMapping("/bookinglist")
    public String BookingList(Model model) {
        model.addAttribute("bookinglist", bookingDAO.findAll());
        return "admin/bookinglist";
    }

}
