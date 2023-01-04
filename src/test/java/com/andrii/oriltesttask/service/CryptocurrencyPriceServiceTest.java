package com.andrii.oriltesttask.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.andrii.oriltesttask.model.CryptocurrencyPrice;
import com.andrii.oriltesttask.repository.CryptocurrencyPricesRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class CryptocurrencyPriceServiceTest {
  @MockBean
  private CryptocurrencyPricesRepository cryptocurrencyPricesRepository;

  @InjectMocks
  private CryptocurrencyPriceService cryptocurrencyPriceService;

  private static final String CSV_FILE = "./cryptocurrency-prices.csv";
  private LocalDateTime time = OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime();
  private CryptocurrencyPrice cryptocurrencyBTCPrice = new CryptocurrencyPrice("63b346f12b207611fc867ff3", "BTC", 12341f, time);
  private CryptocurrencyPrice cryptocurrencyETHPrice = new CryptocurrencyPrice("63b346f12b207611fc867ff3", "ETH", 12341f, time);
  private CryptocurrencyPrice cryptocurrencyXRPPrice = new CryptocurrencyPrice("63b346f12b207611fc867ff3", "XRP", 12341f, time);

  private List<CryptocurrencyPrice> list = Arrays.asList(
      new CryptocurrencyPrice("63b346f12b207611fc867ff3", "BTC", 12341f, time),
      new CryptocurrencyPrice("63b346f12b20761zx5ft7ff3", "BTC", 23455f, time),
      new CryptocurrencyPrice("63b346f12b207611fc867ff3", "ETH", 1200f, time),
      new CryptocurrencyPrice("63b346f125106611fc867ff3", "ETH", 1300f, time),
      new CryptocurrencyPrice("63b346gt2b26544564t67ff3", "ETH", 1400f, time),
      new CryptocurrencyPrice("63b3z35de2b207611fc86ff3", "XRP", 200f, time),
      new CryptocurrencyPrice("63b346f12b207611fc789ff3", "XRP", 300f, time),
      new CryptocurrencyPrice("63b34kl42b207611fc867ff3", "XRP", 520f, time));

  @Test
  public void findBTCMinPriceTest() {
    Mockito.doReturn(cryptocurrencyBTCPrice).when(cryptocurrencyPricesRepository).findMinMaxByName("BTC", 1);
    CryptocurrencyPrice result = cryptocurrencyPriceService.findMinMaxPriceByCryptocurrencyName("BTC", 1);
    assertEquals(cryptocurrencyBTCPrice, result);
  }

  @Test
  public void findBTCMaxPriceTest() {
    Mockito.doReturn(cryptocurrencyBTCPrice).when(cryptocurrencyPricesRepository).findMinMaxByName("BTC", -1);
    CryptocurrencyPrice result = cryptocurrencyPriceService.findMinMaxPriceByCryptocurrencyName("BTC", -1);
    assertEquals(cryptocurrencyBTCPrice, result);
  }

  @Test
  public void findETHMinPriceTest() {
    Mockito.doReturn(cryptocurrencyETHPrice).when(cryptocurrencyPricesRepository).findMinMaxByName("ETH", -1);
    CryptocurrencyPrice result = cryptocurrencyPriceService.findMinMaxPriceByCryptocurrencyName("ETH", -1);
    assertEquals(cryptocurrencyETHPrice, result);
  }

  @Test
  public void findETHMaxPriceTest() {
    Mockito.doReturn(cryptocurrencyETHPrice).when(cryptocurrencyPricesRepository).findMinMaxByName("ETH", -1);
    CryptocurrencyPrice result = cryptocurrencyPriceService.findMinMaxPriceByCryptocurrencyName("ETH", -1);
    assertEquals(cryptocurrencyETHPrice, result);
  }

  @Test
  public void findXRPMinPriceTest() {
    Mockito.doReturn(cryptocurrencyXRPPrice).when(cryptocurrencyPricesRepository).findMinMaxByName("XRP", -1);
    CryptocurrencyPrice result = cryptocurrencyPriceService.findMinMaxPriceByCryptocurrencyName("XRP", -1);
    assertEquals(cryptocurrencyXRPPrice, result);
  }

  @Test
  public void findXRPMaxPriceTest() {
    Mockito.doReturn(cryptocurrencyXRPPrice).when(cryptocurrencyPricesRepository).findMinMaxByName("XRP", -1);
    CryptocurrencyPrice result = cryptocurrencyPriceService.findMinMaxPriceByCryptocurrencyName("XRP", -1);
    assertEquals(cryptocurrencyXRPPrice, result);
  }

  @Test
  public void getBTCPageTest() {
    List<CryptocurrencyPrice> sortedList = list.stream()
        .sorted(Comparator.comparingDouble(CryptocurrencyPrice::getPrice))
        .filter(c -> c.getCryptocurrencyName()
            .equals("BTC")).toList();
    Pageable page = PageRequest.of(0, 10);
    Pageable pageable = PageRequest.of(0, 10, Sort.by("price"));
    Mockito.doReturn(toPage(sortedList, page)).when(cryptocurrencyPricesRepository).findCryptocurrencyPriceByCryptocurrencyName("BTC", pageable);
    Mockito.doReturn(sortedList).when(cryptocurrencyPricesRepository).findAll();
    List<CryptocurrencyPrice> result = cryptocurrencyPriceService.getCryptocurrencyPages("BTC", 0, 10);
    assertEquals(sortedList, result);
  }

  @Test
  public void getETHPageTest() {
    List<CryptocurrencyPrice> sortedList = list.stream()
        .sorted(Comparator.comparingDouble(CryptocurrencyPrice::getPrice))
        .filter(c -> c.getCryptocurrencyName()
            .equals("ETH")).toList();
    Pageable page = PageRequest.of(0, 10);
    Pageable pageable = PageRequest.of(0, 10, Sort.by("price"));
    Mockito.doReturn(toPage(sortedList, page)).when(cryptocurrencyPricesRepository).findCryptocurrencyPriceByCryptocurrencyName("ETH", pageable);
    Mockito.doReturn(list).when(cryptocurrencyPricesRepository).findAll();
    List<CryptocurrencyPrice> result = cryptocurrencyPriceService.getCryptocurrencyPages("ETH", 0, 10);
    assertEquals(sortedList, result);
  }

  @Test
  public void getXRPPageTest() {
    List<CryptocurrencyPrice> sortedList = list.stream()
        .sorted(Comparator.comparingDouble(CryptocurrencyPrice::getPrice))
        .filter(c -> c.getCryptocurrencyName()
            .equals("XRP")).toList();
    Pageable page = PageRequest.of(0, 10);
    Pageable pageable = PageRequest.of(0, 10, Sort.by("price"));
    Mockito.doReturn(toPage(sortedList, page)).when(cryptocurrencyPricesRepository).findCryptocurrencyPriceByCryptocurrencyName("XRP", pageable);
    Mockito.doReturn(sortedList).when(cryptocurrencyPricesRepository).findAll();
    List<CryptocurrencyPrice> result = cryptocurrencyPriceService.getCryptocurrencyPages("XRP", 0, 10);
    assertEquals(sortedList, result);
  }

  @Test
  public void createCSV() throws IOException {
    String expected = "Cryptocurrency Name,Min Price,Max Price" +
                      "BTC,12341.0,12341.0" +
                      "ETH,12341.0,12341.0" +
                      "XRP,12341.0,12341.0";
    Mockito.doReturn(cryptocurrencyBTCPrice).when(cryptocurrencyPricesRepository).findMinMaxByName("BTC", -1);
    Mockito.doReturn(cryptocurrencyBTCPrice).when(cryptocurrencyPricesRepository).findMinMaxByName("BTC", 1);
    Mockito.doReturn(cryptocurrencyBTCPrice).when(cryptocurrencyPricesRepository).findMinMaxByName("ETH", -1);
    Mockito.doReturn(cryptocurrencyBTCPrice).when(cryptocurrencyPricesRepository).findMinMaxByName("ETH", 1);
    Mockito.doReturn(cryptocurrencyBTCPrice).when(cryptocurrencyPricesRepository).findMinMaxByName("XRP", -1);
    Mockito.doReturn(cryptocurrencyBTCPrice).when(cryptocurrencyPricesRepository).findMinMaxByName("XRP", 1);
    File result = cryptocurrencyPriceService.createCSVFile("cryptocurrency-prices");
    StringBuilder sb = new StringBuilder();
    String line;
    BufferedReader br = new BufferedReader(new FileReader(CSV_FILE));
    while ((line = br.readLine()) != null) {
      sb.append(line);
    }
    br.close();
    Assert.assertEquals(expected, sb.toString());
    result.delete();
  }

  public Page<?> toPage(List<?> list, Pageable pageable) {
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), list.size());
    if (start > list.size())
      return new PageImpl<>(new ArrayList<>(), pageable, list.size());
    return new PageImpl<>(list.subList(start, end), pageable, list.size());
  }
}

