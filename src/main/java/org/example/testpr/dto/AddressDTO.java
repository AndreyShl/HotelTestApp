package org.example.testpr.dto;



import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddressDTO {

    @NotNull(message = "House number is required")
    @Positive(message = "House number must be positive")
    private Integer houseNumber;

    @NotBlank(message = "Street must not be blank")
    private String street;

    @NotBlank(message = "City must not be blank")
    private String city;

    @NotBlank(message = "Country must not be blank")
    private String country;

    @NotBlank(message = "Post code must not be blank")
    private String postCode;
}
