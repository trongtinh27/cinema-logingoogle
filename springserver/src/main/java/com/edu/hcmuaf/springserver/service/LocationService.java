package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.entity.Location;
import com.edu.hcmuaf.springserver.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public List<Location> getListLocation() { return locationRepository.findAll();}

    public Location getLocationById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }
}
