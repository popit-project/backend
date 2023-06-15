package com.popit.popitproject.Item.entity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
public class ItemImg {

  @Id
  @Column(name = "item_img_id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String imgName;
  private String oriImgName;
  private String imgUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item;

  public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
    this.oriImgName = oriImgName;
    this.imgName = imgName;
    this.imgUrl = imgUrl;
  }

}
