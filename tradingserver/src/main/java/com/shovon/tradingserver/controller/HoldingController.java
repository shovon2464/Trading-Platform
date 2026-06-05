package com.shovon.tradingserver.controller;

import com.shovon.tradingserver.dto.request.BuyStockInput;
import com.shovon.tradingserver.dto.request.SellStockInput;
import com.shovon.tradingserver.security.CustomUserDetails;
import com.shovon.tradingserver.service.HoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/holdings")
public class HoldingController {

  @Autowired
  private HoldingService holdingService;

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/buy")
  public ResponseEntity<?> buyStock(@RequestBody BuyStockInput buyStockInput,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    try {
      buyStockInput.validate();
      return new ResponseEntity<>(this.holdingService.buy(buyStockInput, userDetails), HttpStatus.CREATED);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/sell")
  public ResponseEntity<?> sellStock(@RequestBody SellStockInput sellStockInput,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    try {
      sellStockInput.validate();
      return new ResponseEntity<>(this.holdingService.sell(sellStockInput, userDetails), HttpStatus.CREATED);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping
  public ResponseEntity<?> getAllHoldings(@AuthenticationPrincipal CustomUserDetails userDetails) {
    try {
      return ResponseEntity.ok(this.holdingService.getAllHoldings(userDetails));
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

}
