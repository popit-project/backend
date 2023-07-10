package com.popit.popitproject.Item.service;

import com.popit.popitproject.Item.entity.Item;
import com.popit.popitproject.Item.entity.OrderList;
import com.popit.popitproject.Item.exception.InsufficientStockException;
import com.popit.popitproject.Item.exception.ItemNotFoundException;
import com.popit.popitproject.Item.model.OrderItemDTO;
import com.popit.popitproject.Item.repository.ItemRepository;
import com.popit.popitproject.Item.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class OrderService {

  private final ItemRepository itemRepository;
  private final OrderRepository orderRepository;

  @Transactional
  public void placeOrders(List<OrderItemDTO> orderItems, String orderUserId) {
    for (OrderItemDTO orderItem: orderItems) {
      Item item = itemRepository.findByItemNm(orderItem.getItemNm());
      if (item == null) {
        throw new ItemNotFoundException(orderItem.getItemNm() + "는 없는 상품입니다.");
      }

      if (item.getStockNumber() < orderItem.getQuantity()) {
        throw new InsufficientStockException(orderItem.getItemNm() + "의 재고는 현재 : " + item.getStockNumber() + "개 입니다.");
      }

      OrderList order = OrderList.builder()
          .orderUserId(orderUserId)
          .item(item)
          .price(orderItem.getPrice())
          .quantity(orderItem.getQuantity())
          .orderTime(LocalDateTime.now())
          .build();

      updateItemStock(item, orderItem.getQuantity());

      orderRepository.save(order);
    }
  }

  private void updateItemStock(Item item, int quantity) {
    int updatedStockNumber = item.getStockNumber() - quantity;

    if (updatedStockNumber < 0) {
      throw new InsufficientStockException("재고를 확인해주세요");
    }

    item.setStockNumber(updatedStockNumber);

    if (updatedStockNumber == 0) {
      item.setItemSellStatus("SOLD_OUT");
    }

    itemRepository.save(item);
  }
}

