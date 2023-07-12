package com.popit.popitproject.common.exception;

import com.popit.popitproject.news.controller.NewsController;
import com.popit.popitproject.store.controller.StoreEntryController;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.support.DefaultMessageSourceResolvable;

/**
 * validate 어노테이션에 대한 예외처리
 * */

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@ControllerAdvice(assignableTypes = {NewsController.class, StoreEntryController.class} )
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentValidException(
        MethodArgumentNotValidException e) {

        ResponseDTO<Object> response =
            ResponseDTO.builder()
                .error(String.valueOf(e.getFieldError()))
                .status(e.getObjectName())
                .message(extractErrorMessages(e).toString())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }

    private List<?> extractErrorMessages(MethodArgumentNotValidException e) {
        return e.getBindingResult()
            .getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .map(ResponseDTO::new)
            .collect(Collectors.toList());
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> processValidationError(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

        List<ValidationError> validationErrorsList = new ArrayList<>();
        for (ConstraintViolation<?> violation : constraintViolations) {
            String messageTemplate = violation.getMessageTemplate();
            Path propertyPath = violation.getPropertyPath();
            String value = violation.getInvalidValue().toString();

            ValidationError validationError = ValidationError.builder()
                .propertyPaths(propertyPath.toString())
                .messageTemplates(messageTemplate)
                .value(value)
                .build();

            validationErrorsList.add(validationError);
        }

        ValidationErrorResponse response =
            ValidationErrorResponse.builder()
                .status(ErrorCode.INPUT_VALIDATION_FAILURE.getMessage())
                .validationErrorsList(validationErrorsList)
                .build();

        return ResponseEntity.status(ErrorCode.INPUT_VALIDATION_FAILURE.getStatus()).body(response);
    }

}