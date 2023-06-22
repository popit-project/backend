package com.popit.popitproject.store.controller;


import com.popit.popitproject.store.service.StoreBusinessService;
import com.popit.popitproject.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {

    private final StoreBusinessService storeBusinessService;
    private final StoreRepository storeRepository;

}