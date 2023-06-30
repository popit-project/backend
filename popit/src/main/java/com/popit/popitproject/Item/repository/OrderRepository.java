package com.popit.popitproject.Item.repository;

import com.popit.popitproject.Item.entity.Item;
import com.popit.popitproject.Item.entity.OrderList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderList, Long> {

  @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM OrderList o WHERE o.item = :item AND o.quantity <= :quantity")
  boolean hasSufficientStockQuantity(@Param("item") Item item, @Param("quantity") int quantity);
}
