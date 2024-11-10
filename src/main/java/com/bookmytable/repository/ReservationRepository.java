package com.bookmytable.repository;

import com.bookmytable.entity.business.Reservation;
import com.bookmytable.entity.business.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Bu sınıfın bir repository olduğunu belirtir
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Kullanıcının tüm rezervasyonlarını bulmak için gerekli yöntem
    List<Reservation> findByUser(User user);

    // Reservation nesnesini user ve restaurant bilgileriyle birlikte getirir
    @Query("SELECT r FROM Reservation r JOIN FETCH r.user JOIN FETCH r.restaurant WHERE r.id = :id")
    Optional<Reservation> findByIdWithDetails(@Param("id") Long id);
}
