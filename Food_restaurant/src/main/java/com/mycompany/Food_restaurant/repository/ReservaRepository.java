package com.mycompany.Food_restaurant.repository;

import com.mycompany.Food_restaurant.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, String> {

    @Query("SELECT r.codReserva FROM Reserva r ORDER BY r.codReserva DESC")
    List<String> findAllCodReservaOrdenados();
}