package com.hitori.service.impl;

import com.hitori.dao.RoomDAO;
import com.hitori.entity.Room;
import com.hitori.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    RoomDAO dao;

    @Autowired
    RoomService roomService;

    @Override
    public List<Room> findAll() {
        return dao.findAll();
    }

    @Override
    public Room findById(Integer id) {
        return dao.findById(id).get();
    }

    @Override
    public List<Room> findByRoomTypeId(int cid) {
        return dao.findByRoomId(cid);
    }

    @Override
    public Room create(Room room) {
        return dao.save(room);
    }

    @Override
    public Room update(Room room) {
        return dao.save(room);
    }

    @Override
    public Optional<Room> findByRoomId(Integer id) {
        return dao.findById(id);
    }

    @Override
    public void delete(int id) {
        dao.deleteById(id);
    }
    @Override
    public Room findRoomById(Integer roomId) {
        return dao.findById(roomId).orElse(null);
    }

    @Override
    public Room updateRoomStatus(Integer roomId, boolean newStatus) {
        Optional<Room> optionalRoom = dao.findById(roomId);

        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            room.setAvailable(newStatus); // Đặt trạng thái mới cho phòng
            return dao.save(room);
        }else {
            return null;
        }
    }
}

