package com.popit.popitproject.Item.controller;

import com.popit.popitproject.Item.model.OrderRequestDTO;
import com.popit.popitproject.Item.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @ApiOperation(
      value = "상품을 주문"
      , notes = "상품을 주문하는 api")
  @PostMapping("/order")
  public ResponseEntity<String> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
    try {
      orderService.placeOrders(orderRequestDTO.getOrderItems());
      return ResponseEntity.ok("Order placed successfully");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
