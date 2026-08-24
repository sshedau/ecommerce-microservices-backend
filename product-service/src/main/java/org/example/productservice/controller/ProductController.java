package org.example.productservice.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.productservice.dto.PageResponseDto;
import org.example.productservice.dto.ProductDto;
import org.example.productservice.dto.ProductRequestDto;
import org.example.productservice.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Data
@AllArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService ;

    //    Public catalog : Active Products only (USER or ADMIN)
    @GetMapping
    public ResponseEntity<PageResponseDto<ProductDto>> getActiveCatalog(Pageable pageable) {
        return ResponseEntity.ok(
                productService.getActiveCatalog(pageable)
        );
    }

    //    ADMIN : All Products including inactive
    @GetMapping("/admin")
    public ResponseEntity<PageResponseDto<ProductDto>> getAllForAdmin(Pageable pageable) {
        return ResponseEntity.ok(
                productService.getAllForAdmin(pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id)) ;
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProductDto> getByName(@PathVariable String name) {
        return ResponseEntity.ok(productService.getByName(name)) ;
    }

    @GetMapping("/name/search")
    public ResponseEntity<PageResponseDto<ProductDto>> searchByName(
            @RequestParam String name,
            Pageable pageable) {
        return ResponseEntity.ok(productService.searchByName(name, pageable)) ;
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(dto)) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.update(id, dto)) ;
    }

//    Soft delete : Marks Product Inactive (ADMIN).
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        productService.deactivate(id) ;
        return ResponseEntity.noContent().build() ;
    }

}
