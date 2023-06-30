package com.popit.popitproject.news.service;

import com.popit.popitproject.Item.service.S3Service;
import com.popit.popitproject.common.ResponseDTO;
import com.popit.popitproject.news.entity.NewsEntity;
import com.popit.popitproject.news.model.NewsDTO;
import com.popit.popitproject.news.model.NewsListResponseDTO;
import com.popit.popitproject.news.repository.NewsRepository;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final StoreSellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final KakaoAddressParseService kakaoAddressParse;


    public List<NewsEntity> createNews(MultipartFile file, NewsDTO dto, UserEntity user,
        StoreEntity store) throws java.io.IOException {

        // 받은 데이터를 엔티티로 변환
        NewsEntity newsEntity = convertToEntity(dto);
        String imageUrl = s3Service.uploadFile(file);

        newsEntity.setStoreName(store.getStoreName());
        newsEntity.setCity(kakaoAddressParse.parseAddress(store.getStoreAddress())); // 동만 나오게
        newsEntity.setCreateTime(LocalDateTime.now());
        newsEntity.setSeller(user.getStore());
        newsEntity.setNewsImgURL(imageUrl);
        newsEntity.setNewsNumber(newsRepository.countNewsByStoreId(store.getId())+1);

        return newsSaveToList(newsEntity);
    }

    // 생성 create
    @Transactional
    public List<NewsEntity> newsSaveToList(final NewsEntity entity) {
        // Validations
        validate(entity);
        // 엔티티 저장
        newsRepository.save(entity);

        log.info("Entity Id : {} is saved", entity.getId());

        return newsRepository.findBySeller(entity.getSeller()).orElseThrow(
            ()-> new RuntimeException("사용자 정보 x")
        );
    }

    public String formatTimeAgo(LocalDateTime createTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(createTime, currentDateTime);

        if (minutes < 60) {
            return minutes + "분 전";
        } else if (minutes < 24 * 60) {
            long hours = minutes / 60;
            return hours + "시간 전";
        } else if (minutes < 30 * 24 * 60) {
            long days = minutes / (24 * 60);
            return days + "일 전";
        } else if (minutes < 12 * 30 * 24 * 60) {
            long months = minutes / (30 * 24 * 60);
            return months + "달 전";
        } else {
            long years = minutes / (12 * 30 * 24 * 60);
            return years + "년 전";
        }
    }

    // 컨트롤러(DTO)에서 생성된 엔티티가 있는지에 대한 검증
    private void validate(NewsEntity entity) {
        if (entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if (entity.getSeller().getId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

    // 검색
    public List<NewsEntity> retrieve(final Long id) {

        StoreEntity seller = sellerRepository.findById(id).orElseThrow();

        return newsRepository.findBySeller(seller).orElseThrow(
            ()-> new RuntimeException("사용자 정보 x")
        );
    }

    // 검색
    public NewsEntity retrieveById(final Long newsId) {
        return newsRepository.findById(newsId)
            .orElseThrow(() -> new RuntimeException("뉴스 아이디에 해당하는 글을 찾을 수 없습니다."));
    }

    // 삭제
    public List<NewsEntity> delete(final NewsEntity entity) {
        // 저장할 엔티티가 유효한지 확인
        validate(entity);
        try {
            // 엔티티 삭제
            newsRepository.delete(entity);
        } catch (Exception e) {
            // 예외 발생 시 id랑 exception 로깅
            log.error("error deleting entity ", entity.getId(),e);

            // 컨트롤러로 예외를 날린다. db 내부 로직을 캡슐화하기 위해 e는 리턴하지 않고 새 예외 오브젝트를 리턴
            throw new RuntimeException("error deleting entity " + entity.getId());
        }

        // 새 News 리스트를 리턴 해줌
        return retrieve(entity.getSeller().getId());
    }

    public UserEntity getUserById(String userId) {
        return userRepository.findByUserId(userId);
    }


    public StoreEntity getSellerByUser(UserEntity user) {
        return sellerRepository.findByUser(user)
            .orElseThrow(() -> new IllegalArgumentException("Seller not found"));
    }

    public StoreEntity getStoreByName(String storeName) {
        return sellerRepository.findByStoreName(storeName)
            .orElseThrow(() -> new IllegalArgumentException("Seller not found"));
    }

    public NewsEntity convertToEntity(NewsDTO dto) {
        return NewsDTO.toEntity(dto);
    }

    public ResponseDTO<NewsDTO> getResponseDTO(List<NewsDTO> dtos) {
        // 변환된 TodoDto 리스트를 이용해서 리스펀스를 초기화
        return ResponseDTO.<NewsDTO>builder()
            .data(dtos)
            .build();
    }

    public ResponseDTO<NewsDTO> getResponseListDTO(List<NewsListResponseDTO> dtos) {
        // 변환된 TodoDto 리스트를 이용해서 리스펀스를 초기화
        return ResponseDTO.<NewsDTO>builder()
            .data(dtos)
            .build();
    }


    // 주소 문자열을 파싱하여 Address 객체로 변환하는 메서드
    public String parseAddress(String fullAddress) {
        // 주소 문자열을 파싱하여 필요한 정보 추출
        // 예시로는 각 요소를 공백이나 쉼표 등으로 구분하여 추출하는 방식을 사용하였습니다.
        String[] addressParts = fullAddress.split("[,\\s]+");
        String city = addressParts[2];;
        return city;
    }

    public ResponseEntity<ResponseDTO<NewsDTO>> createErrorResponse(String errorMessage) {
        ResponseDTO<NewsDTO> response = new ResponseDTO<>();
        response.setError(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}