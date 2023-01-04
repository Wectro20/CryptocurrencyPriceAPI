package com.andrii.oriltesttask.service;

import com.andrii.oriltesttask.exceptions.CryptocurrencyPriceNotFoundException;
import com.andrii.oriltesttask.exceptions.InvalidParamsForThePageException;
import com.andrii.oriltesttask.model.CryptocurrencyPrice;
import com.andrii.oriltesttask.repository.CryptocurrencyPricesRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@Service
public class CryptocurrencyPriceService {
  @Autowired
  public CryptocurrencyPricesRepository cryptocurrencyPricesRepository;

  public CryptocurrencyPrice findMinMaxPriceByCryptocurrencyName(String name, Integer sortOrder) {
    if (cryptocurrencyPricesRepository.findMinMaxByName(name, sortOrder) == null) {
      throw new CryptocurrencyPriceNotFoundException("price of " + name + " cryptocurrency not found");
    }
    return cryptocurrencyPricesRepository.findMinMaxByName(name, sortOrder);
  }

  public List<CryptocurrencyPrice> getCryptocurrencyPages(String name, Integer pageNo, Integer pageSize) {
    if (cryptocurrencyPricesRepository.findAll().isEmpty()) {
      throw new CryptocurrencyPriceNotFoundException("cryptocurrency prices not found");
    } else if (pageNo < 0 || pageSize < 0) {
      throw new InvalidParamsForThePageException("invalid params for the page");
    } else {
      Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("price"));
      Page<CryptocurrencyPrice> pagedResult;
      if (name == null) {
        pagedResult = cryptocurrencyPricesRepository.findAll(pageable);
      } else {
        pagedResult = cryptocurrencyPricesRepository.findCryptocurrencyPriceByCryptocurrencyName(name, pageable);
      }
      return pagedResult.getContent();
    }
  }

  public File createCSVFile(String fileName) throws IOException {
    String[] cryptocurrencyNames = {"BTC", "ETH", "XRP"};
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("./" + fileName + ".csv"));
         CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
             .withHeader("Cryptocurrency Name", "Min Price", "Max Price"));
    ) {
      for (String name : cryptocurrencyNames) {
        csvPrinter.printRecord(name, cryptocurrencyPricesRepository.findMinMaxByName(name, 1).getPrice().toString(),
            cryptocurrencyPricesRepository.findMinMaxByName(name, -1).getPrice().toString());
        csvPrinter.flush();
      }
    }
    return new File("./" + fileName + ".csv");
  }
}
