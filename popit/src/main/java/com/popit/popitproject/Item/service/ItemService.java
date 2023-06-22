package com.popit.popitproject.Item.service;

import com.popit.popitproject.Item.entity.Item;
import com.popit.popitproject.Item.model.ItemDTO;
import com.popit.popitproject.Item.model.ItemInput;
import com.popit.popitproject.Item.repository.ItemRepository;
import com.popit.popitproject.config.SecurityConfig;
import com.popit.popitproject.user.entity.UserEntity;
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

@Service
@AllArgsConstructor
@Data
@ToString
public class ItemService {

  private final ItemRepository itemRepository;

  public Item registerItem(ItemInput itemInput) {
    Item item = Item.builder()
        .itemNm(itemInput.getItemNm())
        .price(itemInput.getPrice())
        .itemDetail(itemInput.getItemDetail())
        .stockNumber(itemInput.getStockNumber())
        .itemSellStatus(itemInput.getItemSellStatus())
        .email(itemInput.getEmail())
        .regTime(LocalDateTime.now())
        .updateTime(LocalDateTime.now())
        .build();

    return itemRepository.save(item);
  }

  public Item getItem(Long id) {
    return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Not Found"));
  }

  public Item updateItem(Long id, ItemInput itemInput) {
    Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

    Optional.ofNullable(itemInput.getItemNm()).ifPresent(item::setItemNm);
    Optional.ofNullable(itemInput.getPrice()).ifPresent(item::setPrice);
    Optional.ofNullable(itemInput.getItemDetail()).ifPresent(item::setItemDetail);
    Optional.ofNullable(itemInput.getItemSellStatus()).ifPresent(item::setItemSellStatus);
    Optional.ofNullable(itemInput.getStockNumber()).ifPresent(item::setStockNumber);

    item.setUpdateTime(LocalDateTime.now());

    return itemRepository.save(item);

  }

  public void deleteItem(Long id) {
    itemRepository.deleteById(id);
  }

  public List<ItemDTO> getItemsByEmail(String email) {
    List<Item> items = itemRepository.findByEmail(email);
    return items.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  private ItemDTO convertToDto(Item item) {
    ItemDTO itemDTO = new ItemDTO();
    itemDTO.setItemNm(item.getItemNm());
    itemDTO.setPrice(item.getPrice());
    itemDTO.setStockNumber(item.getStockNumber());

    return itemDTO;
  }
}
