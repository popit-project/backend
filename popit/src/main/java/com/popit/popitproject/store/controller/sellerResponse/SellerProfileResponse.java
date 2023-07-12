package com.popit.popitproject.store.controller.sellerResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerProfileResponse {

    private String storeImage;
    private String storeName;
    private String storeType;
    private String storeAddress;

}
