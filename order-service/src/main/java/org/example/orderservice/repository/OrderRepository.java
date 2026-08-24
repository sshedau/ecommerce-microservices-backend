package org.example.orderservice.repository;

import org.example.orderservice.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    public List<Order> findByUserId(Long userId) ;

    public Page<Order> findByUserId(Long userId, Pageable pageable) ;

}
