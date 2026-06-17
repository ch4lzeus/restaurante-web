package com.mycompany.Food_restaurant.repository;

import com.mycompany.Food_restaurant.entity.CorrelativoComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CorrelativoComprobanteRepository extends JpaRepository<CorrelativoComprobante, String> {

    Optional<CorrelativoComprobante> findByCodTipoComprobante(String codTipoComprobante);
}