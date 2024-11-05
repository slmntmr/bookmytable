package com.bookmytable.repository;

import com.bookmytable.entity.business.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Bu sınıfın bir repository olduğunu belirtir
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // JpaRepository arayüzü, ID tipi Long olan Restaurant entity'si için CRUD işlemlerini sağlar
}
