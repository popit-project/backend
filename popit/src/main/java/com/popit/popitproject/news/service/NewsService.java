package com.popit.popitproject.news.service;

import com.popit.popitproject.common.ResponseDTO;
import com.popit.popitproject.news.entity.NewsEntity;
import com.popit.popitproject.news.model.NewsDTO;
import com.popit.popitproject.news.repository.NewsRepository;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.repository.StoreRepository;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 서비스 레이어는 컨트롤러와 퍼시스턴스 사이에서 비즈니스 로직을 수행하는 역할을 한다. HTTP와 긴밀히 연관된 컨트롤러에서 분리돼 있고, 또 데이터 베이스와 긴밀히 연관된
 * 퍼시스턴스와도 분리돼 있다.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final StoreSellerRepository sellerRepository;
    private final UserRepository userRepository;

    // 생성 create
    public List<NewsEntity> create(final NewsEntity entity) {
        // Validations
        validate(entity);

        newsRepository.save(entity);

        log.info("Entity Id : {} is saved", entity.getId());

        return newsRepository.findBySeller(entity.getSeller()).orElseThrow(
            ()-> new RuntimeException("사용자 정보 x")
        );
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
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
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