package com.projeto.e_commerce.order;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ResponseOrderDto> create(@RequestBody @Valid RequestOrderDto req) {
        ResponseOrderDto order = orderService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseOrderDto> findById(@PathVariable UUID id) {
        ResponseOrderDto order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<ResponseOrderDto>> findByCustomer(@PathVariable UUID customerId, Pageable pageable) {
        Page<ResponseOrderDto> orders = orderService.findByCustomer(customerId, pageable);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseOrderDto> updateStatus(@PathVariable UUID id, @RequestBody @Valid UpdateStatusDto req) {
        ResponseOrderDto order = orderService.updateStatus(id, req.status());
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ResponseOrderDto> cancel(@PathVariable UUID id) {
        ResponseOrderDto order = orderService.cancel(id);
        return ResponseEntity.ok(order);
    }
}
