package com.edu.hcmuaf.springserver.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theatre_id", referencedColumnName = "id")
    private Theatre theatre;
    private char row_char;
    private int seat_number;
    private String room;
    private int price;
    private int status;


}
