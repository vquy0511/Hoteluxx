package com.hitori.controller;

import com.hitori.dao.BookingDAO;
import com.hitori.dao.BookingDetailDAO;
import com.hitori.entity.Account;
import com.hitori.entity.BookingDetail;
import com.hitori.entity.Room;
import com.hitori.entity.RoomType;
import com.hitori.service.BookingDetailService;
import com.hitori.service.BookingService;
import com.hitori.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

@Controller
public class AdminCheckOutController {
    @Autowired
    BookingDetailDAO bookingDetailDAO;

    @Autowired
    BookingDetailService bookingDetailService;

    @Autowired
    BookingDAO bookingDAO;

    @Autowired
    BookingService bookingService;

    @Autowired
    RoomService roomService;

    @RequestMapping("/checkBookingDetail")
    public String showBookingDetail(Model model) {
        model.addAttribute("bookingDetails", bookingDetailDAO.findAll());
        return "admin/checkout";
    }

//    @GetMapping("/updateStatus/{roomId}/{newStatus}")
//    public String doUpdateRoomStatus(@PathVariable("roomId") Integer roomId, @PathVariable("newStatus") boolean newStatus) {
//        roomService.updateRoomStatus(roomId, newStatus);
//        return "redirect:/roomlist";
//    }

    @GetMapping("/checkin/{id}")
    public String doGetCheckInBooking(@PathVariable("id") long id, Model model) {
        Optional<BookingDetail> bookingDetailOptional = bookingDetailDAO.findById(id);
        if (bookingDetailOptional.isPresent()){
            BookingDetail bookingDetail = bookingDetailOptional.get();
            model.addAttribute("bookingDetail", bookingDetail);
            return "admin/checkoutForm";
        } else {
            return "admin/checkout"; // Chuyển hướng đến trang thông báo lỗi hoặc xử lý khác
        }
    }

    @PostMapping("/update")
    public String updateBooking(@ModelAttribute("bookingDetail") BookingDetail updatedBookingDetail,
                                @RequestParam(value = "name1", required = false) String name1,
                                @RequestParam(value = "name2", required = false) String name2,
                                @RequestParam(value = "name3", required = false) String name3,
                                @RequestParam("check_in") String checkInDate,
                                @RequestParam("check_out") String checkOutDate,
                                @RequestParam("card1") MultipartFile card1,
                                @RequestParam("card2") MultipartFile card2,
                                @RequestParam("card3") MultipartFile card3,
                                @RequestParam("roomid") String roomIdStr) {
        int roomId = Integer.parseInt(roomIdStr);
        Optional<Room> optionalRoom = roomService.findByRoomId(roomId);
        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            updatedBookingDetail.setRoom(room);
        } else {
        }
        if (name1 != null && !name1.isEmpty()) {
            updatedBookingDetail.setPaxname1(name1);
        }
        if (name2 != null && !name2.isEmpty()) {
            updatedBookingDetail.setPaxname2(name2);
        }
        if (name3 != null && !name3.isEmpty()) {
            updatedBookingDetail.setPaxname3(name3);
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date checkIn = formatter.parse(checkInDate);
            Date checkOut = formatter.parse(checkOutDate);

            updatedBookingDetail.setCheck_in(checkIn);
            updatedBookingDetail.setCheck_out(checkOut);
        } catch (ParseException | java.text.ParseException e) {
            // Xử lý exception nếu có
        }
        try {
            String uploadDir = "./src/main/resources/static/uploads/";
            createUploadsDirectory(uploadDir);
            String imageName1 = saveImage(card1, uploadDir);
            String imageName2 = saveImage(card2, uploadDir);
            String imageName3 = saveImage(card3, uploadDir);

            updatedBookingDetail.setImagecard1(imageName1);
            updatedBookingDetail.setImagecard2(imageName2);
            updatedBookingDetail.setImagecard3(imageName3);
        } catch (IOException e) {
            // Xử lý exception nếu có
        }
        updatedBookingDetail.setStatus(2);
        bookingDetailService.updateBookingDetail(updatedBookingDetail);
        Room room = updatedBookingDetail.getRoom();
        if (room != null) {
            // Cập nhật trạng thái của Room thành true
            room.setAvailable(true);
            // Lưu thông tin cập nhật của Room vào cơ sở dữ liệu (nếu cần)
            roomService.update(room);
        }
        return "redirect:/roomlist";
    }


    private String saveImage(MultipartFile file, String uploadDir) throws IOException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename(); // Sử dụng tên tệp tin gốc
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, bytes);
            return fileName;
        }
        return null;
    }
    private void createUploadsDirectory(String uploadDir) {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Directory created successfully!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
    }

    @GetMapping("/editCheckOut/{id}")
    public String doGetEditRoomCheckOut(@PathVariable("id") int id, Model model) {
        Room rooms = roomService.findById(id);
        if (rooms != null) {
            rooms.setAvailable(false);
            roomService.update(rooms); // Cập nhật trạng thái của phòng trong cơ sở dữ liệu
        }
        return "redirect:/roomlist";
    }
}