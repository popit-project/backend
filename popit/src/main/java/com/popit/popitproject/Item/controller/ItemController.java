package com.popit.popitproject.Item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popit.popitproject.Item.entity.Item;
import com.popit.popitproject.Item.model.ItemInput;
import com.popit.popitproject.Item.service.ItemService;
import com.popit.popitproject.Item.service.ItemService.ItemNotFoundException;
import com.popit.popitproject.user.service.JwtTokenService;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller")
public class ItemController {

  private final ItemService itemService;
  private final JwtTokenService jwtTokenService;
  private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
  @ApiOperation(
      value = "상품 등록"
      , notes = "상품을 등록하는 API ")
  @PostMapping(path = "/profile/item/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> register(
      @RequestPart("itemInput") String itemInputStr,
      @RequestPart("file") MultipartFile file,
      HttpServletRequest request) {
    try {
      String token = request.getHeader("Authorization").substring(7);
      if (!jwtTokenService.validateToken(token)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
      }

      String userId = jwtTokenService.getUserIdFromToken(token);

      ObjectMapper objectMapper = new ObjectMapper();
      ItemInput itemInput = objectMapper.readValue(itemInputStr, ItemInput.class);
      itemInput.setFile(file);

      Item item = itemService.registerItem(itemInput, userId);

      return new ResponseEntity<>(item, HttpStatus.CREATED);
    } catch (Exception e) {
      LOGGER.error("Error while processing request" + e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @ApiOperation(
      value = "상품 이미지 수정"
      , notes = "상품의 이미지를 수정")
  @PutMapping(path = "/item/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Item> updateItemImage(@PathVariable Long id, @RequestPart("file") MultipartFile file) throws IOException {
    Item updatedItem = itemService.updateItemImage(id, file);
    return new ResponseEntity<>(updatedItem, HttpStatus.OK);
  }
  @ApiOperation(
      value = "상품 내용 수정"
      , notes = "상품의 수량, 상태를 수정한다. ")
  @PutMapping("/profile/item/update/{id}")
  public ResponseEntity<Item> update(@PathVariable Long id, @RequestBody ItemInput itemInput) {
    Item item = itemService.updateItem(id, itemInput);
    return new ResponseEntity<>(item, HttpStatus.OK);
  }
  @ApiOperation(
      value = "상품 삭제"
      , notes = "상품을 삭제")
  @DeleteMapping("/profile/item/delete/{id}")
  public ResponseEntity<String> delete(@PathVariable Long id) {
    try {
      itemService.deleteItem(id);
      return new ResponseEntity<>("Item deleted successfully", HttpStatus.OK);
    } catch (ItemNotFoundException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }
  @ApiOperation(
      value = "sellerId를 통해 상품검색"
      , notes = "상품을 검색합니다.")
  @GetMapping("/item/{storeId}")
  public List<Item> getItem(@PathVariable Long storeId) {
    return itemService.getfindBySellerId(storeId);
  }


}
