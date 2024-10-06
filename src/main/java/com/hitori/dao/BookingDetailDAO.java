package com.hitori.dao;

import com.hitori.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingDetailDAO extends JpaRepository<BookingDetail, Long> {
    Optional<BookingDetail> findById(Long id);
}
