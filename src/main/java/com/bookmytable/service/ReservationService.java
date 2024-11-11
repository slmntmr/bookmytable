package com.bookmytable.service;

import com.bookmytable.dto.Request.ReservationRequestDTO;
import com.bookmytable.dto.Response.ReservationResponseDTO;
import com.bookmytable.dto.Response.RestaurantResponseDTO;
import com.bookmytable.dto.Response.UserResponseDTO;
import com.bookmytable.entity.business.Reservation;
import com.bookmytable.entity.business.User;
import com.bookmytable.entity.business.Restaurant;
import com.bookmytable.entity.enums.ReservationStatus;
import com.bookmytable.repository.ReservationRepository;
import com.bookmytable.repository.UserRepository;
import com.bookmytable.repository.RestaurantRepository;
import org.springframework.transaction.annotation.Transactional;
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
    private final RestaurantRepository restaurantRepository;

    // ADMIN rolündeki kullanıcılar için tüm rezervasyonları döndürür
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // Oturum açmış kullanıcının kendi rezervasyonlarını döndürür
    public List<Reservation> getReservationsForCurrentUser() {
        String email = getCurrentUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return reservationRepository.findByUser(user);
    }

    // Belirli bir rezervasyonu ID ile detaylı olarak döndürür
    @Transactional(readOnly = true)
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findByIdWithDetails(id);
    }

    // Yeni bir rezervasyon oluşturur ve veri tabanına kaydeder
    @Transactional
    public Reservation createReservation(ReservationRequestDTO reservationRequestDTO) {
        if (reservationRequestDTO.getUserId() == null || reservationRequestDTO.getRestaurantId() == null) {
            throw new IllegalArgumentException("Kullanıcı ID'si ve Restoran ID'si boş bırakılamaz");
        }

        User user = userRepository.findById(reservationRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        Restaurant restaurant = restaurantRepository.findById(reservationRequestDTO.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restoran bulunamadı"));

        Reservation reservation = new Reservation();
        reservation.setReservationDate(reservationRequestDTO.getReservationDate());
        reservation.setNumberOfGuests(reservationRequestDTO.getNumberOfGuests());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setUser(user);
        reservation.setRestaurant(restaurant);

        return reservationRepository.save(reservation);
    }

    // Rezervasyon durumunu günceller (APPROVED, REJECTED veya PENDING)
    public Reservation updateReservationStatus(Long id, String status) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    ReservationStatus reservationStatus = ReservationStatus.valueOf(status);
                    reservation.setStatus(reservationStatus);
                    return reservationRepository.save(reservation);
                })
                .orElseThrow(() -> new RuntimeException("Rezervasyon bulunamadı"));
    }

    // Rezervasyonu ID ile siler
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    // Oturum açmış kullanıcının e-posta adresini alır
    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    // Reservation nesnesini ReservationResponseDTO'ya dönüştürür
    public ReservationResponseDTO convertToDto(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setReservationDate(reservation.getReservationDate());
        dto.setNumberOfGuests(reservation.getNumberOfGuests());
        dto.setStatus(reservation.getStatus().name());

        // Kullanıcı bilgisi ekleme
        UserResponseDTO userDto = new UserResponseDTO();
        userDto.setId(reservation.getUser().getId());
        userDto.setEmail(reservation.getUser().getEmail());
        dto.setUser(userDto);

        // Restoran bilgisi ekleme
        RestaurantResponseDTO restaurantDto = new RestaurantResponseDTO();
        restaurantDto.setId(reservation.getRestaurant().getId());
        restaurantDto.setName(reservation.getRestaurant().getName());
        restaurantDto.setAddress(reservation.getRestaurant().getAddress());
        restaurantDto.setPhone(reservation.getRestaurant().getPhone());
        restaurantDto.setDescription(reservation.getRestaurant().getDescription());
        dto.setRestaurant(restaurantDto);

        return dto;
    }
}
