package com.edu.hcmuaf.springserver.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToOne
    @JoinColumn(name = "show_time_id", nullable = false)
    private ShowTime showTime;
    @OneToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;
    @Column(name = "code_order")
    private String order;
    private String phone_number;
    private String email;
    private int original_price;
    private int total_price;
    private Date reservation_time;
    private Date expired_time;
    private String payment;
}
