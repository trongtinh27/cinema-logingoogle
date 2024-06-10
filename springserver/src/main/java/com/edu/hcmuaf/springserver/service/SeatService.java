package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.dto.response.SeatResponse;
import com.edu.hcmuaf.springserver.entity.Seat;
import com.edu.hcmuaf.springserver.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;

    public List<SeatResponse> getSeatsByShowTime(int showTimeId, int theatreId, int room) {
        return seatRepository.findSeatsByShowTimeAndTheatre(showTimeId, theatreId, room);
    }

    public Seat getSeatById(int id) {
        return seatRepository.findById(Long.valueOf(id)).orElse(null);
    }
}
