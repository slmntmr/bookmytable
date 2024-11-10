package com.bookmytable.service;


import com.bookmytable.dto.Response.ReservationResponseDTO;
import com.bookmytable.dto.Response.RestaurantResponseDTO;
import com.bookmytable.dto.Response.UserResponseDTO;
import com.bookmytable.entity.business.Reservation;
import com.bookmytable.entity.business.User;
import com.bookmytable.entity.enums.ReservationStatus;
import com.bookmytable.repository.ReservationRepository;
import com.bookmytable.repository.UserRepository;
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

    /**
     * Sadece ADMIN rolüne sahip kullanıcılar için tüm rezervasyonları döndürür.
     * @return List<Reservation> - Tüm rezervasyonların listesi
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    /**
     * Oturum açmış kullanıcıya ait tüm rezervasyonları döndürür.
     * @return List<Reservation> - Mevcut kullanıcının rezervasyon listesi
     */
    public List<Reservation> getReservationsForCurrentUser() {
        String email = getCurrentUsername();  // Oturum açmış kullanıcının e-posta adresini alır
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));  // Kullanıcı bulunamazsa hata fırlatır
        return reservationRepository.findByUser(user);  // Kullanıcının rezervasyonlarını döndürür
    }

    /**
     * Belirli bir ID'ye sahip rezervasyonu tüm detaylarıyla birlikte döndürür.
     * @param id - Rezervasyonun ID değeri
     * @return Optional<Reservation> - Belirtilen ID'ye sahip rezervasyon
     */
    @Transactional(readOnly = true)
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findByIdWithDetails(id);  // İlişkili tüm detaylarıyla birlikte getirir
    }

    /**
     * Yeni bir rezervasyon oluşturur, varsayılan olarak durumu PENDING olarak ayarlanır.
     * @param reservation - Oluşturulacak rezervasyon bilgisi
     * @return Reservation - Oluşturulan rezervasyon nesnesi
     */
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatus.PENDING);  // Varsayılan durum PENDING olarak atanır
        return reservationRepository.save(reservation);  // Rezervasyonu kaydeder ve geri döndürür
    }

    /**
     * Rezervasyonun durumunu günceller (APPROVED, REJECTED, veya PENDING).
     * @param id - Güncellenecek rezervasyonun ID'si
     * @param status - Yeni durum değeri
     * @return Reservation - Güncellenmiş rezervasyon nesnesi
     */
    public Reservation updateReservationStatus(Long id, String status) {
        return reservationRepository.findById(id)  // Rezervasyonu ID ile bulur
                .map(reservation -> {
                    ReservationStatus reservationStatus = ReservationStatus.valueOf(status);  // Yeni durum değeri enum olarak alınır
                    reservation.setStatus(reservationStatus);  // Durum güncellenir
                    return reservationRepository.save(reservation);  // Güncellenmiş rezervasyon kaydedilir
                })
                .orElseThrow(() -> new RuntimeException("Reservation not found"));  // Rezervasyon yoksa hata fırlatır
    }

    /**
     * Rezervasyonu ID ile siler.
     * @param id - Silinecek rezervasyonun ID'si
     */
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    /**
     * Oturum açmış kullanıcının e-posta adresini almak için yardımcı metot.
     * @return String - Kullanıcının e-posta adresi
     */
    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();  // Eğer principal UserDetails ise, e-posta döndürülür
        } else {
            return principal.toString();
        }
    }

    /**
     * Reservation nesnesini ReservationResponseDTO nesnesine dönüştürür.
     * @param reservation - Dönüştürülecek rezervasyon nesnesi
     * @return ReservationResponseDTO - Dönüştürülmüş rezervasyon DTO nesnesi
     */
    public ReservationResponseDTO convertToDto(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setReservationDate(reservation.getReservationDate());
        dto.setNumberOfGuests(reservation.getNumberOfGuests());
        dto.setStatus(reservation.getStatus().name());

        // User bilgisi ekleme
        UserResponseDTO userDto = new UserResponseDTO();
        userDto.setId(reservation.getUser().getId());
        userDto.setEmail(reservation.getUser().getEmail());
        dto.setUser(userDto);

        // Restaurant bilgisi ekleme
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
