package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.entity.Location;
import com.edu.hcmuaf.springserver.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/locations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @GetMapping("/all")
    public ResponseEntity<?> getListLocation() {
        List<Location> getListLocation = locationService.getListLocation();
        if (getListLocation != null) {
            return ResponseEntity.ok(getListLocation);
        } else return ResponseEntity.badRequest().body(null);
    }
}
