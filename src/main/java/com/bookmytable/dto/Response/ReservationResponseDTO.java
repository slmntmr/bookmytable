package com.bookmytable.dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationResponseDTO {
    private Long id;
    private LocalDateTime reservationDate;
    private int numberOfGuests;
    private String status;
    private UserResponseDTO user;
    private RestaurantResponseDTO restaurant;
}
