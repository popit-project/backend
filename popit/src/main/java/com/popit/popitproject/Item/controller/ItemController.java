package com.popit.popitproject.Item.controller;

import com.popit.popitproject.Item.entity.Item;
import com.popit.popitproject.Item.model.ItemDTO;
import com.popit.popitproject.Item.model.ItemInput;
import com.popit.popitproject.Item.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class ItemController {

  private final ItemService itemService;

  @PostMapping("/profile/item/add")
  public ResponseEntity<Item> register(@RequestBody  ItemInput itemInput) {
    Item item = itemService.registerItem(itemInput);
    return new ResponseEntity<>(item, HttpStatus.CREATED);
  }

  @PutMapping("/profile/item/update/{id}")
  public ResponseEntity<Item> update(@PathVariable Long id, @RequestBody ItemInput itemInput ) {
    Item item = itemService.updateItem(id, itemInput);
    return new ResponseEntity<>(item, HttpStatus.OK);
  }
  @DeleteMapping("/profile/item/{id}")
  public ResponseEntity<String> deleteItem(@PathVariable Long id) {
    itemService.deleteItem(id);
    return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
  }

  @GetMapping("/item/{email}")
  public List<ItemDTO> getItemByEmail(@PathVariable String email) {
    System.out.println("email = " + email);
    return itemService.getItemsByEmail(email);
  }
}