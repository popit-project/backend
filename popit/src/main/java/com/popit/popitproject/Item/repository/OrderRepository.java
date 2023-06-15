package com.popit.popitproject.Item.repository;

import com.popit.popitproject.Item.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
