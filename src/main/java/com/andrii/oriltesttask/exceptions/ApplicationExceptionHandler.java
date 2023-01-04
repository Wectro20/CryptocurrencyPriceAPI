package com.andrii.oriltesttask.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApplicationExceptionHandler {
  @ResponseBody
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ExceptionHandler(CryptocurrencyPriceNotFoundException.class)
  public ErrorResponse handleCryptocurrencyPriceNotFoundException(CryptocurrencyPriceNotFoundException e) {
    return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), e.getMessage());
  }

  @ResponseBody
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidParamsForThePageException.class)
  public ErrorResponse handleInvalidParamsForThePageException(InvalidParamsForThePageException e) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class ErrorResponse {
    private String errorCode;
    private String errorMessage;
  }
}
