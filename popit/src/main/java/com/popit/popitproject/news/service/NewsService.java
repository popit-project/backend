package com.popit.popitproject.news.service;

import com.popit.popitproject.common.kakaoAddress.KakaoAddressParseService;
import com.popit.popitproject.common.s3.S3Service;
import com.popit.popitproject.news.entity.NewsEntity;
import com.popit.popitproject.news.model.NewsDTO;
import com.popit.popitproject.news.repository.NewsRepository;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        newsEntity.setNewsNumber(newsRepository.countNewsByStoreId(store.getId()) + 1);

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
            () -> new RuntimeException("사용자 정보 x")
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
            () -> new RuntimeException("사용자 정보 x")
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

            NewsEntity news = newsRepository.findById(entity.getId())
                .orElseThrow(RuntimeException::new);
            String newsImageUrl = news.getNewsImgURL();
            String fileName = newsImageUrl.replace(
                "https://" + s3Service.getBucketName() + ".s3." + s3Service.getRegion()
                    + ".amazonaws.com/", "");
            s3Service.deleteFile(fileName);
            // 엔티티 삭제
            newsRepository.delete(entity);
        } catch (Exception e) {
            // 예외 발생 시 id랑 exception 로깅
            log.error("error deleting entity ", entity.getId(), e);

            // 컨트롤러로 예외를 날린다. db 내부 로직을 캡슐화하기 위해 e는 리턴하지 않고 새 예외 오브젝트를 리턴
            throw new RuntimeException("error deleting entity " + entity.getId());
        }

        // 새 News 리스트를 리턴 해줌
        return retrieve(entity.getSeller().getId());
    }

    // 삭제
    public void deleteNews(StoreEntity store) {
            List<NewsEntity> newsEntities = newsRepository.findAllBySeller(store).orElseThrow();

            // 뉴스의 이미지 삭제
            for (NewsEntity news : newsEntities) {
                String newsImageUrl = news.getNewsImgURL();
                String fileName = newsImageUrl.replace(
                    "https://" + s3Service.getBucketName() + ".s3." + s3Service.getRegion()
                        + ".amazonaws.com/", "");
                s3Service.deleteFile(fileName);
            }

            newsRepository.deleteAll(newsEntities);
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


}