package com.edu.hcmuaf.springserver.repositories;

import com.edu.hcmuaf.springserver.entity.Category;
import com.edu.hcmuaf.springserver.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

}
