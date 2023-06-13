package com.popit.popitproject.map.controller;


import com.popit.popitproject.map.entity.MapEntity;
import com.popit.popitproject.map.model.MapDTO;
import com.popit.popitproject.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {

    private final MapService mapService;

    @PostMapping("/insert")
    public String insertMap(@RequestBody MapDTO mapDto) {
        mapService.creatMap(mapDto.getId(),mapDto.getStoreAddress(),mapDto.getStoreName(),mapDto.getStorePhone(),
                mapDto.getStoreDay(), mapDto.getStoreTime(),mapDto.getLongitude(),mapDto.getLatitude());
        return "지도등록완료";
    }
    @GetMapping("/search/{storeName}")
    public MapEntity findMap(@PathVariable("storeName") String storeName){
        MapEntity mapEntity = mapService.findMap(storeName);
        return mapEntity;
    }
    @GetMapping("/findAll")
    public List<MapEntity> findMapAll(){
        return mapService.findMapAll();
    }

}
