package org.example.testpr.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArrivalTime {

    private LocalTime checkIn;
    private LocalTime checkOut;
}