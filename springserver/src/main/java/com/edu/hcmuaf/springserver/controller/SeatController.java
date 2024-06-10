package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.dto.response.SeatResponse;
import com.edu.hcmuaf.springserver.service.ReservationService;
import com.edu.hcmuaf.springserver.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/seats")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SeatController {
    @Autowired
    private SeatService seatService;
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/get/{showTimeId}/{theatreId}/{room}")
    public ResponseEntity<List<SeatResponse>> getSeatsByShowTime(@PathVariable("showTimeId") int showTimeId, @PathVariable("theatreId") int theatreId, @PathVariable("room") int room) {
        List<SeatResponse> seats = seatService.getSeatsByShowTime(showTimeId, theatreId, room);

        System.out.println(seatService.getSeatById(2));

        Map<Integer, SeatResponse> idMap = new HashMap<>();

        Date now = new Date();

        for (SeatResponse seat : seats) {
            if (idMap.containsKey(seat.getId()) && seat.getPayment() != null) {
                idMap.put(seat.getId(), seat);
            }
            else if (!idMap.containsKey(seat.getId())) {
                idMap.put(seat.getId(), seat);
            }
            if(seat.getReservationId() != 0 && seat.getPayment() != null) {
                    if (seat.getPayment().equals("Đang thanh toán") && (seat.getExpired_time().before(now))) {
                        reservationService.updateReservationPaymentFailure(seat.getReservationId());
                }
            }
        }
        List<SeatResponse> result = new ArrayList<>(idMap.values());

        return ResponseEntity.ok(result);
    }

}
