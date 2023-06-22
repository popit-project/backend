package com.popit.popitproject.Item.model;

import com.popit.popitproject.Item.entity.ItemImg;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class ItemImgDto {

  private Long id;
  private String imgName;
  private String oriImgName;
  private String imgUrl;

  private static ModelMapper modelMapper = new ModelMapper();
  public static ItemImgDto of(ItemImg itemImg){
    return modelMapper.map(itemImg,ItemImgDto.class);
  }

}
