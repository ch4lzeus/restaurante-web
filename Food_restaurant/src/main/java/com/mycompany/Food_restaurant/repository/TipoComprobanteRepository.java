package com.mycompany.Food_restaurant.repository;

import com.mycompany.Food_restaurant.entity.TipoComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoComprobanteRepository extends JpaRepository<TipoComprobante, String> { }
