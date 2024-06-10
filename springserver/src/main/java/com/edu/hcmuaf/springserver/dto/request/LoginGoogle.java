package com.edu.hcmuaf.springserver.dto.request;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginGoogle {
    private String sub;
    private String fullName;
    private String email;


    @Override
    public String toString() {
        String name = fullName.replace(" ", "");
        return "sub="+sub+"&fullName="+name+"&email="+email;
    }
}
