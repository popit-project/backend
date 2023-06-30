package com.popit.popitproject.Item.service;

import com.popit.popitproject.Item.entity.Item;
import com.popit.popitproject.Item.model.ItemInput;
import com.popit.popitproject.Item.repository.ItemRepository;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.model.UserDTO;
import com.popit.popitproject.user.repository.UserRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Service
@AllArgsConstructor
@Data
@ToString
public class ItemService {

  private final ItemRepository itemRepository;
  private final S3Service s3Service;
  private final UserRepository userRepository;


  public Item registerItem(ItemInput itemInput, String userId) throws IOException {

    MultipartFile file = itemInput.getFile();
    String imageUrl = s3Service.uploadFile(file);

    UserEntity userEntity = userRepository.findByUserId(userId);

    if (userEntity == null) {
      throw new IllegalArgumentException("가입 된 회원이 아닙니다.");
    }


    Item item = Item.builder()
        .sellerId(userEntity.getStore().getId())
        .userId(userId)
        .itemNm(itemInput.getItemNm())
        .price(itemInput.getPrice())
        .stockNumber(itemInput.getStockNumber())
        .itemSellStatus(itemInput.getItemSellStatus())
        .itemImgURL(imageUrl)
        .regTime(LocalDateTime.now())
        .updateTime(LocalDateTime.now())
        .build();

    return itemRepository.save(item);
  }

  public List<Item> getfindBySellerId(Long sellerId) {
    return itemRepository.findBySellerId(sellerId);
  }

  public Item updateItemImage(Long id, MultipartFile file) throws IOException {
    Item item = itemRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

    if (file != null) {
      String fileUrl = s3Service.uploadFile(file);
      item.setItemImgURL(fileUrl);
      item.setUpdateTime(LocalDateTime.now());
    }
    return itemRepository.save(item);
  }

  public Item updateItem(Long id, ItemInput itemInput) {
    Item item = itemRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

    if (itemInput.getItemNm() != null) {
      item.setItemNm(itemInput.getItemNm());
    }
    if (itemInput.getPrice() != null) {
      item.setPrice(itemInput.getPrice());
    }
    if (itemInput.getItemSellStatus() != null) {
      item.setItemSellStatus(itemInput.getItemSellStatus());
    }
    if (itemInput.getStockNumber() != null) {
      item.setStockNumber(itemInput.getStockNumber());

      if (itemInput.getStockNumber() > 0) {
        item.setItemSellStatus(null);
      }

    }

    item.setUpdateTime(LocalDateTime.now());

    return itemRepository.save(item);
  }

  public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long id) {
      super("Item with id " + id + " does not exist.");
    }
  }

  public void deleteItem(Long id) {
    Item item = itemRepository.findById(id)
        .orElseThrow(() -> new ItemNotFoundException(id));
    String itemImageUrl = item.getItemImgURL();
    String fileName = itemImageUrl.replace("https://" + s3Service.getBucketName() + ".s3." + s3Service.getRegion() + ".amazonaws.com/", "");
    s3Service.deleteFile(fileName);
    itemRepository.deleteById(id);
  }
}
