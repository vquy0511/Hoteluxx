package com.hitori.dao;

import com.hitori.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomDAO extends JpaRepository<Room, Integer> {
    @Query("SELECT r FROM Room r WHERE r.roomType.id=?1")
    List<Room> findByRoomId(int id);

    @Query("SELECT COUNT(r) FROM Room r WHERE r.available = true")
    int countRoomCheckIn();
}
