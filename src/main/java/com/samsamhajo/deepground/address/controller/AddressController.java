package com.samsamhajo.deepground.address.controller;

import com.samsamhajo.deepground.address.dto.AddressDto;
import com.samsamhajo.deepground.address.service.AddressService;
import com.samsamhajo.deepground.address.success.AddressSuccessCode;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/cities")
    public ResponseEntity<SuccessResponse<List<AddressDto>>> getAllCities() {
        List<AddressDto> cities = addressService.getAllCities();
        return ResponseEntity
                .status(AddressSuccessCode.GET_CITIES_SUCCESS.getStatus())
                .body(SuccessResponse.of(AddressSuccessCode.GET_CITIES_SUCCESS, cities));
    }

    @GetMapping("/gus")
    public ResponseEntity<SuccessResponse<List<AddressDto>>> getGusByCity(
            @RequestParam String city)
    {
        List<AddressDto> gus = addressService.getGusByCity(city);
        return ResponseEntity
                .status(AddressSuccessCode.GET_GUS_SUCCESS.getStatus())
                .body(SuccessResponse.of(AddressSuccessCode.GET_GUS_SUCCESS, gus));
    }

    @GetMapping("/dongs")
    public ResponseEntity<SuccessResponse<List<AddressDto>>> getDongsByCityAndGu(
            @RequestParam String city,
            @RequestParam String gu
    ) {
        List<AddressDto> dongs = addressService.getDongsByCityAndGu(city, gu);
        return ResponseEntity
                .status(AddressSuccessCode.GET_DONGS_SUCCESS.getStatus())
                .body(SuccessResponse.of(AddressSuccessCode.GET_DONGS_SUCCESS, dongs));
    }
}
