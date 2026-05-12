package com.shovon.tradingserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "holding")
public class Holding {

    @Id
    private String id;

    private Long userId;

    private String stockId;

    private Integer quantity;

    private Double buyPrice;
}