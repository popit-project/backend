package com.popit.popitproject.store.exception.storeSeller;

import com.popit.popitproject.store.entity.StoreEntity;
import com.popit.popitproject.store.model.SellerEntryDTO;
import com.popit.popitproject.store.model.StoreType;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreSellerValidate {

    private final StoreSellerRepository sellerRepository;
    private final UserRepository userRepository;

    // 스토어 등록 유효성 검사
    public UserEntity validateSellerRegistration(String userId, SellerEntryDTO sellerDTO) {

        // 사용자를 찾을 수 없음
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }

        // 스토어가 이미 등록된 경우
        if (user.getStore() != null) {
            StoreEntity store = StoreEntity.builder()
                .storeName(user.getStore().getStoreName())
                .id(user.getSellerId())
                .build();

            throw new StoreAlreadyRegisteredException("이미 판매자로 등록된 상태입니다.");
        }

        // 중복된 스토어가 있을 경우
        if (sellerRepository.existsByStoreName(sellerDTO.getStoreName())) {
            throw new DuplicateStoreNameException("중복된 스토어 이름입니다.");
        }

        if (sellerDTO.getStoreType().equals(StoreType.CHOOSE_TYPE.toString())) {
            throw new StoreTypeEmptyException("가게 유형이 선택되지 않았습니다.");
        }
        return user;
    }

}
