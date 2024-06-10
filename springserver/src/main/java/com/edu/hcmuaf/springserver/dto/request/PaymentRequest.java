package com.edu.hcmuaf.springserver.dto.request;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PaymentRequest {
    private int showTimeId;
    private int amount;
    private int price;
    private List<Integer> listSeatId;
}
