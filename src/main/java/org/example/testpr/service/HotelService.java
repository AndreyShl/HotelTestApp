package org.example.testpr.service;

import org.example.testpr.dto.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface HotelService {


    HotelShortResponseDTO createHotel(HotelCreateRequestDTO dto);


    List<HotelShortResponseDTO> getAllHotels();


    HotelDetailsResponseDTO getHotelDetails(Long id);

    List<HotelShortResponseDTO> searchHotels(
            String name,
            String brand,
            String city,
            String country,
            Set<String> amenities
    );


    void deleteHotel(Long id);

    HotelDetailsResponseDTO addAmenities(Long hotelId, Set<String> amenities);

    Map<String, Long> getHistogram(String param);
}
