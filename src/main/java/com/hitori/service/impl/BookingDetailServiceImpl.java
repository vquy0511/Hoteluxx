package com.hitori.service.impl;

import com.hitori.dao.BookingDetailDAO;
import com.hitori.entity.BookingDetail;
import com.hitori.service.BookingDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingDetailServiceImpl implements BookingDetailService {
    @Autowired
    BookingDetailDAO dao;

//    @Override
//    public BookingDetail findById(Long id) {
//        return dao.findById(id).get();
//    }

    @Override
    public Optional<BookingDetail> findById(Long id) {
        return dao.findById(id); // Trả về Optional<BookingDetail>
    }

    @Override
    public void updateBookingDetail(BookingDetail updatedBookingDetail) {
        dao.save(updatedBookingDetail);
    }
}
