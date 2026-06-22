package org.example.testpr.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.testpr.dto.HotelCreateRequestDTO;
import org.example.testpr.dto.HotelDetailsResponseDTO;
import org.example.testpr.dto.HotelShortResponseDTO;
import org.example.testpr.exception.HotelNotFoundException;
import org.example.testpr.mapper.HotelMapper;
import org.example.testpr.model.Amenity;
import org.example.testpr.model.Hotel;
import org.example.testpr.repository.AmenityRepository;
import org.example.testpr.repository.HotelRepository;
import org.example.testpr.service.HotelService;
import org.example.testpr.specification.HotelSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;


    @Override
    @Transactional
    public HotelShortResponseDTO createHotel(HotelCreateRequestDTO dto) {
        Hotel hotel = hotelMapper.toEntity(dto);
        Hotel saved = hotelRepository.save(hotel);
        return hotelMapper.toShortDto(saved);
    }


    @Override
    public List<HotelShortResponseDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
                .filter(h -> !h.isDeleted())
                .map(hotelMapper::toShortDto)
                .toList();
    }

    @Override
    public HotelDetailsResponseDTO getHotelDetails(Long id) {
        Hotel hotel = findActiveHotel(id);
        return hotelMapper.toDetailsDto(hotel);
    }


    @Override
    public List<HotelShortResponseDTO> searchHotels(
            String name,
            String brand,
            String city,
            String country,
            Set<String> amenities) {

        Specification<Hotel> spec = Specification
                .where(HotelSpecification.notDeleted())
                .and(HotelSpecification.hasName(name))
                .and(HotelSpecification.hasBrand(brand))
                .and(HotelSpecification.hasCity(city))
                .and(HotelSpecification.hasCountry(country))
                .and(HotelSpecification.hasAmenityNames(amenities));

        return hotelRepository.findAll(spec).stream()
                .map(hotelMapper::toShortDto)
                .toList();
    }


    @Override
    @Transactional
    public HotelDetailsResponseDTO addAmenities(Long hotelId, Set<String> amenityNames) {
        Hotel hotel = findActiveHotel(hotelId);

        Set<Amenity> amenitiesToAdd = amenityNames.stream()
                .map(name -> amenityRepository.findByName(name)
                        .orElseGet(() -> amenityRepository.save(
                                Amenity.builder().name(name).build())))
                .collect(Collectors.toSet());

        hotel.getAmenities().addAll(amenitiesToAdd);
        hotelRepository.save(hotel);

        return hotelMapper.toDetailsDto(hotel);
    }


    @Override
    public Map<String, Long> getHistogram(String param) {
        List<Hotel> hotels = hotelRepository.findAll().stream()
                .filter(h -> !h.isDeleted())
                .toList();

        return switch (param.toLowerCase()) {
            case "brand" -> hotels.stream()
                    .collect(Collectors.groupingBy(Hotel::getBrand, Collectors.counting()));
            case "city" -> hotels.stream()
                    .collect(Collectors.groupingBy(h -> h.getAddress().getCity(), Collectors.counting()));
            case "country" -> hotels.stream()
                    .collect(Collectors.groupingBy(h -> h.getAddress().getCountry(), Collectors.counting()));
            case "amenities" -> hotels.stream()
                    .flatMap(h -> h.getAmenities().stream())
                    .collect(Collectors.groupingBy(Amenity::getName, Collectors.counting()));
            default -> throw new IllegalArgumentException("Invalid histogram param: " + param);
        };
    }

    @Override
    @Transactional
    public void deleteHotel(Long id) {
        Hotel hotel = findActiveHotel(id);
        hotel.setDeleted(true);
        hotelRepository.save(hotel);
    }

    private Hotel findActiveHotel(Long id) {
        return hotelRepository.findById(id)
                .filter(h -> !h.isDeleted())
                .orElseThrow(() -> new HotelNotFoundException(id));
    }
}
