package com.popit.popitproject.Item.controller;

import com.popit.popitproject.Item.entity.Item;
import com.popit.popitproject.Item.model.ItemInput;
import com.popit.popitproject.Item.repository.ItemRepository;
import com.popit.popitproject.Item.service.ItemService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/item")
public class ItemController {

  private final ItemService itemService;

  @PostMapping("/register")
  public Item register(@RequestBody  ItemInput itemInput) {

    return itemService.registerItem(itemInput);
  }
  @PutMapping("/update/{id}")
  public Item update(@PathVariable Long id, @RequestBody ItemInput itemInput ) {

    return itemService.updateItem(id, itemInput);

  }

  @DeleteMapping("/delete/{id}")
  public String deleteItem(@PathVariable Long id){
    itemService.deleteItem(id);
    return "Successfully";
  }


}
