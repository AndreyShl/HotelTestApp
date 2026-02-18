package org.example.testpr.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactDTO {

    @NotBlank(message = "Phone must not be blank")
    private String phone;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email must not be blank")
    private String email;
}
