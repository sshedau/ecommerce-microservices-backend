package org.example.orderservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.orderservice.client.ProductClient;
import org.example.orderservice.dto.*;
import org.example.orderservice.entities.Order;
import org.example.orderservice.exception.OrderNotFoundException;
import org.example.orderservice.exception.ProductNotAvailableException;
import org.example.orderservice.repository.OrderRepository;
import org.example.orderservice.security.AuthenticatedUser;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository ;
    private final ProductClient productClient ;
    private final CacheManager cacheManager ;

    // =========================================================
    // Authentication
    // =========================================================

    private AuthenticatedUser getAuthenticatedUser() {

        return (AuthenticatedUser)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
    }

    // =========================================================
    // Create Order
    // =========================================================

//    @Cacheable(
//            value = "orders",
//            key = "@orderService.getOrderCacheKey(#id)"
//    )
    @Transactional
    public OrderDto createOrder(CreateOrderDto dto) {

        AuthenticatedUser user = getAuthenticatedUser();

        // Get product from product-service
        ProductResponseDto product =
                productClient.getProductById(dto.getProductId());

        if (product == null) {
            throw new ProductNotAvailableException("Product not found");
        }

        if (!product.isActive()) {
            throw new ProductNotAvailableException("Product is not active");
        }

        Order order = new Order();

        // User information comes from JWT
        order.setUserId(user.getUserId());

        // Product information comes from product-service
        order.setProductId(product.getId());
        order.setProductName(product.getName());

        // Snapshot price at the time of purchase
        order.setPriceAtPurchase(product.getPrice());

        Order savedOrder = orderRepository.save(order);

        return mapOrder(savedOrder);
    }

    // =========================================================
    // USER -> My Orders
    // =========================================================

    public List<OrderDto> getMyOrders() {

        AuthenticatedUser user = getAuthenticatedUser();

        return orderRepository
                .findByUserId(user.getUserId())
                .stream()
                .map(this::mapOrder)
                .toList();
    }

    // =========================================================
    // USER -> My Orders Paginated
    // =========================================================

    public PageResponseDto<OrderDto> getMyOrdersPaginated(
            Pageable pageable) {

        AuthenticatedUser user = getAuthenticatedUser();

        Page<OrderDto> page =
                orderRepository
                        .findByUserId(user.getUserId(), pageable)
                        .map(this::mapOrder);

        return toPageResponse(page);
    }

//    @Cacheable(
//            value = "orders",
//            key = "#id + ':' + @orderService.getAuthenticatedUser().getUserId()"
//    )

    public OrderDto getMyOrderById(Long id) {

        AuthenticatedUser user = getAuthenticatedUser();

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with ID : " + id
                        ));

        System.out.println("ORDER ID       = " + order.getId());
        System.out.println("ORDER USER ID  = " + order.getUserId());
        System.out.println("JWT USER ID    = " + user.getUserId());

        if (!order.getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException(
                    "You are not allowed to access this order"
            );
        }

        return mapOrder(order);
    }

    // =========================================================
    // USER -> Delete Self Order by orderId
    // =========================================================

    public void deleteMyOrderById(Long id) {
        AuthenticatedUser user = getAuthenticatedUser();

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with ID: " + id
                        ));

        if (!order.getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException(
                    "You are not allowed to delete this order"
            );
        }

        orderRepository.delete(order);
    }

    // =========================================================
    // ADMIN -> All Orders
    // =========================================================

    public List<OrderDto> getAllOrders() {

        return orderRepository
                .findAll()
                .stream()
                .map(this::mapOrder)
                .toList();
    }

    // =========================================================
    // ADMIN -> All Orders Paginated
    // =========================================================

    public PageResponseDto<OrderDto> getAllOrdersPaginated(
            Pageable pageable) {

        Page<OrderDto> page =
                orderRepository
                        .findAll(pageable)
                        .map(this::mapOrder);

        return toPageResponse(page);
    }

    // =========================================================
    // ADMIN -> Orders of Specific User
    // =========================================================

    public List<OrderDto> getAllOrdersByUser(Long userId) {

        return orderRepository
                .findByUserId(userId)
                .stream()
                .map(this::mapOrder)
                .toList();
    }


    // =========================================================
    // ADMIN -> Orders of User Paginated
    // =========================================================

    public PageResponseDto<OrderDto> getAllOrdersByUserPaginated(
            Long userId,
            Pageable pageable) {

        Page<OrderDto> page =
                orderRepository
                        .findByUserId(userId, pageable)
                        .map(this::mapOrder);

        return toPageResponse(page);
    }

    // =========================================================
    // ADMIN -> Delete Order
    // =========================================================

    public void deleteOrder(Long id) {

        System.out.println("🔥 DELETE SERVICE CALLED: " + id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with ID: " + id
                        ));

        orderRepository.delete(order);
    }

    // =========================================================
    // ADMIN -> Update Order
    // =========================================================


    public OrderDto updateOrder(
            Long id,
            CreateOrderDto dto) {

        Order order = orderRepository
                .findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with ID : " + id
                        ));

        /*
         * We don't allow the client to directly change:
         *
         * userId
         * productName
         * priceAtPurchase
         *
         * Those belong to the order/product relationship.
         *
         * If you want to change the product, ask
         * product-service for the new product.
         */

        ProductResponseDto product =
                productClient.getProductById(
                        dto.getProductId()
                );

        if (product == null) {
            throw new ProductNotAvailableException(
                    "Product not found"
            );
        }

        if (!product.isActive()) {
            throw new ProductNotAvailableException(
                    "Product is not active"
            );
        }

        order.setProductId(product.getId());
        order.setProductName(product.getName());

        /*
         * Decide carefully whether you want to change
         * priceAtPurchase during an update.
         *
         * Normally historical purchase price should NOT
         * change.
         */

        return mapOrder(order);
    }

    // =========================================================
    // Mapper
    // =========================================================

    private OrderDto mapOrder(Order order) {

        return new OrderDto(
                order.getId(),
                order.getUserId(),
                order.getProductId(),
                order.getProductName(),
                order.getPriceAtPurchase()
        );
    }

    // =========================================================
    // Page Mapper
    // =========================================================

    private <T> PageResponseDto<T> toPageResponse(
            Page<T> page) {

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

}
