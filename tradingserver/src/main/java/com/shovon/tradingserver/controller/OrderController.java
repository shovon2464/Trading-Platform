package com.shovon.tradingserver.controller;

import com.shovon.tradingserver.security.CustomUserDetails;
import com.shovon.tradingserver.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<?> getOrders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            return ResponseEntity.ok(this.orderService.getOrders(userDetails));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
