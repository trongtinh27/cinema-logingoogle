package com.edu.hcmuaf.springserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String background_img_url;
    private String title_img_url;
    private String title;
    private Date released_date;
    private String trailer_video_url;
    private String poster_url;
    private String description;
    private String sub_title;
    private String age_type;
    private String type;
    private int is_active;

    @OneToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "movie_categories",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;
}
