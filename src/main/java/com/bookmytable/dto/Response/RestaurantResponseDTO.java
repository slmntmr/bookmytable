package com.bookmytable.dto.Response;

import lombok.Data;

@Data
public class RestaurantResponseDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String description;
}
