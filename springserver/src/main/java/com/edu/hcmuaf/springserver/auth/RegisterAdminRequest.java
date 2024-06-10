package com.edu.hcmuaf.springserver.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAdminRequest {
    private String username;
    private String password;
    private String email;
    private String phone_number;
    private String full_name;
    private String gender;
    private Date birthday;
    private String role;
}
