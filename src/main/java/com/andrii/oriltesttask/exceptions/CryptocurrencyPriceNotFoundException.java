package com.andrii.oriltesttask.exceptions;

public class CryptocurrencyPriceNotFoundException extends RuntimeException{
  public CryptocurrencyPriceNotFoundException(String message) {
    super(message);
  }
}
