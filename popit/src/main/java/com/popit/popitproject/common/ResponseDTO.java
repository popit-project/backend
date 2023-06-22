package com.popit.popitproject.common;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO<T> {

    private String error;
    private Object data;
    private List<T> datas;

}
