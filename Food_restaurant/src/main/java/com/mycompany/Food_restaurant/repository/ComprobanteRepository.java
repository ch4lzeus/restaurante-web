package com.mycompany.Food_restaurant.repository;

import com.mycompany.Food_restaurant.entity.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComprobanteRepository extends JpaRepository<Comprobante, String> {

    @Query("SELECT c.codComprobante FROM Comprobante c ORDER BY c.codComprobante DESC")
    List<String> findAllCodOrdenados();

    @Query("SELECT c FROM Comprobante c ORDER BY c.fecEmision DESC")
    List<Comprobante> findAllOrderByFecEmisionDesc();
}