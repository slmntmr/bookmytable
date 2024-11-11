package com.bookmytable.dto.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ReservationRequestDTO {
    private LocalDateTime reservationDate;
    private int numberOfGuests;
    private Long userId;
    private Long restaurantId;

    // Getters and Setters
}
