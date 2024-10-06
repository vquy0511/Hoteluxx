package com.hitori.api;

import com.hitori.entity.RoomType;
import com.hitori.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/roomtypes")
public class RoomTypeApi {
    @Autowired
    RoomTypeService roomTypeService;

    @GetMapping
    public List<RoomType> findAll(){

        return roomTypeService.findAll();
    }
}
