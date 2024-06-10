package com.edu.hcmuaf.springserver.repositories;

import com.edu.hcmuaf.springserver.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface CategoryRepository extends JpaRepository<Category, Long> {


}
