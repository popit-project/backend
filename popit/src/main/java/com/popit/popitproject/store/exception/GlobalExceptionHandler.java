package com.popit.popitproject.store.exception;


import com.popit.popitproject.store.exception.storeSeller.DuplicateStoreNameException;

import com.popit.popitproject.store.exception.storeSeller.ExceptionDTO;
import com.popit.popitproject.store.exception.storeSeller.StoreAlreadyRegisteredException;
import com.popit.popitproject.store.exception.storeSeller.StoreRegisteredException;
import com.popit.popitproject.store.exception.storeSeller.UserNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.support.DefaultMessageSourceResolvable;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ExceptionDTO>> methodArgumentValidException(
        MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(extractErrorMessages(e));
    }


    private List<ExceptionDTO> extractErrorMessages(MethodArgumentNotValidException e) {
        return e.getBindingResult()
            .getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .map(ExceptionDTO::new)
            .collect(Collectors.toList());
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> processValidationError(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

        StringBuilder messageTemplates = new StringBuilder();
        for (ConstraintViolation<?> violation : constraintViolations) {
            String messageTemplate = violation.getMessageTemplate();
            messageTemplates.append(messageTemplate).append("\n");
        }

        return ResponseEntity.badRequest().body(messageTemplates.toString());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("User not found: " + ex.getMessage());
    }

    @ExceptionHandler(StoreAlreadyRegisteredException.class)
    public ResponseEntity<String> handleStoreAlreadyRegisteredException(
        StoreAlreadyRegisteredException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body("Store already registered: " + ex.getMessage());
    }

    @ExceptionHandler(StoreRegisteredException.class)
    private ResponseEntity<String> prePersistDate() {
        // 적절한 응답 형식으로 에러 메시지 반환
        return ResponseEntity.status(HttpStatus.CONFLICT).body("오픈 날짜 이후의 날짜를 입력해주세요.");
    }

    @ExceptionHandler(DuplicateStoreNameException.class)
    public ResponseEntity<String> handleDuplicateStoreNameException(
        DuplicateStoreNameException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 스토어 이름입니다.");
    }


    private static class ErrorResponseBody {

        private String error;
        private List<String> messages;

        public ErrorResponseBody(String error, List<String> messages) {
            this.error = error;
            this.messages = messages;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public List<String> getMessages() {
            return messages;
        }

        public void setMessages(List<String> messages) {
            this.messages = messages;
        }
    }

}