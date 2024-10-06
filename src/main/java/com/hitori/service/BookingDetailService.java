package com.hitori.service;

import com.hitori.entity.Booking;
import com.hitori.entity.BookingDetail;

import java.util.List;
import java.util.Optional;

public interface BookingDetailService {
//    public BookingDetail findById(Long id) ;
//    BookingDetail save(BookingDetail bookingDetail );
    Optional<BookingDetail> findById(Long id);

    void updateBookingDetail(BookingDetail updatedBookingDetail);
}
