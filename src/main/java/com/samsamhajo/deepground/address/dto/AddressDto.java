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

    public static AddressDto fromCity(String city) {
        return AddressDto.builder()
                .city(city)
                .build();
    }

    public static AddressDto fromGu(String city, String gu) {
        return AddressDto.builder()
                .city(city)
                .gu(gu)
                .build();
    }

    public static AddressDto from(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .city(address.getCity())
                .gu(address.getGu())
                .dong(address.getDong())
                .build();
    }
}
