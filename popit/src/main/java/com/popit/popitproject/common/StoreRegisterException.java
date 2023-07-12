package com.popit.popitproject.common;

import com.popit.popitproject.common.exception.ErrorCode;
import com.popit.popitproject.common.exception.ResponseDTO;
import com.popit.popitproject.store.exception.storeSeller.DuplicateStoreNameException;
import com.popit.popitproject.store.exception.storeSeller.StoreAddressException;
import com.popit.popitproject.store.exception.storeSeller.StoreAlreadyRegisteredException;
import com.popit.popitproject.store.exception.storeSeller.StoreRegisteredException;
import com.popit.popitproject.store.repository.StoreSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RequiredArgsConstructor
public class StoreRegisterException {

    private final StoreSellerRepository sellerRepository;

    @ExceptionHandler(StoreAlreadyRegisteredException.class)
    public ResponseEntity<?> handleStoreAlreadyRegisteredException() {

        ResponseDTO<Object> response =
            ResponseDTO.builder()
                .status(String.valueOf(ErrorCode.ALREADY_STORE_SELLER.getStatus()))
                .message(ErrorCode.ALREADY_STORE_SELLER.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(StoreRegisteredException.class)
    private ResponseEntity<String> prePersistDate() {
        // 적절한 응답 형식으로 에러 메시지 반환
        return ResponseEntity.status(HttpStatus.CONFLICT).body("오픈 날짜 이후의 날짜를 입력해주세요.");
    }

    @ExceptionHandler(DuplicateStoreNameException.class)
    public ResponseEntity<String> handleDuplicateStoreNameException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 스토어 이름입니다.");
    }

    @ExceptionHandler(StoreAddressException.class)
    public ResponseEntity<?> storeAddressException() {

        ResponseDTO<Object> response =
            ResponseDTO.builder()
                .status(String.valueOf(ErrorCode.INPUT_ADDRESS_VALIDATION_FAILURE.getStatus()))
                .message(ErrorCode.INPUT_ADDRESS_VALIDATION_FAILURE.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
