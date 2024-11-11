package com.bookmytable.entity.business;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Table(name = "t_user")
@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"reservations"}) // Döngüyü engellemek için kullanılır
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    // Kullanıcının rollerini tutan alan
    @ManyToMany(fetch = FetchType.EAGER) // Döngü hatasını engellemek için EAGER kullanımı
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("user") // Döngüyü engellemek için eklenir
    private Set<Reservation> reservations;
}
