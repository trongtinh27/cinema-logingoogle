package com.edu.hcmuaf.springserver.repositories;

import com.edu.hcmuaf.springserver.entity.Theatre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
    Theatre findOneById(int id);
    Page<Theatre> findAll(Specification<Theatre> specification, Pageable pageable);
}
