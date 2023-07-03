package com.popit.popitproject.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_PASSWORD(HttpStatus.NOT_FOUND, "Password is invalid"),
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "User name is duplicated"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),

    DUPLICATED_STORE_NAME(HttpStatus.CONFLICT, "Store name is duplicated"),


    ADDRESS_NULL_ERROR(HttpStatus.BAD_REQUEST, "주소 값이 null 입니다."),
    STORE_NAME_NULL_ERROR(HttpStatus.BAD_REQUEST, "가게 이름 값이 null 입니다."),

    OPEN_TIME_NULL_ERROR(HttpStatus.BAD_REQUEST, "오픈 시간 값이 null 입니다."),
    CLOSE_TIME_NULL_ERROR(HttpStatus.BAD_REQUEST, "마감 시간 값이 null 입니다."),

    OPEN_DATE_NULL_ERROR(HttpStatus.BAD_REQUEST, "오픈 날짜 값이 null 입니다."),
    CLOSE_DATE_NULL_ERROR(HttpStatus.BAD_REQUEST, "마감 날짜 값이 null 입니다."),

    INVALID_OPEN_DATE(HttpStatus.NOT_FOUND, "오픈 날짜 값이 형식에 맞지 않습니다."),
    INVALID_CLOSE_DATE(HttpStatus.NOT_FOUND, "마감 시간 값이 형식에 맞지 않습니다."),

//    ADDRESS_NULL_ERROR(HttpStatus.BAD_REQUEST, "주소 값이 null 입니다."),
//    ADDRESS_NULL_ERROR(HttpStatus.BAD_REQUEST, "주소 값이 null 입니다."),

    CONTENT_NULL_ERROR(HttpStatus.BAD_REQUEST, " null 입니다."),


    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    ;

    private HttpStatus status;
    private String message;
}
