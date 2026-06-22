package org.example.testpr.mapper;

import org.example.testpr.dto.HotelCreateRequestDTO;
import org.example.testpr.dto.HotelDetailsResponseDTO;
import org.example.testpr.dto.HotelShortResponseDTO;
import org.example.testpr.model.Address;
import org.example.testpr.model.Amenity;
import org.example.testpr.model.Hotel;
import org.mapstruct.Builder;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true))
public interface HotelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    Hotel toEntity(HotelCreateRequestDTO dto);

    @Mapping(target = "address", expression = "java(formatAddress(hotel.getAddress()))")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelShortResponseDTO toShortDto(Hotel hotel);

    @Mapping(target = "amenities", expression = "java(mapAmenitiesToNames(hotel.getAmenities()))")
    HotelDetailsResponseDTO toDetailsDto(Hotel hotel);



    default String formatAddress(Address address) {
        if (address == null) return null;

        return String.format("%s %s, %s, %s",
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getCountry()
        );
    }

    default Set<String> mapAmenitiesToNames(Set<Amenity> amenities) {
        if (amenities == null) return null;

        return amenities.stream()
                .map(Amenity::getName)
                .collect(Collectors.toSet());
    }
}
