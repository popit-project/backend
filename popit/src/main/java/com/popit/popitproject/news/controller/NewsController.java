package com.popit.popitproject.news.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popit.popitproject.common.exception.ResponseDTO;
import com.popit.popitproject.news.entity.NewsEntity;
import com.popit.popitproject.news.model.NewsDTO;
import com.popit.popitproject.news.model.NewsListResponseDTO;
import com.popit.popitproject.news.service.NewsService;
import com.popit.popitproject.notification.service.NotificationService;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.user.entity.UserEntity;
import io.jsonwebtoken.io.IOException;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NewsController {

    private final NewsService newsService;
    private final StoreSellerRepository sellerRepository;
    private final NotificationService notificationService;

    @ApiOperation(
        value = "소식 등록",
        notes = "소식 작성을 하고 저장/등록합니다."
    )
    @PostMapping(path = "/seller/news", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<NewsDTO>> createNews(
            @AuthenticationPrincipal String userId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("dto") String dtoJson
    ) throws IOException {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NewsDTO dto = objectMapper.readValue(dtoJson, NewsDTO.class);

            UserEntity user = newsService.getUserById(userId);
            StoreEntity store = sellerRepository.findById(user.getStore().getId())
                    .orElseThrow();

            NewsEntity entity = newsService.convertToEntity(dto);
            entity.setStoreName(store.getStoreName());
            entity.setCity(store.getStoreAddress());
            entity.setCreateTime(LocalDateTime.now());
            entity.setSeller(user.getStore());

            List<NewsEntity> entities = newsService.createNews(file, dto, user, store);

            notificationService.newsNotification(store);

            List<NewsDTO> dtos = entities.stream().map(NewsDTO::new).collect(Collectors.toList());
            ResponseDTO<NewsDTO> response = ResponseDTO.getResponseDTO(dtos);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseDTO.createErrorResponse(e.getMessage());
        }
    }




    @ApiOperation(
        value = "소식 목록",
        notes = "가게에 해당하는 소식글 리스트를 불러옵니다."
    )
    @GetMapping("/{storeName}/news")
    public ResponseEntity<ResponseDTO<NewsDTO>> retrieveNewsList(@PathVariable String storeName) {
        try {
            StoreEntity store = newsService.getStoreByName(storeName);
            List<NewsEntity> entities = newsService.retrieve(store.getId());
            List<NewsListResponseDTO> newsResponses = new ArrayList<>();

            for (NewsEntity entity : entities) {
                NewsListResponseDTO newsResponse = NewsListResponseDTO.toDTO(entity);
                newsResponses.add(newsResponse);
            }

            ResponseDTO<NewsDTO> response = ResponseDTO.getResponseListDTO(newsResponses);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseDTO.createErrorResponse(e.getMessage());
        }
    }


    @ApiOperation(
        value = "소식 삭제"
        , notes = "가게에 해당하는 목록 가져온 뒤 소식 id로 소식글을 삭제합니다.")
    @DeleteMapping("/seller/news/{newsId}")
    public ResponseEntity<ResponseDTO<NewsDTO>> deleteNews(
        @AuthenticationPrincipal String userId, @PathVariable Long newsId) {

        try {
            UserEntity user = newsService.getUserById(userId);
            StoreEntity seller = newsService.getSellerByUser(user);
            NewsEntity entity = newsService.retrieveById(newsId);

            entity.setSeller(seller);

            List<NewsEntity> entities = newsService.delete(entity);
            List<NewsDTO> dtos = entities.stream().map(NewsDTO::new).collect(Collectors.toList());

            ResponseDTO<NewsDTO> response = ResponseDTO.getResponseDTO(
                dtos);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseDTO.createErrorResponse(e.getMessage());
        }
    }

}

