package com.mycompany.Food_restaurant.repository;

import com.mycompany.Food_restaurant.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, String> {
    // PK = CodComprobante (String), no necesita query extra
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE DATE(p.fecCreacion) = CURRENT_DATE")
    Double sumMontoHoy();

}

