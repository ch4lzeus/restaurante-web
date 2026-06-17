package com.mycompany.Food_restaurant.repository;

import com.mycompany.Food_restaurant.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, String> {
    @Query("SELECT m.nroMesa FROM Mesa m ORDER BY m.nroMesa DESC")
    List<String> findAllNroMesaOrdenados();
}

