package com.shovon.tradingserver.dto.response;

import com.shovon.tradingserver.model.Holding;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HoldingResponse {

    private String id;
    private Long userId;
    private Integer quantity;
    private Double buyPrice;
    private StockSummary stock;

    public static HoldingResponse fromHolding(Holding holding, StockSummary stock) {
        return HoldingResponse.builder()
            .id(holding.getId())
            .userId(holding.getUserId())
            .quantity(holding.getQuantity())
            .buyPrice(holding.getBuyPrice())
            .stock(stock)
            .build();
    }
}
