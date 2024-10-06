package com.hitori.api;

import com.hitori.dao.BookingDAO;
import com.hitori.entity.Booking;
import com.hitori.entity.RevenueStatisticsDto;
import com.hitori.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RevenueStatisticsApi {
    @Autowired
    BookingService bookingService;

    @Autowired
    BookingDAO bookingDAO;

    @GetMapping("/revenue")
    public RevenueStatisticsDto  getRevenueStatistics(@RequestParam String checkInDateStr, @RequestParam String checkOutDateStr) {
        // Chuyển các chuỗi ngày sang đối tượng LocalDate
        LocalDate checkInDate = LocalDate.parse(checkInDateStr);
        LocalDate checkOutDate = LocalDate.parse(checkOutDateStr);
        System.out.println("Converted check-in date: " + checkInDate);
        System.out.println("Converted check-out date: " + checkOutDate);
        java.sql.Date convertedCheckInDate = java.sql.Date.valueOf(checkInDate);
        java.sql.Date convertedCheckOutDate = java.sql.Date.valueOf(checkOutDate);
        List<Object[]> bookingDetails = bookingService.findBookingDetailsByDateRange(
                convertedCheckInDate, convertedCheckOutDate);
        BigDecimal totalPrice = bookingService.getTotalPriceByDateRange(
                convertedCheckInDate, convertedCheckOutDate);
        return new RevenueStatisticsDto(bookingDetails, totalPrice);
    }

    @GetMapping("/revenue-by-room")
    @ResponseBody // Annotation này thông báo cho Spring Boot rằng dữ liệu trả về cần được serialize thành JSON
    public List<Object[]> getRevenueByRoom(@RequestParam String roomname) {
        // Gọi phương thức từ service để thực hiện truy vấn dữ liệu
        return bookingDAO.revenueByRoomName(roomname);
    }
}
