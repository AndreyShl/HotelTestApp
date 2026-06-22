package org.example.testpr.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.testpr.dto.*;
import org.example.testpr.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;


    @GetMapping("/hotels")
    public List<HotelShortResponseDTO> getAllHotels() {
        return hotelService.getAllHotels();
    }


    @GetMapping("/hotels/{id}")
    public HotelDetailsResponseDTO getHotelById(@PathVariable Long id) {
        return hotelService.getHotelDetails(id);
    }


    @PostMapping("/hotels")
    @ResponseStatus(HttpStatus.CREATED)
    public HotelShortResponseDTO createHotel(
            @RequestBody @Valid HotelCreateRequestDTO dto) {

        return hotelService.createHotel(dto);
    }


    @PostMapping("/hotels/{id}/amenities")
    public HotelDetailsResponseDTO addAmenities(
            @PathVariable Long id,
            @RequestBody Set<String> amenities) {

        return hotelService.addAmenities(id, amenities);
    }


    @GetMapping("/search")
    public List<HotelShortResponseDTO> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,

            HashMap @RequestParam(required = false) Set<String> amenities) {

        return hotelService.searchHotels(name, brand, city, country, amenities);
    }


    @GetMapping("/histogram/{param}")
    public Map<String, Long> getHistogram(@PathVariable String param) {
        return hotelService.getHistogram(param);
    }
}
