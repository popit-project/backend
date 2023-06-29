package com.popit.popitproject.Item.entity;

import com.popit.popitproject.Item.model.ItemInput;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.user.entity.UserEntity;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
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

//  @Column(name = "email")
//  private String email;

  private String userId; // 임시

  @Column(nullable = false, length = 50)
  private String itemNm; //상품명

  @Column(name = "price", nullable = false)
  private int price; //가격

  @Column(nullable = false)
  private int stockNumber; //재고수량

  @Lob
  private String itemImgURL; //상품 상세 설명

//  @Enumerated(EnumType.STRING)
  private String itemSellStatus; //상품 판매 상태

  private Long sellerId;

  private LocalDateTime regTime;
  private LocalDateTime updateTime;

}
