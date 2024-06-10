package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.dto.request.TheatreRequest;
import com.edu.hcmuaf.springserver.entity.Theatre;
import com.edu.hcmuaf.springserver.repositories.LocationRepository;
import com.edu.hcmuaf.springserver.repositories.TheatreRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
@Service
public class TheatreService {
    @Autowired
    private TheatreRepository theatreRepository;
    @Autowired
    private LocationService locationService;

    public List<Theatre> getAllTheatre() {
        return theatreRepository.findAll();
    }

    public Theatre getTheatreById(int id) {
        return theatreRepository.findOneById(id);
    }

    public void deleteTheatreById(long id) {theatreRepository.deleteById(id);}

    public Theatre createTheatre(TheatreRequest theatreRequest) {
        System.out.println(theatreRequest);
        Theatre theatre = new Theatre();
        theatre.setImage(theatreRequest.getImage());
        theatre.setAddress(theatreRequest.getAddress());
        theatre.setEmail(theatreRequest.getEmail());
        theatre.setName(theatreRequest.getName());
        theatre.setDescription(theatreRequest.getDescription());
        theatre.setLocation(locationService.getLocationById(theatreRequest.getLocation_id()));
        theatre.setOpening_hours(theatreRequest.getOpening_hours());
        theatre.setRoom_summary(theatreRequest.getRoom_summary());
        theatre.setPhone_number(theatreRequest.getPhone_number());
        theatre.setRooms(theatreRequest.getRooms());
        return theatreRepository.save(theatre);}

    public Theatre updateTheatre(TheatreRequest theatreRequest, int id) {
        Theatre existTheatre = theatreRepository.findOneById(id);
        existTheatre.setImage(theatreRequest.getImage());
        existTheatre.setAddress(theatreRequest.getAddress());
        existTheatre.setEmail(theatreRequest.getEmail());
        existTheatre.setName(theatreRequest.getName());
        existTheatre.setLocation(locationService.getLocationById(theatreRequest.getLocation_id()));
        existTheatre.setDescription(theatreRequest.getDescription());
        existTheatre.setOpening_hours(theatreRequest.getOpening_hours());
        existTheatre.setRoom_summary(theatreRequest.getRoom_summary());
        existTheatre.setPhone_number(theatreRequest.getPhone_number());
        existTheatre.setRooms(theatreRequest.getRooms());
        return theatreRepository.save(existTheatre);
    }


    public Page<Theatre> getAllwithSort(String filter, int page, int perPage, String sortBy, String order) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("DESC"))
            direction = Sort.Direction.DESC;

        JsonNode filterJson;
        try {
            filterJson = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Specification<Theatre> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (filterJson.has("q")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + filterJson.get("q").asText().toLowerCase() + "%"));
            }
            if (filterJson.has("name")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + filterJson.get("name").asText() + "%"));
            }
            if (filterJson.has("address")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("address"), "%" + filterJson.get("address").asText() + "%"));
            }
            return predicate;
        };
        if (sortBy.equals("name")) {
            return theatreRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "name")));
        }
        if (sortBy.equals("address"))  {
            return theatreRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "address")));
        }
        return theatreRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, sortBy)));
    }
}
