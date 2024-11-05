package com.bookmytable.entity.business;

import com.bookmytable.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Table(name = "t_user")
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Reservation> reservations;
}
