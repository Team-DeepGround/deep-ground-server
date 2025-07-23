package com.samsamhajo.deepground.address.dto;

import com.samsamhajo.deepground.address.entity.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressDto {
    private Long id;
    private String city;
    private String gu;
    private String dong;

    public static AddressDto from(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .city(address.getCity())
                .gu(address.getGu())
                .dong(address.getDong())
                .build();
    }
}
