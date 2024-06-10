package com.edu.hcmuaf.springserver.repositories;

import com.edu.hcmuaf.springserver.entity.MovieCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieCategoryRepository extends JpaRepository<MovieCategory, Long> {
    @Transactional
    void deleteAllByMovieId(long movieId);
}
