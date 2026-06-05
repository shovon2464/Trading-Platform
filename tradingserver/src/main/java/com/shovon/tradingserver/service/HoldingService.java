package com.shovon.tradingserver.service;


import com.shovon.tradingserver.dto.request.BuyStockInput;
import com.shovon.tradingserver.dto.request.SellStockInput;
import com.shovon.tradingserver.dto.response.HoldingResponse;
import com.shovon.tradingserver.dto.response.StockSummary;
import com.shovon.tradingserver.model.Holding;
import com.shovon.tradingserver.model.Order;
import com.shovon.tradingserver.model.Stock;
import com.shovon.tradingserver.model.User;
import com.shovon.tradingserver.repository.HoldingRepository;
import com.shovon.tradingserver.repository.OrderRepository;
import com.shovon.tradingserver.repository.StockRepository;
import com.shovon.tradingserver.repository.UserRepository;
import com.shovon.tradingserver.security.CustomUserDetails;
import com.shovon.tradingserver.types.OrderType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HoldingService {

  @Autowired
  private StockRepository stockRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private HoldingRepository holdingRepository;

  @Autowired
  private OrderRepository orderRepository;

  public Holding buy(BuyStockInput buyStockInput, CustomUserDetails userDetails) {
    Optional<Stock> optStock = this.stockRepository.findById(buyStockInput.getStockId());

    if (optStock.isEmpty()) {
      throw new RuntimeException("Stock with this id doesn't exists");
    }

    Stock stock = optStock.get();

    Optional<User> optUser = this.userRepository.findById(userDetails.getId());

    if (optUser.isEmpty()) {
      throw new RuntimeException("User with this id doesn't exists");
    }

    User user = optUser.get();
    Double totalPrice = stock.getCurrentPrice() * buyStockInput.getQuantity();

    if (user.getBalance() < totalPrice) {
      throw new RuntimeException("Insufficient balance");
    }

    user.setBalance(user.getBalance() - totalPrice);
    this.userRepository.save(user);

    Holding holding = Holding.builder()
        .userId(user.getId())
        .stockId(buyStockInput.getStockId())
        .quantity(buyStockInput.getQuantity())
        .buyPrice(stock.getCurrentPrice())
        .build();
    this.holdingRepository.save(holding);

    Order order = Order.builder()
        .user(user)
        .stockId(buyStockInput.getStockId())
        .quantity(buyStockInput.getQuantity())
        .price(stock.getCurrentPrice())
        .orderType(OrderType.BUY)
        .remainingBalance(user.getBalance() - stock.getCurrentPrice())
        .build();
    this.orderRepository.save(order);
    this.holdingRepository.save(holding);
    return holding;
  }

  public Boolean sell(SellStockInput sellStockInput, CustomUserDetails userDetails) {
    Optional<Holding> optHolding = this.holdingRepository.findById(sellStockInput.getHoldingId());

    if (optHolding.isEmpty()) {
      throw new RuntimeException("Holding not found");
    }

    Holding holding = optHolding.get();

    if (sellStockInput.getQuantity() > holding.getQuantity()) {
      throw new RuntimeException("You cannot sell more than you own");
    }

    Optional<Stock> optStock = this.stockRepository.findById(holding.getStockId());

    if (optStock.isEmpty()) {
      throw new RuntimeException("Stock not found");
    }

    Stock stock = optStock.get();
    Double sellPrice = sellStockInput.getQuantity() * stock.getCurrentPrice();

    holding.setQuantity(holding.getQuantity() - sellStockInput.getQuantity());
    if (holding.getQuantity() <= 0) {
      this.holdingRepository.deleteById(holding.getId());
    } else {
      this.holdingRepository.save(holding);
    }

    Optional<User> optUser = this.userRepository.findById(userDetails.getId());

    if (optUser.isEmpty()) {
      throw new RuntimeException("User not found");
    }

    User user = optUser.get();
    user.setBalance(user.getBalance() + sellPrice);
    this.userRepository.save(user);

    Order order = Order.builder().user(user).stockId(holding.getStockId())
        .quantity(sellStockInput.getQuantity()).price(stock.getCurrentPrice())
        .orderType(OrderType.SELL).remainingBalance(user.getBalance()).build();
    this.orderRepository.save(order);

    return true;
  }

  public List<HoldingResponse> getAllHoldings(CustomUserDetails userDetails) {
    List<Holding> holdings = this.holdingRepository.findByUserId(userDetails.getId());

    return holdings.stream()
        .map(holding -> {
            StockSummary stock = this.stockRepository.findById(holding.getStockId())
                .map(StockSummary::fromStock)
                .orElse(null);
            return HoldingResponse.fromHolding(holding, stock);
        })
        .collect(Collectors.toList());
}

}
