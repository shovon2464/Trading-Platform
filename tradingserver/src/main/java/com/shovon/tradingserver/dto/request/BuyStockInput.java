package com.shovon.tradingserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyStockInput {
  private String stockId;

  private Integer quantity;

  public void validate() {
    if (this.stockId == null || this.quantity == null || this.quantity <= 0) {
      throw new IllegalArgumentException("All fields are required");
    }
  }

}
