package com.shovon.tradingserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellStockInput {

  private String holdingId;

  private int quantity;

  public void validate() {
    if (holdingId == null || quantity < 0) {
      throw new RuntimeException("Invalid input");
    }
  }

}
