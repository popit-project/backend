package com.popit.popitproject.news.controller;


import com.popit.popitproject.common.ResponseDTO;
import com.popit.popitproject.news.entity.NewsEntity;
import com.popit.popitproject.news.model.NewsDTO;
import com.popit.popitproject.news.service.NewsService;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.user.entity.UserEntity;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller")
public class NewsController {

    private final NewsService newsService;
    private final StoreSellerRepository sellerRepository;

    @PostMapping("/news")
    public ResponseEntity<ResponseDTO<NewsDTO>> createNews(
        @AuthenticationPrincipal String userId, @RequestBody NewsDTO dto) {
        try {
            UserEntity user = newsService.getUserById(userId);
            NewsEntity entity = newsService.convertToEntity(dto);

            StoreEntity store = sellerRepository.findById(user.getStore().getId())
                .orElseThrow();

            entity.setStoreName(store.getStoreName());
            entity.setCity(store.getStoreAddress()); // 동만 나오게
            entity.setCreateTime(LocalDateTime.now());
            entity.setSeller(user.getStore());

            List<NewsEntity> entities = newsService.create(entity);
            List<NewsDTO> dtos = entities.stream().map(NewsDTO::new).collect(Collectors.toList());

            ResponseDTO<NewsDTO> response = newsService.getResponseDTO(
                dtos);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return newsService.createErrorResponse(e.getMessage());
        }
    }

    // Todo : 스토어에 해당하는 목록 가져오기, 내려주는 객체 당 목록 값 바꾸기
    @GetMapping("/{storeName}/news")
    public ResponseEntity<ResponseDTO<NewsDTO>> retrieveNewsList(@PathVariable String storeName) {

        try {
            StoreEntity store = newsService.getStoreByName(storeName);
            List<NewsEntity> entities = newsService.retrieve(store.getId());

            List<NewsDTO> dtos = entities.stream().map(NewsDTO::new).collect(Collectors.toList());

            ResponseDTO<NewsDTO> response = newsService.getResponseDTO(
                dtos);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return newsService.createErrorResponse(e.getMessage());
        }
    }

    // Todo : 스토어에 해당하는 목록 가져오기, id로  가져온 news 삭제
    @DeleteMapping("/{storeName}/news/{newsId}")
    public ResponseEntity<ResponseDTO<NewsDTO>> deleteNews(
        @AuthenticationPrincipal String userId, @PathVariable Long newsId) {


        try {
            UserEntity user = newsService.getUserById(userId);
            StoreEntity seller = newsService.getSellerByUser(user);
            NewsEntity entity = newsService.retrieveById(newsId);

            entity.setSeller(seller);

            List<NewsEntity> entities = newsService.delete(entity);
            List<NewsDTO> dtos = entities.stream().map(NewsDTO::new).collect(Collectors.toList());

            ResponseDTO<NewsDTO> response = newsService.getResponseDTO(
                dtos);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return newsService.createErrorResponse(e.getMessage());
        }
    }



}
