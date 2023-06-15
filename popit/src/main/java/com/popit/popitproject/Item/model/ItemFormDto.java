package com.popit.popitproject.Item.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ItemFormDto {

  private Long id;

  private String itemNm;
  private Integer price;
  private String itemDetail;
  private Integer stockNumber;
  private String itemSellStatus;

  // 상품 저장 후 수정 할때 상품 이미지 정보를 저장하는 리스트
  private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

  // 상품의 이미지 아이디 저장
  private List<Long> itemImgIds = new ArrayList<>();


}
