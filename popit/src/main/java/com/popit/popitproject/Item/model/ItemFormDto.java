package com.popit.popitproject.Item.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ItemFormDto {

  private Long id;

  private String itemNm;
  private Integer price;
  private Integer stockNumber;
  private String itemSellStatus;

}
