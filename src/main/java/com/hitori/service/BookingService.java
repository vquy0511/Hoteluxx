package com.hitori.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hitori.entity.Booking;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
public interface BookingService {
    public Booking findById(Long id) ;
    public List<Booking> findByUsername(String username) ;
    List<Object[]> findBookingDetailsByDateRange(Date checkInDate, Date checkOutDate);
    BigDecimal getTotalPriceByDateRange(Date checkInDate, Date checkOutDate);
    void updateBookingStatusPayment(Long bookingId, boolean status);
}
