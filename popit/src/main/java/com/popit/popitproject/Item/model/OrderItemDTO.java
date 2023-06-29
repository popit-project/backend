package com.popit.popitproject.Item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

  private String itemNm;
  private int price;
  private int quantity;
}