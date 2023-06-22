package com.popit.popitproject.Item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ItemInput {

  private Long id;

  private String itemNm;

  private Integer price;

  private String itemDetail;

  private Integer stockNumber;

  private String itemSellStatus;

  private String email;

}
