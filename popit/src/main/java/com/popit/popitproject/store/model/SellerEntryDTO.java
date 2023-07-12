package com.popit.popitproject.store.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.popit.popitproject.user.entity.UserEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SellerEntryDTO {

    private Long id;

    private String storeName;

    private MultipartFile storeImgURL;

    private String storeType; // 사업 종류

    private String storeAddress; // 매장 주소

    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime; // 운영시간

    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate openDate; // 운영기간

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate closeDate;

    private LocalDate updateTime;

    private UserEntity user;

    @NotBlank
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{5}")
    private String businessLicenseNumber;

    private Double x;
    private Double y;


    public static SellerEntryDTO getStoreSellerDTO(MultipartFile file, String sellerDTO)
        throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        SellerEntryDTO SellerEntryDTO = objectMapper.readValue(sellerDTO, SellerEntryDTO.class);
        SellerEntryDTO.setStoreImgURL(file);

        return SellerEntryDTO;
    }
}
