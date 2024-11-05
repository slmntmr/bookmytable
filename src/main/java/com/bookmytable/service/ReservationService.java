package com.bookmytable.service;

import com.bookmytable.entity.business.Reservation;
import com.bookmytable.entity.business.User;
import com.bookmytable.entity.enums.ReservationStatus;
import com.bookmytable.repository.ReservationRepository;
import com.bookmytable.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    // Sadece ADMIN rolü için tüm rezervasyonları döndürür
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Mevcut kullanıcıya ait tüm rezervasyonları döndürür
    public List<Reservation> getReservationsForCurrentUser() {
        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return reservationRepository.findByUser(user);
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation createReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatus.PENDING); // Varsayılan olarak askıda başlatıyoruz
        return reservationRepository.save(reservation);
    }

    // Rezervasyon durumunu güncelleyen metot
    public Reservation updateReservationStatus(Long id, String status) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    ReservationStatus reservationStatus = ReservationStatus.valueOf(status);
                    reservation.setStatus(reservationStatus);
                    return reservationRepository.save(reservation);
                })
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    // Oturum açmış kullanıcının adını almak için yardımcı metot
    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
