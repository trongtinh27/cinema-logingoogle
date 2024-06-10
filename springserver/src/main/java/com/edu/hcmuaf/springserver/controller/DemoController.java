package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {
    @Autowired
    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Authentication and Authorization is succeded");
    }
}
