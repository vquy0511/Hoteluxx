package com.hitori.service.impl;

import com.hitori.dao.RoomTypeDAO;
import com.hitori.entity.RoomType;
import com.hitori.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class  RoomTypeServiceImpl implements RoomTypeService {
    @Autowired
    RoomTypeDAO roomTypeDAO;

    @Override
    public List<RoomType> findAll() {
        return roomTypeDAO.findAll();
    }

    @Override
    public RoomType findById(int id) {
        return roomTypeDAO.findById(id).get();
    }

    @Override
    public RoomType create(RoomType roomType) {
        return roomTypeDAO.save(roomType);
    }

    @Override
    public RoomType update(RoomType updatedRoomType) {
       return roomTypeDAO.save(updatedRoomType);
    }

    @Override
    public void delete(int id) {
        roomTypeDAO.deleteById(id);
    }
}
