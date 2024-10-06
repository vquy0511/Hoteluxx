package com.hitori.controller;

import com.hitori.dao.RoomTypeDAO;
import com.hitori.entity.RoomType;
import com.hitori.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class RoomController {
    @Autowired
    RoomService roomService;

    @Autowired
    RoomTypeService roomTypeService;

    @Autowired
    BookingDetailService bookingDetailService;

    @Autowired
    RoomTypeDAO roomTypeDAO;

    @GetMapping("/roomlist")
    public String showStaffList(Model model) {
        model.addAttribute("rooms", roomService.findAll());
        return "admin/roomlist";
    }

    @GetMapping("/rtypelist")
    public String showRoomTypeList(Model model) {
        model.addAttribute("rtl", roomTypeService.findAll());
        return "admin/roomtypelist";
    }

    @RequestMapping("/roomtype/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        RoomType item = roomTypeService.findById(id);
        model.addAttribute("item", item);
        return "user/roomdetail";
    }

//    @GetMapping("/editRoom/{id}")
//    public String doGetEditRoomType(@PathVariable("id") int id, Model model) {
//        RoomType roomType = roomTypeService.findById(id);
//        model.addAttribute("roomType", roomType);
//        return "admin/editRoomType";
//    }

    @PostMapping("/editRoom")
    public String doPostEditRoomType(@ModelAttribute("roomType") RoomType updatedRoomType,
                                     BindingResult result, Model model){
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                System.out.println("Lỗi ở thuộc tính: " + error.getField());
                System.out.println("Thông báo lỗi: " + error.getDefaultMessage());
            }
            model.addAttribute("errors", result.getFieldErrors());
            return "admin/editRoomType";
        }
        roomTypeService.update(updatedRoomType);
        return "redirect:/rtypelist"; // Chuyển hướng sau khi cập nhật thành công
    }

}