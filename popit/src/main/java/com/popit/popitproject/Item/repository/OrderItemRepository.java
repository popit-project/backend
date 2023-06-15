package com.popit.popitproject.Item.repository;

import com.popit.popitproject.Item.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
