package com.edu.hcmuaf.springserver.repositories;

import com.edu.hcmuaf.springserver.entity.ShowTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {

//    Optional<ShowTime> findShowTimeById(Long id);
    Optional<List<ShowTime>> findShowTimeByMovieIdAndTheatreId(int movieId, int theatreId);

    Page<ShowTime> findAll(Specification<ShowTime> specification, Pageable pageable);
}
