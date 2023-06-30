package com.popit.popitproject.Item.exception;

public class InsufficientStockException extends RuntimeException{
  public InsufficientStockException(String message) {
    super(message);
  }

}
