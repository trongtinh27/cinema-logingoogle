package com.edu.hcmuaf.springserver.repositories;

import com.edu.hcmuaf.springserver.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);

    User findUsersById(int id);

    Page<User> findAll(Specification<User> specification, Pageable pageable);
    Optional<User> findByEmail(String email);

    User findByUsernameAndEmail(String username, String email);
}
