package com.mycompany.Food_restaurant.repository;

import com.mycompany.Food_restaurant.entity.ComprobanteDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComprobanteDetalleRepository extends JpaRepository<ComprobanteDetalle, String> {

    @Query("SELECT cd.codComprobanteDetalle FROM ComprobanteDetalle cd ORDER BY cd.codComprobanteDetalle DESC")
    List<String> findAllCodOrdenados();
}