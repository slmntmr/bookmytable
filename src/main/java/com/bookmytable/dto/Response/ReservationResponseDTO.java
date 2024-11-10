package com.bookmytable.dto.Response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationResponseDTO {
    private Long id;
    private LocalDateTime reservationDate;
    private int numberOfGuests;
    private String status;
    private UserResponseDTO user;
    private RestaurantResponseDTO restaurant;
}
