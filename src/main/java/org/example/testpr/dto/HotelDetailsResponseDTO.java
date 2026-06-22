package org.example.testpr.dto;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDetailsResponseDTO {

    private Long id;
    private String name;
    private String description;

    private String brand;

    private AddressDTO address;
    private ContactDTO contacts;
    private ArrivalTimeDTO arrivalTime;

    private Set<String> amenities;
}
