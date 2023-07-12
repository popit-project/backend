package com.popit.popitproject.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.popit.popitproject.news.model.NewsDTO;
import com.popit.popitproject.news.model.NewsListResponseDTO;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> {
    private Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<T> datas;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String value;

    public ResponseDTO(String message) {
        this.message = message;
    }

    public static ResponseDTO<NewsDTO> getResponseDTO(List<NewsDTO> dtos) {
        // 변환된 TodoDto 리스트를 이용해서 리스펀스를 초기화
        return ResponseDTO.<NewsDTO>builder()
            .data(dtos)
            .build();
    }

    public static ResponseDTO<NewsDTO> getResponseListDTO(List<NewsListResponseDTO> dtos) {
        // 변환된 TodoDto 리스트를 이용해서 리스펀스를 초기화
        return ResponseDTO.<NewsDTO>builder()
            .data(dtos)
            .build();
    }

    public static ResponseEntity<ResponseDTO<NewsDTO>> createErrorResponse(String errorMessage) {
        ResponseDTO<NewsDTO> response = new ResponseDTO<>();
        response.setError(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}