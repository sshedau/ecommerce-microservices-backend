package org.example.productservice.repository;

import org.example.productservice.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByActiveTrueOrderByNameAsc() ;

    Page<Product> findAllByActiveTrue(Pageable pageable) ;

    Optional<Product> findByIdAndActiveTrue(Long id) ;

    boolean existsByNameIgnoreCase(String name) ;

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id) ;

    Optional<Product> findByNameIgnoreCaseAndActiveTrue(String name) ;

    // Search saarkha chaalan
    Page<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable) ;

}
