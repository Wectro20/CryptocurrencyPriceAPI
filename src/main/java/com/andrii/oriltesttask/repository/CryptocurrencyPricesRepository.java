package com.andrii.oriltesttask.repository;

import com.andrii.oriltesttask.model.CryptocurrencyPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CryptocurrencyPricesRepository extends MongoRepository<CryptocurrencyPrice, String> {
  @Aggregation(pipeline = {
      "{ '$match': { 'cryptocurrencyName' : ?0 } }",
      "{ '$sort' : { 'price' : ?1} }",
      "{ '$limit' : 1 }"
  })
  CryptocurrencyPrice findMinMaxByName(String cryptocurrencyName, int sort);

  Page<CryptocurrencyPrice> findCryptocurrencyPriceByCryptocurrencyName(String name, Pageable pageable);
}