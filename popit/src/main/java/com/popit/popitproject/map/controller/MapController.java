package com.popit.popitproject.map.controller;


import com.popit.popitproject.map.model.MapDTO;
import com.popit.popitproject.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {

    private final MapService mapService;

    @PostMapping("/insert")
    public String insertMap(@RequestBody MapDTO mapDto) {
        mapService.creatMap(mapDto.getId(),mapDto.getStore_address(),mapDto.getStore_name(),mapDto.getStore_phone(),
                mapDto.getStore_time(),mapDto.getLongitude(),mapDto.getLatitude());
        return "성공";
    }
//    @GetMapping("/select")
//    public boolean findMap(String name){
//        return mapService.findMap(name);
//    }

}
