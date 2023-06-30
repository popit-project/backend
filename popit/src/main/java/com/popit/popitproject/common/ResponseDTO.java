package com.popit.popitproject.common;

import com.popit.popitproject.news.model.NewsDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO<T> {

    private String error;
    private Object data;
//    private List<T> datas;
}