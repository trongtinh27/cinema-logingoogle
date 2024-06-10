package com.edu.hcmuaf.springserver.repositories;

import com.edu.hcmuaf.springserver.dto.response.SeatResponse;
import com.edu.hcmuaf.springserver.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query("SELECT DISTINCT new com.edu.hcmuaf.springserver.dto.response.SeatResponse(s.id, (CASE WHEN r.id IS NOT NULL THEN r.id ELSE 0 END), CONCAT(s.row_char, s.seat_number), s.price, r.expired_time, "
            + "CASE WHEN r.id IS NOT NULL AND (r.payment = 'thanh toán thành công' OR r.payment = 'đang thanh toán') THEN r.payment ELSE '' END, "
            + "(CASE WHEN r.id IS NOT NULL AND (r.payment = 'thanh toán thành công' OR r.payment = 'đang thanh toán') THEN true ELSE false END), s.status)"
            + "FROM Seat s "
            + "LEFT JOIN Reservation r ON s.id = r.seat.id AND r.showTime.id = :showTimeId "
            + "WHERE s.theatre.id = :theatreId AND s.room = :room")
    List<SeatResponse> findSeatsByShowTimeAndTheatre(@Param("showTimeId") int showTimeId, @Param("theatreId") int theatreId, @Param("room") int room);




}
