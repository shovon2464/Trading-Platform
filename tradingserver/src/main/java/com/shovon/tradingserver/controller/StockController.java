package com.shovon.tradingserver.controller;

import com.shovon.tradingserver.dto.request.StockInput;
import com.shovon.tradingserver.model.Stock;
import com.shovon.tradingserver.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {

  @Autowired
  private StockService stockService;

  @PostMapping("/register")
  public ResponseEntity<?> registerStock(@RequestBody StockInput stockInput) {
    try {
      stockInput.validate();
      return new ResponseEntity<>(this.stockService.register(stockInput), HttpStatus.CREATED);
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }

  @GetMapping
  public ResponseEntity<?> getAllStocks() {
    try {
      return ResponseEntity.ok(stockService.getAllStocks());
    } catch (Exception ex) {
      return ResponseEntity.badRequest().body("Failed to retrieve stocks. " + ex.getMessage());
    }
  }

  @GetMapping("/stock")
  public ResponseEntity<?> getStockBySymbol(@RequestParam("stock") String symbol) {
    try {
      if (symbol == null || symbol.trim().isEmpty()) {
        return ResponseEntity.badRequest()
            .body(java.util.Map.of("msg", "Please provide stock symbol"));
      }

      return ResponseEntity.ok(java.util.Map.of("msg", "Stock retrieved successfully!", "data",
          stockService.getStockBySymbol(symbol)));
    } catch (RuntimeException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(java.util.Map.of("msg", ex.getMessage()));
    } catch (Exception ex) {
      return ResponseEntity.badRequest()
          .body(java.util.Map.of("msg", "Failed to retrieve stock. " + ex.getMessage()));
    }
  }

}
