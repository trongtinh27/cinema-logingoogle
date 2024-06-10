package com.edu.hcmuaf.springserver.dto.response;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserResponse {
    private int id;
    private String username;
    private String email;
    private String phone;
    private String fullName;
    private String gender;
    private String birthday;


}
