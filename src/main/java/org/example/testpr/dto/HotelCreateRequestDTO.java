package org.example.testpr.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class HotelCreateRequestDTO {

    @NotBlank(message = "Hotel name must not be blank")
    @Size(max = 255, message = "Hotel name must be less than 255 characters")
    private String name;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @NotBlank(message = "Brand must not be blank")
    @Size(max = 255, message = "Brand must be less than 255 characters")
    private String brand;

    @NotNull(message = "Address is required")
    @Valid
    private AddressDTO address;

    @NotNull(message = "Contact info is required")
    @Valid
    private ContactDTO contacts;

    @Valid
    private ArrivalTimeDTO arrivalTime;

    private Set<Long> amenityIds;
}
