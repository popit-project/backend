package com.popit.popitproject.Item.repository;

import com.popit.popitproject.Item.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
