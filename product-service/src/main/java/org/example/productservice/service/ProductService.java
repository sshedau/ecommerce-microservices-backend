package org.example.productservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.productservice.dto.PageResponseDto;
import org.example.productservice.dto.ProductDto;
import org.example.productservice.dto.ProductRequestDto;
import org.example.productservice.entities.Product;
import org.example.productservice.exception.ProductAlreadyExistsException;
import org.example.productservice.exception.ProductNotFoundException;
import org.example.productservice.repository.ProductRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Data
@AllArgsConstructor
@Slf4j      // Logging
public class ProductService {
    private final ProductRepository productRepository ;
    private final CacheManager cacheManager ;

    public PageResponseDto<ProductDto> getActiveCatalog(Pageable pageable) {
        Page<ProductDto> page = productRepository
                .findAllByActiveTrue(pageable)
                .map(this::toDto);
        return toPageResponse(page) ;
    }

    public PageResponseDto<ProductDto> getAllForAdmin(Pageable pageable) {
        Page<ProductDto> page = productRepository
                .findAll(pageable)
                .map(this::toDto);
        return toPageResponse(page) ;
    }

    @Cacheable(value = "product-service-products", key = "#id")      // (product :: 1)
    public ProductDto getById(Long id) {
        log.info("Getting product from DB for id {}", id);
        Product product = productRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with ID : " + id));
        return toDto(product);
    }

    @Cacheable(value = "product-service-productsByName", key = "#name.toLowerCase()")
    public ProductDto getByName(String name) {
        Product product = productRepository
                .findByNameIgnoreCaseAndActiveTrue(name)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with name : " + name));
        return toDto(product);
    }

    public PageResponseDto<ProductDto> searchByName(String name, Pageable pageable) {
        Page<ProductDto> page = productRepository
                .findByNameContainingIgnoreCaseAndActiveTrue(name, pageable)
                .map(this::toDto) ;
        return toPageResponse(page) ;
    }

    public ProductDto create(ProductRequestDto dto) {
        if (productRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new ProductAlreadyExistsException("Product with name : " + dto.getName() + " already exists");
        }

        Product product = new Product();

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());

        return toDto(productRepository.save(product));
    }

    @CacheEvict(value = "product-service-products", key = "#id")
    @Transactional
    public ProductDto update(Long id, ProductRequestDto dto) {

        Product product = productRepository
                .findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with ID : " + id));

        if (productRepository.existsByNameIgnoreCaseAndIdNot(dto.getName(), id)) {
            throw new ProductAlreadyExistsException(
                    "Product with name : " + dto.getName() + " already exists");
        }

        // Store old cache key
        String oldName = product.getName();

        // Update entity
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());

        // Remove stale cache entry only
        Cache cache = cacheManager.getCache("product-service-productsByName");
        if (cache != null) {
            cache.evict(oldName);
        }

        return toDto(product);
    }

    @Transactional
    public void deactivate(Long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with ID : " + id));
        product.setActive(false);
        Cache productCache = cacheManager.getCache("product-service-products");
        if (productCache != null) {
            productCache.evict(id);
        }
        Cache nameCache = cacheManager.getCache("product-service-productsByName");
        if (nameCache != null) {
            nameCache.evict(product.getName());
        }
    }



    //=========================
    // Mappers
    //=========================

    private <T> PageResponseDto<T> toPageResponse(Page<T> page) {

        return new PageResponseDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    private ProductDto toDto(Product product) {

        ProductDto dto = new ProductDto();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setActive(product.isActive());

        return dto;
    }
}
