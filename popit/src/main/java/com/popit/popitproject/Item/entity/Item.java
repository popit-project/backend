package com.popit.popitproject.Item.entity;

import com.popit.popitproject.Item.model.ItemInput;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Item {

  @Id
  @Column(name = "item_id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;       //상품 코드

  @Column(nullable = false, length = 50)
  private String itemNm; //상품명

  @Column(name = "price", nullable = false)
  private int price; //가격

  @Column(nullable = false)
  private int stockNumber; //재고수량

  @Lob
  @Column(nullable = false)
  private String itemDetail; //상품 상세 설명

//  @Enumerated(EnumType.STRING)
  private String itemSellStatus; //상품 판매 상태

  private LocalDateTime regTime;
  private LocalDateTime updateTime;


  public void updateItem(ItemInput itemInput) {
    this.itemNm = itemInput.getItemNm();
    this.price = itemInput.getPrice();
    this.stockNumber = itemInput.getStockNumber();
    this.itemDetail = itemInput.getItemDetail();
    this.itemSellStatus = itemInput.getItemSellStatus();
  }

}
