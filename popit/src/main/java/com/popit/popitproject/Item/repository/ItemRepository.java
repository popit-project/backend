package com.popit.popitproject.Item.repository;

import com.popit.popitproject.Item.entity.Item;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
  List<Item> findBySellerId(Long SellerId);
  Item findByItemNm(String itemNm);

}

