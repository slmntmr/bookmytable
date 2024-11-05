package com.bookmytable.service;

import com.bookmytable.entity.business.Restaurant;
import com.bookmytable.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;



    // Tüm restoranları listeleme işlevi
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // ID ile restoran bulma işlevi
    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }

    // Yeni bir restoran ekleme işlevi
    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    // Var olan restoranı güncelleme işlevi
    public Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant) {
        return restaurantRepository.findById(id)
                .map(restaurant -> {
                    restaurant.setName(updatedRestaurant.getName());
                    restaurant.setAddress(updatedRestaurant.getAddress());
                    restaurant.setPhone(updatedRestaurant.getPhone());
                    restaurant.setDescription(updatedRestaurant.getDescription());
                    return restaurantRepository.save(restaurant);
                })
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    // Restoranı ID ile silme işlevi
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }
}
