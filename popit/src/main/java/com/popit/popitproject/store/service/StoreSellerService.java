package com.popit.popitproject.store.service;


import com.popit.popitproject.Item.service.ItemService;
import com.popit.popitproject.common.s3.S3Service;
import com.popit.popitproject.news.service.NewsService;
import com.popit.popitproject.review.repository.ReviewRepository;
import com.popit.popitproject.store.entity.LikeEntity;
import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.common.kakaoAddress.KakaoAddressChange;
import com.popit.popitproject.store.exception.storeSeller.StoreAddressException;
import com.popit.popitproject.store.model.SellerModeButton;
import com.popit.popitproject.store.model.SellerEntryDTO;
import com.popit.popitproject.store.model.StoreType;
import com.popit.popitproject.store.model.UpdateStoreInfoDTO;
import com.popit.popitproject.store.repository.LikeRepository;
import com.popit.popitproject.store.repository.StoreRepository;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.entity.UserEntity.Role;
import com.popit.popitproject.user.repository.UserRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreSellerService {

    private final StoreSellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final NewsService newsService;
    private final ItemService itemService;
    private final LikeRepository likeRepository;

    public StoreEntity saveStoreInfo(UserEntity user, SellerEntryDTO StoreSellerDTO)
        throws IOException {

        MultipartFile file = StoreSellerDTO.getStoreImgURL();
        String imageUrl = s3Service.uploadFile(file);

        StoreEntity storeEntity = StoreEntity.builder()
            .storeName(StoreSellerDTO.getStoreName())
            .storeImage(imageUrl)
            .storeType(StoreType.valueOf(StoreSellerDTO.getStoreType()))
            .storeAddress(StoreSellerDTO.getStoreAddress())
            .openTime(StoreSellerDTO.getOpenTime())
            .closeTime(StoreSellerDTO.getCloseTime())
            .openDate(StoreSellerDTO.getOpenDate())
            .closeDate(StoreSellerDTO.getCloseDate())
            .businessLicenseNumber(StoreSellerDTO.getBusinessLicenseNumber())
            .user(user)
            .build();

        addressChange(storeEntity);

        return storeRepository.save(storeEntity);
    }

    public void addressChange(StoreEntity storeEntity) throws IOException {
        StoreEntity change = KakaoAddressChange.addressChange(storeEntity.getStoreAddress());

        if (change != null) {
            storeEntity.setX(change.getX());
            storeEntity.setY(change.getY());
        } else {
            throw new StoreAddressException("주소 변환 실패 : " + storeEntity.getStoreAddress());
        }

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
        log.info("service-delete start");

        itemService.deleteItems(store.getId());
        newsService.deleteNews(store);
        reviewRepository.deleteByStore(store);

        List<LikeEntity> like = store.getLikes();
        likeRepository.deleteAll(like);

        UserEntity user = store.getUser();
        store.setUser(null);
        user.setStore(null);
        user.setSellerModeButton(SellerModeButton.BUTTON_DISPLAY_OFF);
        user.getRoles().remove(Role.ROLE_SELLER);

        userRepository.save(user);
        sellerRepository.delete(store);
    }

    @Transactional
    public StoreEntity updateStore(UpdateStoreInfoDTO updateStoreInfoDTO, StoreEntity storeInfo)
        throws IOException {

        String newAddress = updateStoreInfoDTO.getStoreAddress();
        StoreEntity change = KakaoAddressChange.addressChange(newAddress);
        if (change == null) {
            throw new RuntimeException("주소가 잘못 입력되었습니다.");
        }

        // 수정된 정보로 업데이트
        storeInfo.setStoreAddress(updateStoreInfoDTO.getStoreAddress());
        storeInfo.setOpenTime(updateStoreInfoDTO.getOpenTime());
        storeInfo.setCloseTime(updateStoreInfoDTO.getCloseTime());
        storeInfo.setOpenDate(updateStoreInfoDTO.getOpenDate());
        storeInfo.setCloseDate(updateStoreInfoDTO.getCloseDate());

        storeInfo.setX(change.getX());
        storeInfo.setY(change.getY());

        return sellerRepository.save(storeInfo);
    }

}