package com.bookmytable.repository;

import com.bookmytable.entity.business.Reservation;
import com.bookmytable.entity.business.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Bu sınıfın bir repository olduğunu belirtir
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // JpaRepository arayüzü, ID tipi Long olan Reservation entity'si için CRUD işlemlerini sağlar

    // Kullanıcının tüm rezervasyonlarını bulmak için gerekli yöntem
    List<Reservation> findByUser(User user);
}
