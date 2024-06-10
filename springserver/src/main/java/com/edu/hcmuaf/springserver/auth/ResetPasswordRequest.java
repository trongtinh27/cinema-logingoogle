package com.edu.hcmuaf.springserver.auth;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResetPasswordRequest {
    private String username;
    private String email;
}
