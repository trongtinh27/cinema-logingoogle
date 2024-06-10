package com.edu.hcmuaf.springserver.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TicketResponse {
    private int id;
    private String orderCode;
    private String reservationTime;
    private String ticketCode;
    private int price;
}
