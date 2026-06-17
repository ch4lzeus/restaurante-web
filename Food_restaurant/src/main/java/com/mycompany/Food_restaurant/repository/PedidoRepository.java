package com.mycompany.Food_restaurant.repository;

import com.mycompany.Food_restaurant.entity.Pedido;
import com.mycompany.Food_restaurant.entity.PedidoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, String> {

    // Siguiente NroPedido automatico
    @Query("SELECT p.nroPedido FROM Pedido p ORDER BY p.nroPedido DESC")
    List<String> findAllNroPedidoOrdenados();

    // Contar por estado para las tarjetas del dashboard
    long countByEstado(String estado);

    // Contar delivery activos
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.esLocal = 0 AND p.estado NOT IN ('ENTREGADO', 'CANCELADO')")
    long countDeliveryActivos();

    @Query("SELECT p FROM Pedido p WHERE p.estado NOT IN ('ENTREGADO', 'CANCELADO') ORDER BY p.fecPedido DESC")
    List<Pedido> findOperativos();

    @Query("SELECT p FROM Pedido p WHERE p.esLocal = 0 AND p.estado NOT IN ('ENTREGADO', 'CANCELADO') ORDER BY p.fecPedido DESC")
    List<Pedido> findDeliveryOperativos();

    List<Pedido> findByEstado(String estado);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE DATE(p.fecPedido) = CURRENT_DATE")
    long countPedidosHoy();


}
