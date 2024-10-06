package com.hitori.service;

import com.hitori.entity.Account;
import com.hitori.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    public List<Room> findAll() ;

    public Room findById(Integer id) ;

    public List<Room> findByRoomTypeId(int cid) ;

    public Room create(Room room) ;

    public Room update(Room room) ;

    Optional<Room> findByRoomId(Integer id);

    public void delete(int id) ;

    Room findRoomById(Integer roomId);

    Room updateRoomStatus(Integer roomId, boolean newStatus);
}
