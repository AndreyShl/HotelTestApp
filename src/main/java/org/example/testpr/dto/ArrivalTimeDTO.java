package org.example.testpr.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ArrivalTimeDTO {

    @Pattern(
            regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",
            message = "Check-in time must be in HH:mm format"
    )
    private String checkIn;

    @Pattern(
            regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",
            message = "Check-out time must be in HH:mm format"
    )
    private String checkOut;
}
