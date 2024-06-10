package com.edu.hcmuaf.springserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class SeatResponse {
    private int id;
    private int reservationId;
    private String seatNumber;
    private int price;
    private Date expired_time;
    private String payment;
    private boolean isBooked;
    private int status;
}
