package com.popit.popitproject.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ValidationError {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String propertyPaths;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String messageTemplates;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String value;



}
