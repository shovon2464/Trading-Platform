package com.shovon.tradingserver.service;

import com.shovon.tradingserver.dto.response.OrderResponse;
import com.shovon.tradingserver.dto.response.StockSummary;
import com.shovon.tradingserver.dto.response.UserSummary;
import com.shovon.tradingserver.model.Order;
import com.shovon.tradingserver.repository.OrderRepository;
import com.shovon.tradingserver.repository.StockRepository;
import com.shovon.tradingserver.security.CustomUserDetails;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private StockRepository stockRepository;


  @Transactional
  public List<OrderResponse> getOrders(CustomUserDetails userDetails) {
    List<Order> orders = this.orderRepository.findByUserIdOrderByCreatedDateDesc(
        userDetails.getId());

    return orders.stream().map(order -> {
      UserSummary user = UserSummary.fromUser(order.getUser());
      StockSummary stock = this.stockRepository.findById(order.getStockId())
          .map(StockSummary::fromStock).orElse(null);
      return OrderResponse.fromOrder(order, user, stock);
    }).collect(Collectors.toList());
  }

}
