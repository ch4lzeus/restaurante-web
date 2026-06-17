package com.mycompany.Food_restaurant.repository;

import com.mycompany.Food_restaurant.entity.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, String> { }