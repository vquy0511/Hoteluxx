package com.hitori.dao;

import com.hitori.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
public interface BookingDAO extends JpaRepository<Booking, Long> {
    @Query("SELECT o FROM Booking o WHERE o.account.username=?1")
    List<Booking> findByUsername(String username);
    @Query("SELECT b.id, b.account.username, b.account.fullname, b.check_in, b.check_out, SUM(b.totalPrice) AS total_price " +
        "FROM Booking b " +
        "WHERE b.check_in >= :checkInDate " +
        "AND b.check_out <= :checkOutDate " +
        "AND b.status = true " +
        "GROUP BY b.id, b.account.username, b.account.fullname, b.check_in, b.check_out, b.totalPrice")
    List<Object[]> findBookingDetailsByDateRange(
        @Param("checkInDate") Date checkInDate,
        @Param("checkOutDate") Date checkOutDate);

    @Query("SELECT SUM(b.totalPrice) AS total_price " +
            "FROM Booking b " +
            "WHERE b.check_in >= :checkInDate " +
            "AND b.check_out <= :checkOutDate " +
            "AND b.status = true ")
    BigDecimal getTotalPriceByDateRange(
            @Param("checkInDate") Date checkInDate,
            @Param("checkOutDate") Date checkOutDate);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = true")
    Long countPaidBookings();

    @Query("SELECT b.room_name, SUM(b.totalPrice) FROM Booking b WHERE b.room_name = :roomname GROUP BY b.room_name")
    List<Object[]> revenueByRoomName(@Param("roomname") String roomname);
}
