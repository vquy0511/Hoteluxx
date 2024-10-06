package com.hitori.dao;

import com.hitori.entity.Account;
import com.hitori.entity.Booking;
import com.hitori.entity.RoomType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeDAO extends JpaRepository<RoomType,Integer> {
   @Query("SELECT o FROM RoomType o WHERE o.name=?1")
   List<RoomType> findByName(String name);
   @Query("SELECT COUNT(o) FROM RoomType o")
   int countAllRoomTypes();

   @Query("SELECT o FROM RoomType o ORDER BY o.id ASC")
   List<RoomType> findFirst4RoomTypes(Pageable pageable);
}