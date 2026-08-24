package org.example.userservice.repository;

import org.example.userservice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Page<User> findAll(Pageable pageable) ;

    // Search saarkha chaalan
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable) ;

    List<User> findByName(String name);

    Optional<User> findByEmail(String email) ;

}
