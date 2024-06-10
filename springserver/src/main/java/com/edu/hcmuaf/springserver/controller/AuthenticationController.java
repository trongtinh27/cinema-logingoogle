package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.auth.AuthenticationRequest;
import com.edu.hcmuaf.springserver.auth.AuthenticationResponse;
import com.edu.hcmuaf.springserver.auth.RegisterRequest;
import com.edu.hcmuaf.springserver.auth.ResetPasswordRequest;
import com.edu.hcmuaf.springserver.entity.User;
import com.edu.hcmuaf.springserver.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.config.annotation.web.oauth2.client.OAuth2ClientSecurityMarker;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(userService.authentication(authenticationRequest));
    }

    @PostMapping("/login_admin")
    public ResponseEntity<AuthenticationResponse> login_admin(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(userService.adminAuthentication(authenticationRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        AuthenticationResponse authenticationResponse = userService.register(registerRequest);
        if(authenticationResponse != null) {
            return ResponseEntity.ok(authenticationResponse);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/login-google")
    public ResponseEntity<?> getUserInfo(@RequestParam("sub") String sub, @RequestParam("fullName") String fullname, @RequestParam("email") String email) {
        User user = userService.findUserByEmail(email);
        if(user != null) {
            return ResponseEntity.ok(userService.authenticationGoogle(user));
        }
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(email);
        registerRequest.setPassword(sub);
        registerRequest.setEmail(email);
        registerRequest.setFullname(fullname);

        return register(registerRequest);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) throws MessagingException {
        AuthenticationResponse authenticationResponse = userService.resetPassword(resetPasswordRequest);
        if(authenticationResponse != null) {
            return ResponseEntity.ok(authenticationResponse);
        }
        return ResponseEntity.badRequest().build();
    }

}
