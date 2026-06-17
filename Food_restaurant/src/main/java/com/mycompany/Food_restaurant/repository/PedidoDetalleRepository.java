package com.mycompany.Food_restaurant.repository;

import com.mycompany.Food_restaurant.entity.PedidoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, String> {

    // Siguiente CodPedidoDetalle automatico
    @Query("SELECT pd.codPedidoDetalle FROM PedidoDetalle pd ORDER BY pd.codPedidoDetalle DESC")
    List<String> findAllCodOrdenados();

    List<PedidoDetalle> findByNroPedidoOrderByCodPedidoDetalle(String nroPedido);
}
