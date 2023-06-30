package com.popit.popitproject.Item.controller;

import com.popit.popitproject.Item.exception.InsufficientStockException;
import com.popit.popitproject.Item.exception.ItemNotFoundException;
import com.popit.popitproject.Item.model.OrderRequestDTO;
import com.popit.popitproject.Item.service.OrderService;
import com.popit.popitproject.user.service.JwtTokenService;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final JwtTokenService jwtTokenService;

  @ApiOperation(
      value = "상품을 주문"
      , notes = "상품을 주문하는 api")
  @PostMapping("/order")
  public ResponseEntity<String> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO,
      HttpServletRequest request) {
    String token = request.getHeader("Authorization").substring(7); // Extract token
    if (!jwtTokenService.validateToken(token)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
    }

    String orderUserId = jwtTokenService.getSellerIdFromToken(token);
    try {
      orderService.placeOrders(orderRequestDTO.getOrderItems(), orderUserId);
      return ResponseEntity.ok("Order placed successfully");
    } catch (ItemNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (InsufficientStockException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}