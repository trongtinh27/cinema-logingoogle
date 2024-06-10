package com.edu.hcmuaf.springserver.repositories;

import com.edu.hcmuaf.springserver.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findOneById(int id);
    Page<Movie> findAll(Specification<Movie> specification, Pageable pageable);
}
