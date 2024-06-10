package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.entity.ShowTime;

import com.edu.hcmuaf.springserver.entity.Theatre;
import com.edu.hcmuaf.springserver.repositories.ShowTimeRepository;
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
import java.util.logging.SimpleFormatter;

@Service
public class ShowTimeService {
    @Autowired
    private ShowTimeRepository showTimeRepository;
  
    public List<ShowTime> getShowTimesByMovieIdAndTheatreId(int movieId, int theatreId) {
        return showTimeRepository.findShowTimeByMovieIdAndTheatreId(movieId, theatreId).orElse(null);
    }
  
    public List<ShowTime> getAllShowTime() {
        return showTimeRepository.findAll();
    }

    public ShowTime getShowTimeById(int id) {
        return showTimeRepository.findById((long) id).orElse(null);
    }

    public ShowTime updateShowTime(int id,ShowTime showTime) {
        ShowTime existShowTime = showTimeRepository.findById((long) id).orElseThrow(() -> new IllegalArgumentException("not found"));
        return getShowTime(showTime, existShowTime);
    }

    private ShowTime getShowTime(ShowTime showTime, ShowTime existShowTime) {
        existShowTime.setMovie(showTime.getMovie());
        existShowTime.setRoom(showTime.getRoom());
        existShowTime.setTheatre(showTime.getTheatre());
        existShowTime.setStart_time(showTime.getStart_time());
        existShowTime.setEnd_time(showTime.getEnd_time());
        existShowTime.setStatus(showTime.getStatus());
        return showTimeRepository.save(existShowTime);
    }

    public ShowTime createShowTime(ShowTime showTime) {
        return getShowTime(showTime, showTime);
    }

    public void deleteShowTime(long id) {
        showTimeRepository.deleteById(id);
    }


    public Page<ShowTime> getAllwithSort(String filter, int page, int perPage, String sortBy, String order) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("DESC"))
            direction = Sort.Direction.DESC;

        JsonNode filterJson;
        try {
            filterJson = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Specification<ShowTime> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (filterJson.has("q")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("movie.title"), "%" + filterJson.get("q").asText().toLowerCase() + "%"));
            }
            if (filterJson.has("movie.title")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("movie.title"), "%" + filterJson.get("movie.title").asText() + "%"));
            }
            if (filterJson.has("theatre.name")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("theatre"), "%" + filterJson.get("theatre.name").asText() + "%"));
            }
            if (filterJson.has("room")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("room"), "%" + filterJson.get("room").asText() + "%"));
            }
            return predicate;
        };
        if (sortBy.equals("title")) {
            return showTimeRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "title")));
        }
        if (sortBy.equals("theatre"))  {
            return showTimeRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "theatre")));
        }
        if (sortBy.equals("room"))  {
            return showTimeRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "room")));
        }
        return showTimeRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, sortBy)));
    }

}