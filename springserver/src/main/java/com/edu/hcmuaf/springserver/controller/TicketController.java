package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.dto.response.TicketResponse;
import com.edu.hcmuaf.springserver.entity.ShowTime;
import com.edu.hcmuaf.springserver.entity.Ticket;
import com.edu.hcmuaf.springserver.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/tickets")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/all")
    public ResponseEntity<?> getListTicket() {
        List<Ticket> ticketList = ticketService.getAllTicket();
        if (ticketList != null ) {
            return ResponseEntity.ok(ticketList);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable int id) {
        Ticket ticket = ticketService.getTicketById(id);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/")
    public ResponseEntity<?> createTicket() {
        return null;
    }
    @PutMapping("/{id}") public ResponseEntity<?> updateTicket() {
        return null;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getTicketsByUserId(@PathVariable int userId) {
        List<Ticket> ticketList = ticketService.findTicketsByUserId(userId);

        List<TicketResponse> ticketResponseList = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            TicketResponse ticketResponse = new TicketResponse();
            ticketResponse.setId(ticket.getId());
            ticketResponse.setOrderCode(ticket.getReservation().getOrder());
            ticketResponse.setReservationTime(ticket.getReservation().getReservation_time().toString());
            ticketResponse.setTicketCode(ticket.getTicketCode());
            ticketResponse.setPrice(ticket.getPrice());

            ticketResponseList.add(ticketResponse);
        }
        if(!ticketResponseList.isEmpty()) return ResponseEntity.ok(ticketResponseList);
        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<Page<Ticket>> getAll(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "{}") String filter,
                                                         @RequestParam(defaultValue = "16") int perPage,
                                                         @RequestParam(defaultValue = "showTime.movie.title") String sort,
                                                         @RequestParam(defaultValue = "DESC") String order) {
        Page<Ticket> tickets = ticketService.getAllwithSort(filter, page, perPage, sort, order);
        return ResponseEntity.ok(tickets);
    }
}
  

