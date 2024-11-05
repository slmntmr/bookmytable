package com.bookmytable.controller;

import com.bookmytable.entity.business.Reservation;
import com.bookmytable.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    // Sadece ADMIN rolüne sahip kullanıcıların tüm rezervasyonları görmesini sağlayan metot
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    // USER ve ADMIN rollerinin kendi rezervasyonlarını görmesini sağlayan metot
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<Reservation>> getReservationsForCurrentUser() {
        List<Reservation> reservations = reservationService.getReservationsForCurrentUser();
        return ResponseEntity.ok(reservations);
    }

    // USER ve ADMIN rollerinin kendi rezervasyon detayı alma işlemi
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.getReservationById(id);
        return reservation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // USER ve ADMIN rollerinin yeni rezervasyon ekleme işlemi
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation createdReservation = reservationService.createReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    // Sadece ADMIN rolüne sahip kullanıcıların rezervasyon onaylama işlemi
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/approve/{id}")
    public ResponseEntity<Reservation> approveReservation(@PathVariable Long id) {
        Reservation approvedReservation = reservationService.updateReservationStatus(id, "APPROVED");
        return ResponseEntity.ok(approvedReservation);
    }

    // Sadece ADMIN rolüne sahip kullanıcıların rezervasyon reddetme işlemi
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reject/{id}")
    public ResponseEntity<Reservation> rejectReservation(@PathVariable Long id) {
        Reservation rejectedReservation = reservationService.updateReservationStatus(id, "REJECTED");
        return ResponseEntity.ok(rejectedReservation);
    }

    // Sadece ADMIN rolüne sahip kullanıcıların rezervasyon askıya alma işlemi
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/pending/{id}")
    public ResponseEntity<Reservation> pendingReservation(@PathVariable Long id) {
        Reservation pendingReservation = reservationService.updateReservationStatus(id, "PENDING");
        return ResponseEntity.ok(pendingReservation);
    }

    // Sadece ADMIN rolüne sahip kullanıcıların rezervasyon silme işlemi
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
