package com.andrii.oriltesttask.controller;

import com.andrii.oriltesttask.CryptocurrencyPriceApplication;
import com.andrii.oriltesttask.model.CryptocurrencyPrice;
import com.andrii.oriltesttask.repository.CryptocurrencyPricesRepository;
import com.andrii.oriltesttask.service.CryptocurrencyPriceService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = CryptocurrencyPriceApplication.class)
@WebMvcTest
public class CryptocurrencyPriceControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CryptocurrencyPriceService cryptocurrencyPriceService;
  @MockBean
  private CryptocurrencyPricesRepository cryptocurrencyPricesRepository;
  private LocalDateTime time = OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime();
  private static final String CSV_FILE = "./cryptocurrency-report.csv";

  private CryptocurrencyPrice cryptocurrencyBTCPrice = new CryptocurrencyPrice("63b346f12b207611fc867ff3", "BTC", 12341f, time);
  private CryptocurrencyPrice cryptocurrencyETHPrice = new CryptocurrencyPrice("63b346f12b207611fc867ff3", "ETH", 12341f, time);
  private CryptocurrencyPrice cryptocurrencyXRPPrice = new CryptocurrencyPrice("63b346f12b207611fc867ff3", "XRP", 12341f, time);
  private List<CryptocurrencyPrice> list = Arrays.asList(
      new CryptocurrencyPrice("63b346f12b207611fc867ff3", "BTC", 12341f, time),
      new CryptocurrencyPrice("63b346f12b20761zx5ft7ff3", "BTC", 23455f, time),
      new CryptocurrencyPrice("63b346f12b207611fc867ff3", "ETH", 1200f, time),
      new CryptocurrencyPrice("63b346f125106611fc867ff3", "ETH", 1300f, time),
      new CryptocurrencyPrice("63b346gt2b26544564t67ff3", "ETH", 1400f, time),
      new CryptocurrencyPrice("63b3z35de2b207611fc867ff3", "XRP", 200f, time),
      new CryptocurrencyPrice("63b346f12b207611fc789ff3", "XRP", 300f, time),
      new CryptocurrencyPrice("63b34kl42b207611fc867ff3", "XRP", 520f, time));

  @Test
  public void getMinBTCPriceTest() throws Exception {
    Mockito.doReturn(cryptocurrencyBTCPrice).when(cryptocurrencyPriceService).findMinMaxPriceByCryptocurrencyName("BTC", 1);
    String crypPrice = objectMapper.writeValueAsString(cryptocurrencyBTCPrice);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cryptocurrencies/minprice?name=BTC").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(crypPrice));
  }

  @Test
  public void getMaxBTCPriceTest() throws Exception {
    Mockito.doReturn(cryptocurrencyBTCPrice).when(cryptocurrencyPriceService).findMinMaxPriceByCryptocurrencyName("BTC", -1);
    String crypPrice = objectMapper.writeValueAsString(cryptocurrencyBTCPrice);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cryptocurrencies/maxprice?name=BTC").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(crypPrice));
  }

  @Test
  public void getMinETHPriceTest() throws Exception {
    Mockito.doReturn(cryptocurrencyETHPrice).when(cryptocurrencyPriceService).findMinMaxPriceByCryptocurrencyName("ETH", 1);
    String crypPrice = objectMapper.writeValueAsString(cryptocurrencyETHPrice);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cryptocurrencies/minprice?name=ETH").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(crypPrice));
  }

  @Test
  public void getMaxETHPriceTest() throws Exception {
    Mockito.doReturn(cryptocurrencyETHPrice).when(cryptocurrencyPriceService).findMinMaxPriceByCryptocurrencyName("ETH", -1);
    String crypPrice = objectMapper.writeValueAsString(cryptocurrencyETHPrice);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cryptocurrencies/maxprice?name=ETH").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(crypPrice));
  }

  @Test
  public void getMinXRPPriceTest() throws Exception {
    Mockito.doReturn(cryptocurrencyXRPPrice).when(cryptocurrencyPriceService).findMinMaxPriceByCryptocurrencyName("XRP", 1);
    String crypPrice = objectMapper.writeValueAsString(cryptocurrencyXRPPrice);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cryptocurrencies/minprice?name=XRP").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(crypPrice));
  }

  @Test
  public void getMaxXRPPriceTest() throws Exception {
    Mockito.doReturn(cryptocurrencyXRPPrice).when(cryptocurrencyPriceService).findMinMaxPriceByCryptocurrencyName("XRP", -1);
    String crypPrice = objectMapper.writeValueAsString(cryptocurrencyXRPPrice);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cryptocurrencies/maxprice?name=XRP").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(crypPrice));
  }

  @Test
  public void getBTCPage() throws Exception {
    List<CryptocurrencyPrice> sortedList = list.stream()
        .sorted(Comparator.comparingDouble(CryptocurrencyPrice::getPrice))
        .filter(c->c.getCryptocurrencyName()
        .equals("BTC")).toList();
    Mockito.doReturn(sortedList).when(cryptocurrencyPriceService).getCryptocurrencyPages("BTC", 0, 10);
    String crypPrices = objectMapper.writeValueAsString(sortedList);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cryptocurrencies?name=BTC").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(crypPrices));
  }

  @Test
  public void getETHPage() throws Exception {
    List<CryptocurrencyPrice> sortedList = list.stream()
        .sorted(Comparator.comparingDouble(CryptocurrencyPrice::getPrice))
        .filter(c->c.getCryptocurrencyName()
        .equals("ETH")).toList();
    Mockito.doReturn(sortedList).when(cryptocurrencyPriceService).getCryptocurrencyPages("ETH", 0, 10);
    String crypPrices = objectMapper.writeValueAsString(sortedList);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cryptocurrencies?name=ETH").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(crypPrices));
  }

  @Test
  public void getXRPPage() throws Exception {
    List<CryptocurrencyPrice> sortedList = list.stream()
        .sorted(Comparator.comparingDouble(CryptocurrencyPrice::getPrice))
        .filter(c->c.getCryptocurrencyName()
        .equals("XRP")).toList();
    Mockito.doReturn(sortedList).when(cryptocurrencyPriceService).getCryptocurrencyPages("XRP", 0, 10);
    String crypPrices = objectMapper.writeValueAsString(sortedList);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/cryptocurrencies?name=XRP").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(crypPrices));
  }

  @Test
  public void getCSV() throws Exception {
    String expected = "Cryptocurrency Name,Min Price,Max Price" +
        "BTC,12341.0,12341.0" +
        "ETH,12341.0,12341.0" +
        "XRP,12341.0,12341.0";
    File csvFile = createCSVFile("cryptocurrency-report");
    System.out.println(csvFile);
    Mockito.doReturn(csvFile).when(cryptocurrencyPriceService).createCSVFile("cryptocurrency-report");
    mockMvc.perform(MockMvcRequestBuilders.get("/cryptocurrencies/csv")).andReturn().getResponse();
    StringBuilder sb = new StringBuilder();
    String line;
    BufferedReader br = new BufferedReader(new FileReader(CSV_FILE));
    while ((line = br.readLine()) != null) {
      sb.append(line);
    }
    br.close();
    Assert.assertEquals(expected, sb.toString());
    csvFile.delete();
  }

  public File createCSVFile(String fileName) throws IOException {
    String[] cryptocurrencyNames = {"BTC", "ETH", "XRP"};
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("./"+fileName+".csv"));
         CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
             .withHeader("Cryptocurrency Name", "Min Price", "Max Price"));
    ) {
      for (String name : cryptocurrencyNames) {
        csvPrinter.printRecord(name, "12341.0",
            "12341.0");
        csvPrinter.flush();
      }
    }
    return new File("./"+fileName+".csv");
  }
}
