package com.popit.popitproject.store.exception.storeSeller;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class ExceptionDTO {
    private String errorMessage;
    public ExceptionDTO(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}