package com.bookmytable.controller;

import com.bookmytable.dto.Request.ReservationRequestDTO;
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

    // ADMIN rolüne sahip kullanıcıların tüm rezervasyonları görmesi için
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all") //http://localhost:8080/api/reservations/all
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    //*****************************************************************************************************************

    // USER ve ADMIN rollerinin kendi rezervasyonlarını görmesi için
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/my-reservations") //http://localhost:8080/api/reservations/my-reservations
    public ResponseEntity<List<Reservation>> getReservationsForCurrentUser() {
        List<Reservation> reservations = reservationService.getReservationsForCurrentUser();
        return ResponseEntity.ok(reservations);
    }

    //*****************************************************************************************************************

    // USER ve ADMIN rollerinin kendi rezervasyon detayını görmesi için
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/view/{id}") //http://localhost:8080/api/reservations/view/8
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.getReservationById(id);
        return reservation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
//*****************************************************************************************************************
    // USER ve ADMIN rollerinin yeni rezervasyon oluşturması için
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/create")  //http://localhost:8080/api/reservations/create POST + JSON
    public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody ReservationRequestDTO reservationRequestDTO) {
        Reservation createdReservation = reservationService.createReservation(reservationRequestDTO);
        ReservationResponseDTO responseDto = reservationService.convertToDto(createdReservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    //*****************************************************************************************************************


    // ADMIN rolüne sahip kullanıcıların rezervasyon onaylama işlemi için
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/approve/{id}") //http://localhost:8080/api/reservations/approve/8
    public ResponseEntity<Reservation> approveReservation(@PathVariable Long id) {
        Reservation approvedReservation = reservationService.updateReservationStatus(id, "APPROVED");
        return ResponseEntity.ok(approvedReservation);
    }


    //*****************************************************************************************************************


    // ADMIN rolüne sahip kullanıcıların rezervasyon reddetme işlemi için
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/reject/{id}") //http://localhost:8080/api/reservations/reject/8
    public ResponseEntity<Reservation> rejectReservation(@PathVariable Long id) {
        Reservation rejectedReservation = reservationService.updateReservationStatus(id, "REJECTED");
        return ResponseEntity.ok(rejectedReservation);
    }


    //*****************************************************************************************************************


    // ADMIN rolüne sahip kullanıcıların rezervasyon askıya alma işlemi için
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/pending/{id}")
    public ResponseEntity<Reservation> pendingReservation(@PathVariable Long id) {
        Reservation pendingReservation = reservationService.updateReservationStatus(id, "PENDING");
        return ResponseEntity.ok(pendingReservation);
    }


    //*****************************************************************************************************************


    // ADMIN rolüne sahip kullanıcıların rezervasyon silme işlemi için
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}") // http://localhost:8080/api/reservations/delete/7
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.status(HttpStatus.OK).body("Reservation has been successfully deleted");
    }



}
