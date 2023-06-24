package com.popit.popitproject.Item.service;

import com.popit.popitproject.Item.entity.Item;
import com.popit.popitproject.Item.model.ItemDTO;
import com.popit.popitproject.Item.model.ItemInput;
import com.popit.popitproject.Item.repository.ItemRepository;
import com.popit.popitproject.config.SecurityConfig;
import com.popit.popitproject.user.entity.UserEntity;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Data
@ToString
public class ItemService {

  private final ItemRepository itemRepository;
  private final S3Service s3Service;

  public Item registerItem(ItemInput itemInput) throws IOException {

    MultipartFile file = itemInput.getFile();
    String imageUrl = s3Service.uploadFile(file);

    Item item = Item.builder()
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

  public List<Item> getItemsByUserId(String userId) {
    return itemRepository.findByUserId(userId);
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
    Optional<Item> optionalItem = itemRepository.findById(id);
    if (optionalItem.isPresent()) {
      itemRepository.delete(optionalItem.get());
    } else {
      throw new ItemNotFoundException(id);
    }
  }




}
