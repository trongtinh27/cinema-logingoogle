package com.edu.hcmuaf.springserver.repositories;

import com.edu.hcmuaf.springserver.entity.ShowTime;
import com.edu.hcmuaf.springserver.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findOneById(int id);
    boolean existsByShowTimeIdAndSeatId(Long showTime_id, int seat_id);
    Optional<Ticket> findTicketByTicketCode(String ticketCode);

    @Query("SELECT t FROM Ticket t JOIN t.reservation r WHERE r.user.id = :userId")
    List<Ticket> findTicketsByUserId(Long userId);

    Page<Ticket> findAll(Specification<Ticket> specification, Pageable pageable);

}
