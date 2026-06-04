package com.shovon.tradingserver.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockInput {

  private String symbol;

  private String companyName;

  private String iconUrl;

  private Double lastDayTradedPrice;

  private Double currentPrice;

  public void validate() {
    if (this.symbol == null || this.companyName == null
        || this.iconUrl == null || this.lastDayTradedPrice == null
        || this.currentPrice == null) {
      throw new IllegalArgumentException("All fields are required");
    }
  }
}
