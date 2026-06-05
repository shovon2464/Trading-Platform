package com.shovon.tradingserver.dto.response;

import com.shovon.tradingserver.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockSummary {

    private String id;
    private String symbol;
    private String companyName;
    private String iconUrl;
    private Double lastDayTradedPrice;
    private Double currentPrice;

    public static StockSummary fromStock(Stock stock) {
        return StockSummary.builder()
            .id(stock.getId())
            .symbol(stock.getSymbol())
            .companyName(stock.getCompanyName())
            .iconUrl(stock.getIconUrl())
            .lastDayTradedPrice(stock.getLastDayTradedPrice())
            .currentPrice(stock.getCurrentPrice())
            .build();
    }
}
