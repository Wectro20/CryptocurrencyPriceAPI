package com.andrii.oriltesttask.repository.controller;

import com.andrii.oriltesttask.model.CryptocurrencyPrice;
import com.andrii.oriltesttask.service.CryptocurrencyPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping()
public class CryptocurrencyPriceController {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CryptocurrencyPriceService cryptocurrencyPriceService;


  @GetMapping("/cryptocurrencies/minprice")
  public ResponseEntity<CryptocurrencyPrice> getMinCryptocurrencyPrice(@RequestParam() String name) {
    return new ResponseEntity<>(cryptocurrencyPriceService.findMinMaxPriceByCryptocurrencyName(name, 1), HttpStatus.OK);
  }

  @GetMapping("/cryptocurrencies/maxprice")
  public ResponseEntity<CryptocurrencyPrice> getMaxCryptocurrencyPrice(@RequestParam() String name) {
    return new ResponseEntity<>(cryptocurrencyPriceService.findMinMaxPriceByCryptocurrencyName(name, -1), HttpStatus.OK);
  }

  @GetMapping("/cryptocurrencies")
  public ResponseEntity<List<CryptocurrencyPrice>> getCryptocurrencyByPages(@RequestParam(required = false) String name,
                                                                            @RequestParam() Optional<Integer> page,
                                                                            @RequestParam() Optional<Integer> size) {
    return new ResponseEntity<>(cryptocurrencyPriceService.getCryptocurrencyPages(name, page.orElse(0), size.orElse(10)), HttpStatus.OK);
  }

  @GetMapping("/cryptocurrencies/csv")
  public ResponseEntity downloadCSV(@RequestParam(required = false) Optional<String> fileName) throws IOException {
    File file = cryptocurrencyPriceService.createCSVFile(fileName.orElse("cryptocurrency-report"));

    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=cryptocurrency-report.csv")
        .contentLength(file.length())
        .contentType(MediaType.parseMediaType("text/csv"))
        .body(new FileSystemResource(file));
  }
}
