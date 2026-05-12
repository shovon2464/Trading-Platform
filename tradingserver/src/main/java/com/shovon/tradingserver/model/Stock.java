package com.shovon.tradingserver.model;

import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stock")
public class Stock {

  @Id
  private String id;

  @Indexed(unique = true)
  private String symbol;

  private String companyName;

  private String iconUrl;

  private Double lastDayTradedPrice;

  private Double currentPrice;

  @Builder.Default
  private List<Map<String, Object>> dayTimeSeries = new ArrayList<>();

  @Builder.Default
  private List<Map<String, Object>> tenMinTimeSeries = new ArrayList<>();


}
