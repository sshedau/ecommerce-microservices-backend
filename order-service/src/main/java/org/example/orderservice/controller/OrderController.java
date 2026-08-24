package org.example.orderservice.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.orderservice.dto.CreateOrderDto;
import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.dto.PageResponseDto;
import org.example.orderservice.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
//@RequestMapping("/api/v1/users/{userId}/orders")
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService ;

    // USER -> Order Create
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderDto createOrderDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(createOrderDto)) ;
    }

    // USER -> My Orders
    @GetMapping("/my")
    public ResponseEntity<List<OrderDto>> getMyOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getMyOrders()) ;
    }

    // USER -> My Orders (Paginated)
    @GetMapping("/my/paginated")
    public ResponseEntity<PageResponseDto<OrderDto>> getMyOrdersPaginated(Pageable pageable) {
        return ResponseEntity.ok(
                orderService.getMyOrdersPaginated(pageable)
        );
    }

    // USER -> Specific Order
    @GetMapping("/my/{id}")
    public ResponseEntity<OrderDto> getMyOrderById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getMyOrderById(id)) ;
    }

    // USER -> Delete Order of Self by ID
    @DeleteMapping("/my/{id}")
    public ResponseEntity<String> deleteMyOrderById(
            @PathVariable Long id) {

        orderService.deleteMyOrderById(id);

        return ResponseEntity.ok(
                "Order deleted successfully with ID: " + id
        );
    }

    // ADMIN -> All Orders
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders()) ;
    }

    @GetMapping("/paginated")
    public ResponseEntity<PageResponseDto<OrderDto>> getAllOrders(Pageable pageable) {
        return ResponseEntity.ok(
                orderService.getAllOrdersPaginated(pageable)
        );
    }

    // ADMIN -> Orders of a User
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getAllOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrdersByUser(userId)) ;
    }

    @GetMapping("/user/{userId}/paginated")
    public ResponseEntity<PageResponseDto<OrderDto>> getAllOrdersByUserPaginated(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(
                orderService.getAllOrdersByUserPaginated(userId, pageable)
        );
    }

    // ADMIN -> Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(
            @PathVariable Long id) {

        System.out.println("🔥 DELETE ORDER CALLED: " + id);

        orderService.deleteOrder(id);

        return ResponseEntity.ok(
                "Order deleted successfully with ID : " + id
        );
    }

}
