package com.hitori.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitori.dao.BookingDAO;
import com.hitori.dao.BookingDetailDAO;
import com.hitori.entity.Account;
import com.hitori.entity.Booking;
import com.hitori.entity.BookingDetail;
import com.hitori.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingDAO dao;

    @Autowired
    BookingDetailDAO ddao;

    public Booking findById(Long id) {
        return dao.findById(id).get();
    }

    public List<Booking> findByUsername(String username) {
        return dao.findByUsername(username);
    }

    @Override
    public List<Object[]> findBookingDetailsByDateRange(Date checkInDate, Date checkOutDate) {
        return dao.findBookingDetailsByDateRange(checkInDate, checkOutDate);
    }

    @Override
    public BigDecimal getTotalPriceByDateRange(Date checkInDate, Date checkOutDate) {
        return dao.getTotalPriceByDateRange(checkInDate, checkOutDate);
    }

    @Override
    public void updateBookingStatusPayment(Long bookingId, boolean status) {
        Optional<Booking> optionalBooking = dao.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            booking.setStatus(status); // Cập nhật trạng thái
            dao.save(booking); // Lưu thay đổi
        } else {
            System.out.println("BookingId not found: " + bookingId); // Log ra Console để kiểm tra giá trị bookingId
        }
    }

    public List<Object[]> getRevenueByRoom(String roomname) {
        return dao.revenueByRoomName(roomname);
    }
}