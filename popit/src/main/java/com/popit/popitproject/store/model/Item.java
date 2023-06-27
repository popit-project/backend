package com.popit.popitproject.store.model;//package com.popit.popitproject.store.model;
//
//import java.time.LocalDateTime;
//import javax.persistence.Id;
//import javax.validation.constraints.NotBlank;
//import lombok.Data;
//import org.springframework.data.annotation.Transient;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//@Data
//@Document(collection = "item")
//public class Item {
//
//  @Transient
//  public static final String SEQUENCE_NAME = "store_sequence";
//
//  @Id
//  private String id;
//  private Long idx;
//
//  @NotBlank(message = "상품명을 입력해주세요")
//  private String itemName;
//
//  //  @NotEmpty(message = "가격을 입력해주세요")
//  private int price;
//
//  @NotBlank(message = "상품 설명을 입력해주세요")
//  private String itemDetail;
//
//  //  @NotEmpty(message = "수량을 입력해주세요")
//  private int stock;
//
//  private int itemStatus;
//  private LocalDateTime registerDt;
//  private LocalDateTime changeDt;
//  private int postCnt;
//  private int likeCnt;
//
//}
