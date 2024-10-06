package com.hitori.service;

import com.hitori.entity.RoomType;

import java.util.List;

public interface RoomTypeService {
    public List<RoomType> findAll() ;

    public RoomType findById(int id) ;

    public RoomType create(RoomType roomType) ;

    public RoomType update(RoomType updateRoomType) ;

    public void delete(int id) ;

}
