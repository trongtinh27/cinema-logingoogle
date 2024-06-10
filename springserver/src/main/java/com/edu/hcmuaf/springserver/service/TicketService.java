package com.edu.hcmuaf.springserver.service;

import com.edu.hcmuaf.springserver.entity.Theatre;
import com.edu.hcmuaf.springserver.entity.Ticket;
import com.edu.hcmuaf.springserver.repositories.TicketRepository;
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
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public List<Ticket> getAllTicket() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(int id) {
        return ticketRepository.findOneById(id);
    }

    public boolean checkExistTicket(int showTimeId, int seatId) {
        return ticketRepository.existsByShowTimeIdAndSeatId((long) showTimeId, seatId);
    }
    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }
    public Ticket findTicketByTicketCode(String ticketCode) {
        return ticketRepository.findTicketByTicketCode(ticketCode).orElse(null);
    }

    public List<Ticket> findTicketsByUserId(int userId) {
        return ticketRepository.findTicketsByUserId((long) userId);
    }

    public void deleteTicket(long id) {ticketRepository.deleteById(id);}

    public Page<Ticket> getAllwithSort(String filter, int page, int perPage, String sortBy, String order) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (order.equalsIgnoreCase("DESC"))
            direction = Sort.Direction.DESC;

        JsonNode filterJson;
        try {
            filterJson = new ObjectMapper().readTree(java.net.URLDecoder.decode(filter, StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Specification<Ticket> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (filterJson.has("q")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("movie"), "%" + filterJson.get("q").asText().toLowerCase() + "%"));
            }
            if (filterJson.has("movie")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("movie"), "%" + filterJson.get("movie").asText() + "%"));
            }
            if (filterJson.has("theatre")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("theatre"), "%" + filterJson.get("theatre").asText() + "%"));
            }
            if (filterJson.has("seat")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("seat"), "%" + filterJson.get("seat").asText() + "%"));
            }
            if (filterJson.has("seat_number")) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("seat_number"), "%" + filterJson.get("seat").asText() + "%"));
            }
            return predicate;
        };
        if (sortBy.equals("movie")) {
            return ticketRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "movie")));
        }
        if (sortBy.equals("theatre"))  {
            return ticketRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "theatre")));
        }
        if (sortBy.equals("seat.row_char"))  {
            return ticketRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "seat")));
        }
        if (sortBy.equals("seat.seat_number"))  {
            return ticketRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, "seat")));
        }
        return ticketRepository.findAll(specification, PageRequest.of(page, perPage, Sort.by(direction, sortBy)));
    }
}