package com.mycompany.Food_restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mycompany.Food_restaurant.entity.Comprobante;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Comprobante, String> {

    // ── Total ventas cobradas en rango de fechas ──────────────────────────────
    @Query("""
        SELECT COALESCE(SUM(pg.monto), 0)
        FROM Pago pg
        JOIN Comprobante c ON c.codComprobante = pg.codComprobante
        WHERE (:desde IS NULL OR DATE(c.fecEmision) >= :desde)
          AND (:hasta IS NULL OR DATE(c.fecEmision) <= :hasta)
        """)
    Double sumVentasTotales(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    // ── Cantidad de comprobantes emitidos ─────────────────────────────────────
    @Query("""
        SELECT COUNT(c)
        FROM Comprobante c
        WHERE (:desde IS NULL OR DATE(c.fecEmision) >= :desde)
          AND (:hasta IS NULL OR DATE(c.fecEmision) <= :hasta)
        """)
    long countComprobantes(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    // ── Cantidad de pedidos cobrados ──────────────────────────────────────────
    @Query("""
        SELECT COUNT(DISTINCT c.nroPedido)
        FROM Comprobante c
        WHERE (:desde IS NULL OR DATE(c.fecEmision) >= :desde)
          AND (:hasta IS NULL OR DATE(c.fecEmision) <= :hasta)
        """)
    long countPedidosCobrados(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    // ── Plato más vendido ─────────────────────────────────────────────────────
    @Query("""
        SELECT a.nombre
        FROM ComprobanteDetalle cd
        JOIN Comprobante c ON c.codComprobante = cd.codComprobante
        JOIN Alimento a ON a.codAlimento = cd.codAlimento
        WHERE (:desde IS NULL OR DATE(c.fecEmision) >= :desde)
          AND (:hasta IS NULL OR DATE(c.fecEmision) <= :hasta)
        GROUP BY a.codAlimento, a.nombre
        ORDER BY SUM(cd.cantidad) DESC
        """)
    List<String> findPlatoMasVendido(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    // ── Ventas por pedido ─────────────────────────────────────────────────────
    @Query("""
        SELECT c.nroPedido, c.codCliente,
               CASE WHEN p.esLocal = 1 THEN 'Local' ELSE 'Delivery' END,
               pg.monto, c.fecEmision
        FROM Comprobante c
        JOIN Pago pg ON pg.codComprobante = c.codComprobante
        JOIN Pedido p ON p.nroPedido = c.nroPedido
        WHERE (:desde IS NULL OR DATE(c.fecEmision) >= :desde)
          AND (:hasta IS NULL OR DATE(c.fecEmision) <= :hasta)
        ORDER BY c.fecEmision DESC
        """)
    List<Object[]> findVentasPorPedido(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    // ── Platos vendidos con cantidad y total ──────────────────────────────────
    @Query("""
        SELECT a.nombre, cat.nombre, SUM(cd.cantidad), SUM(cd.subTotal)
        FROM ComprobanteDetalle cd
        JOIN Comprobante c ON c.codComprobante = cd.codComprobante
        JOIN Alimento a ON a.codAlimento = cd.codAlimento
        JOIN Categoria cat ON cat.codCategoria = a.codCategoria
        WHERE (:desde IS NULL OR DATE(c.fecEmision) >= :desde)
          AND (:hasta IS NULL OR DATE(c.fecEmision) <= :hasta)
        GROUP BY a.codAlimento, a.nombre, cat.nombre
        ORDER BY SUM(cd.cantidad) DESC
        """)
    List<Object[]> findPlatosVendidos(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    // ── Comprobantes emitidos ─────────────────────────────────────────────────
    @Query("""
        SELECT c.nroComprobante, tc.nombre, c.nroPedido, c.codCliente,
               mp.nombre, pg.monto, c.fecEmision
        FROM Comprobante c
        JOIN Pago pg ON pg.codComprobante = c.codComprobante
        JOIN TipoComprobante tc ON tc.codTipoComprobante = c.codTipoComprobante
        JOIN MetodoPago mp ON mp.codMetodoPago = pg.codMetodoPago
        WHERE (:desde IS NULL OR DATE(c.fecEmision) >= :desde)
          AND (:hasta IS NULL OR DATE(c.fecEmision) <= :hasta)
        ORDER BY c.fecEmision DESC
        """)
    List<Object[]> findComprobantesEmitidos(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}