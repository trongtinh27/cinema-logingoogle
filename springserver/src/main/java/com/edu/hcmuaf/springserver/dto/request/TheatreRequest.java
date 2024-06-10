package com.edu.hcmuaf.springserver.dto.request;

import lombok.*;

import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TheatreRequest {
    private Long location_id;
    private String image;
    private String name;
    private String address;
    private String phone_number;
    private String email;
    private String description;
    private Long room_summary;
    private Time opening_hours;
    private Long rooms;
}
