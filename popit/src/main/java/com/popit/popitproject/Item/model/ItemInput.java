package com.popit.popitproject.Item.model;

import com.popit.popitproject.store.entity.StoreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ItemInput {

  private Long id;

  private String itemNm;

  private Integer price;

  private String itemImgURL;

  private String fileUrl;

  private Integer stockNumber;

  private String itemSellStatus;

  private MultipartFile file;

  private String sellerId;

}

