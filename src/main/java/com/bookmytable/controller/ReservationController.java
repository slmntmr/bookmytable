package com.bookmytable.controller;

import com.bookmytable.dto.Response.ReservationResponseDTO;
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
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/all")  // GET (http://localhost:8080/api/reservations/all)
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    // USER ve ADMIN rollerinin kendi rezervasyonlarını görmesini sağlayan metot
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/my-reservations")  // GET (http://localhost:8080/api/reservations/my-reservations)
    public ResponseEntity<List<Reservation>> getReservationsForCurrentUser() {
        List<Reservation> reservations = reservationService.getReservationsForCurrentUser();
        return ResponseEntity.ok(reservations);
    }

    // USER ve ADMIN rollerinin kendi rezervasyon detayı alma işlemi
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/view/{id}")  // GET (http://localhost:8080/api/reservations/view/{id})
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.getReservationById(id);
        return reservation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // USER ve ADMIN rollerinin yeni rezervasyon ekleme işlemi
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/create")  // POST (http://localhost:8080/api/reservations/create)
    public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody Reservation reservation) {
        Reservation createdReservation = reservationService.createReservation(reservation);
        ReservationResponseDTO responseDto = reservationService.convertToDto(createdReservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    // Sadece ADMIN rolüne sahip kullanıcıların rezervasyon onaylama işlemi
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/approve/{id}")  // PUT (http://localhost:8080/api/reservations/approve/{id})
    public ResponseEntity<Reservation> approveReservation(@PathVariable Long id) {
        Reservation approvedReservation = reservationService.updateReservationStatus(id, "APPROVED");
        return ResponseEntity.ok(approvedReservation);
    }

    // Sadece ADMIN rolüne sahip kullanıcıların rezervasyon reddetme işlemi
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/reject/{id}")  // PUT (http://localhost:8080/api/reservations/reject/{id})
    public ResponseEntity<Reservation> rejectReservation(@PathVariable Long id) {
        Reservation rejectedReservation = reservationService.updateReservationStatus(id, "REJECTED");
        return ResponseEntity.ok(rejectedReservation);
    }

    // Sadece ADMIN rolüne sahip kullanıcıların rezervasyon askıya alma işlemi
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/pending/{id}")  // PUT (http://localhost:8080/api/reservations/pending/{id})
    public ResponseEntity<Reservation> pendingReservation(@PathVariable Long id) {
        Reservation pendingReservation = reservationService.updateReservationStatus(id, "PENDING");
        return ResponseEntity.ok(pendingReservation);
    }

    // Sadece ADMIN rolüne sahip kullanıcıların rezervasyon silme işlemi
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")  // DELETE (http://localhost:8080/api/reservations/delete/{id})
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
