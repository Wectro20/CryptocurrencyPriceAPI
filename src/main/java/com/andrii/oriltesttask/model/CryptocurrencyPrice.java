package com.andrii.oriltesttask.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "statistic")
public class CryptocurrencyPrice {
  @Id
  private String id;
  private String cryptocurrencyName;
  private Float price;
  private LocalDateTime createdTime;
}
