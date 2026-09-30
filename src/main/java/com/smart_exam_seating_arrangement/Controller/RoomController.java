package com.smart_exam_seating_arrangement.Controller;

import com.smart_exam_seating_arrangement.entity.Room;
import com.smart_exam_seating_arrangement.repository.RoomRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @PostMapping("/save")
    public String saveRoom(@RequestBody Room room) {
        roomRepository.save(room);
        return "Room saved successfully";
    }

    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}