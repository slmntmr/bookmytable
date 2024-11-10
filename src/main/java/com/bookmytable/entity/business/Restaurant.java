package com.bookmytable.entity.business;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@JsonIgnoreProperties({"reservations"}) // Döngüyü engellemek için kullanılır
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;
    private String description;

    // Restoranın rezervasyonları
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("restaurant") // Döngüyü engellemek için eklenir
    private Set<Reservation> reservations = new HashSet<>();
}
