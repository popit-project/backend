package com.popit.popitproject.store.controller;//package com.popit.popitproject.store.controller;
//
//import com.popit.popitproject.store.model.Item;
//import com.popit.popitproject.store.service.ItemService;
//import javax.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class ItemController {
//
//  private final ItemService itemService;
//
//  @GetMapping("/profile/item/add")
//  public ResponseEntity<?> addItem(@RequestBody @Valid Item item) {
//
//    itemService.addItem(item);
//
//    return ResponseEntity.ok().build();
//  }
//
//  @GetMapping("/profile/item/delete/{id}")
//  public ResponseEntity<?> deleteItem(@PathVariable Long id){
//
//    itemService.deleteItem(id);
//
//    return ResponseEntity.ok().build();
//  }
//
//  @GetMapping("/profile/item/update/{id}")
//  public ResponseEntity<?> updateItem(@RequestBody Item item, @PathVariable Long id){
//
//    itemService.updateItem(item, id);
//
//    return ResponseEntity.ok().build();
//  }
//
//  @GetMapping("/profile/item/find/{id}")
//  public ResponseEntity<?> findItem(@PathVariable Long id){
//
//    itemService.findItem(id);
//
//    return ResponseEntity.ok().build();
//  }
//
//}
