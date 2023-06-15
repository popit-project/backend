package com.popit.popitproject.Item.service;

import com.popit.popitproject.Item.entity.Item;
import com.popit.popitproject.Item.model.ItemInput;
import com.popit.popitproject.Item.repository.ItemRepository;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
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
        .build();

    return itemRepository.save(item);
  }

  public Item getItem(Long id) {
    return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Not Found"));
  }

  public Item updateItem(Long id, ItemInput itemInput) {
    Item item = getItem(id);

    Optional.ofNullable(itemInput.getItemNm()).ifPresent(item::setItemNm);
    Optional.ofNullable(itemInput.getPrice()).ifPresent(item::setPrice);
    Optional.ofNullable(itemInput.getItemDetail()).ifPresent(item::setItemDetail);
    Optional.ofNullable(itemInput.getItemSellStatus()).ifPresent(item::setItemSellStatus);
    Optional.ofNullable(itemInput.getStockNumber()).ifPresent(item::setStockNumber);

    return itemRepository.save(item);

  }

  public void deleteItem(Long id) {
    itemRepository.deleteById(id);
  }
}
