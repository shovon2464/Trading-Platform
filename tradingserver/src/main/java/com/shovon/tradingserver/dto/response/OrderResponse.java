package com.shovon.tradingserver.dto.response;

import com.shovon.tradingserver.model.Order;
import com.shovon.tradingserver.types.OrderType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private UserSummary user;
    private StockSummary stock;
    private Double price;
    private int quantity;
    private OrderType orderType;
    private Double remainingBalance;
    private LocalDateTime createdDate;

    public static OrderResponse fromOrder(Order order, UserSummary user, StockSummary stock) {
        return OrderResponse.builder()
            .id(order.getId())
            .user(user)
            .stock(stock)
            .price(order.getPrice())
            .quantity(order.getQuantity())
            .orderType(order.getOrderType())
            .remainingBalance(order.getRemainingBalance())
            .createdDate(order.getCreatedDate())
            .build();
    }
}
