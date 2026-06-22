package org.example.testpr.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class HotelResponseDTO {

    private Long id;
    private String name;
    private String description;

    private AddressDTO address;
    private ContactDTO contacts;
    private ArrivalTimeDTO arrivalTime;

    private Set<AmenityDTO> amenities;
}