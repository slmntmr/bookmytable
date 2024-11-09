package com.bookmytable.controller;

import com.bookmytable.entity.business.Restaurant;
import com.bookmytable.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    // USER ve ADMIN rollerine sahip kullanıcıların tüm restoranları görebilmesini sağlayan metot
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/all")  // GET (http://localhost:8080/api/restaurants/all)
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        // Tüm restoranları almak için service katmanındaki metodu çağırır
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        // Restoran listesini HTTP 200 OK yanıtı ile döndürür
        return ResponseEntity.ok(restaurants);
    }

    // USER ve ADMIN rollerine sahip kullanıcıların belirli bir restoranı görebilmesini sağlayan metot
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/view/{id}")  // GET (http://localhost:8080/api/restaurants/view/{id})
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        // Belirli bir restoranı ID'ye göre almak için service katmanındaki metodu çağırır
        Optional<Restaurant> restaurant = restaurantService.getRestaurantById(id);
        // Restoran varsa HTTP 200 OK yanıtı ile döndürür, yoksa HTTP 404 NOT FOUND döner
        return restaurant.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Sadece ADMIN rolüne sahip kullanıcıların restoran ekleyebilmesini sağlayan metot
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create")  // POST (http://localhost:8080/api/restaurants/create)
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        // Yeni bir restoran oluşturmak için service katmanındaki metodu çağırır
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        // Oluşturulan restoranı HTTP 201 CREATED yanıtı ile döndürür
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
    }

    // Sadece ADMIN rolüne sahip kullanıcıların restoran güncelleyebilmesini sağlayan metot
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update/{id}")  // PUT (http://localhost:8080/api/restaurants/update/{id})
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant updatedRestaurant) {
        // Belirli bir restoranı ID'ye göre güncellemek için service katmanındaki metodu çağırır
        Restaurant restaurant = restaurantService.updateRestaurant(id, updatedRestaurant);
        // Güncellenen restoranı HTTP 200 OK yanıtı ile döndürür
        return ResponseEntity.ok(restaurant);
    }

    // Sadece ADMIN rolüne sahip kullanıcıların restoran silebilmesini sağlayan metot
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")  // DELETE (http://localhost:8080/api/restaurants/delete/{id})
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        // Belirli bir restoranı ID'ye göre silmek için service katmanındaki metodu çağırır
        restaurantService.deleteRestaurant(id);
        // Başarılı silme işleminden sonra HTTP 204 NO CONTENT yanıtı döner
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
