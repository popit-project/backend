package com.popit.popitproject.store.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popit.popitproject.Item.service.S3Service;
import com.popit.popitproject.news.entity.NewsEntity;
import com.popit.popitproject.news.model.NewsDTO;
import com.popit.popitproject.news.repository.NewsRepository;
import com.popit.popitproject.review.repository.ReviewRepository;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.exception.KakaoAddressChange;
import com.popit.popitproject.store.model.SellerModeButton;
import com.popit.popitproject.store.model.UpdateStoreSellerDTO;
import com.popit.popitproject.store.repository.StoreRepository;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.entity.UserEntity.Role;
import com.popit.popitproject.user.repository.UserRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 서비스 레이어는 컨트롤러와 퍼시스턴스 사이에서 비즈니스 로직을 수행하는 역할을 한다. HTTP와 긴밀히 연관된 컨트롤러에서 분리돼 있고, 또 데이터 베이스와 긴밀히 연관된
 * 퍼시스턴스와도 분리돼 있다.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreSellerService {

    private final StoreSellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final NewsRepository newsRepository;
    private final ReviewRepository reviewRepository;

    public StoreEntity saveSellerInfo(MultipartFile file, UserEntity user, StoreEntity store)
        throws IOException {

        String imageUrl = s3Service.uploadFile(file);
        store.setStoreImage(imageUrl);

        return sellerRepository.save(StoreEntity.builder()
            .id(store.getId())
            .storeName(store.getStoreName())
            .storeImage(store.getStoreImage())
            .storeType(store.getStoreType())
            .storeAddress(store.getStoreAddress())
            .openTime(store.getOpenTime())
            .closeTime(store.getCloseTime())
            .openDate(store.getOpenDate())
            .closeDate(store.getCloseDate())
            .businessLicenseNumber(store.getBusinessLicenseNumber())
            .user(user)
            .x(store.getX())
            .y(store.getY())
            .build()
        );
    }

    public void generateSellerRole(UserEntity user, StoreEntity store) {
        user.getRoles().add(Role.ROLE_SELLER);
        user.setSellerModeButton(SellerModeButton.BUTTON_DISPLAY_ON);
        user.setStore(store);
        userRepository.save(user);
    }

    @Transactional
    public void deleteStore(Long storeId) throws NotFoundException {
        StoreEntity store = sellerRepository.findById(storeId)
            .orElseThrow(NotFoundException::new);

        // 스토어에 연결된 소식 삭제
        newsRepository.deleteBySeller(store);

        // 스토어에 연결된 리뷰 삭제
        reviewRepository.deleteByStore(store);

        // 스토어와 연결된 유저 정보 삭제
        UserEntity user = store.getUser();
        store.setUser(null); // 스토어에서 유저 연결 해제

        // 스토어와 연결된 유저 정보에서 sellerId 필드 제거
        user.setStore(null); // sellerId 필드 제거
        userRepository.save(user); // 유저 정보 업데이트

        // 스토어 삭제
        sellerRepository.delete(store);
    }

    @Transactional
    public StoreEntity updateStore(UpdateStoreSellerDTO updateStoreSellerDTO, StoreEntity storeInfo) throws IOException {

        String newAddress = updateStoreSellerDTO.getStoreAddress();
        StoreEntity change = KakaoAddressChange.addressChange(newAddress);


        // 수정된 정보로 업데이트
        storeInfo.setStoreAddress(updateStoreSellerDTO.getStoreAddress());
        storeInfo.setOpenTime(updateStoreSellerDTO.getOpenTime());
        storeInfo.setCloseTime(updateStoreSellerDTO.getCloseTime());
        storeInfo.setOpenDate(updateStoreSellerDTO.getOpenDate());
        storeInfo.setCloseDate(updateStoreSellerDTO.getCloseDate());

        storeInfo.setX(change.getX());
        storeInfo.setY(change.getY());

        return sellerRepository.save(storeInfo);
    }

}