package com.popit.popitproject.Item.service;

import com.popit.popitproject.Item.entity.Item;
import com.popit.popitproject.Item.entity.Order;
import com.popit.popitproject.Item.model.OrderItemDTO;
import com.popit.popitproject.Item.model.OrderRequestDTO;
import com.popit.popitproject.Item.repository.ItemRepository;
import com.popit.popitproject.Item.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class OrderService {

  private final ItemRepository itemRepository;
  private final OrderRepository orderRepository;

  public void placeOrders(List<OrderItemDTO> orderItems) {
    for (OrderItemDTO orderItem : orderItems) {
      Item item = itemRepository.findByItemNm(orderItem.getItemNm());
      if (item == null) {
        throw new IllegalArgumentException("찾을수 없는 상품입니다.");
      }

      if (item.getStockNumber() < orderItem.getQuantity()) {
        throw new IllegalArgumentException("재고를 확인해주세요");
      }


      Order order = Order.builder()
          .item(item)
          .price(orderItem.getPrice())
          .quantity(orderItem.getQuantity())
          .orderTime(LocalDateTime.now())
          .build();

      item.setStockNumber(item.getStockNumber() - orderItem.getQuantity());

      if (item.getStockNumber() <= 0) {
        item.setItemSellStatus("SOLD_OUT");
      }

      itemRepository.save(item);
      orderRepository.save(order);
    }
  }
}

