package Controlador;

import Conexión.Conexion;
import Modelo.DetalleVenta;
import Modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Ctrl_DetalleVenta {

    public List<DetalleVenta> listar(int idCabeceraVenta) throws SQLException {
        List<DetalleVenta> lista = new ArrayList<>();

        String sql = """
                     SELECT d.*, p.nombre as producto_nombre, p.precio as producto_precio
                     FROM tb_detalle_venta d
                     INNER JOIN tb_producto p ON d.idProducto = p.idProducto
                     WHERE d.idCabeceraVenta = ?
                     ORDER BY d.idDetalleVenta
                     """;

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idCabeceraVenta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta d = new DetalleVenta();
                    d.setIdDetalleVenta(rs.getInt("idDetalleVenta"));
                    d.setIdCabeceraVenta(rs.getInt("idCabeceraVenta"));
                    d.setIdProducto(rs.getInt("idProducto"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecioUnitario(rs.getDouble("precioUnitario"));
                    d.setSubtotal(rs.getDouble("subtotal"));
                    d.setDescuento(rs.getDouble("descuento"));
                    d.setTotalPagar(rs.getDouble("totalPagar"));
                    d.setEstado(rs.getInt("estado"));

                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("idProducto"));
                    p.setNombre(rs.getString("producto_nombre"));
                    p.setPrecio(rs.getDouble("producto_precio"));
                    d.setProducto(p);

                    lista.add(d);
                }
            }
        }
        return lista;
    }

    public boolean guardar(DetalleVenta d) throws SQLException {
        String sql = """
                     INSERT INTO tb_detalle_venta
                     (idCabeceraVenta, idProducto, cantidad, precioUnitario, subtotal, descuento, totalPagar, estado)
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                     """;

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, d.getIdCabeceraVenta());
            ps.setInt(2, d.getIdProducto());
            ps.setInt(3, d.getCantidad());
            ps.setDouble(4, d.getPrecioUnitario());
            ps.setDouble(5, d.getSubtotal());
            ps.setDouble(6, d.getDescuento());
            ps.setDouble(7, d.getTotalPagar());
            ps.setInt(8, d.getEstado());

            return ps.executeUpdate() > 0;
        }
    }

    public DetalleVenta buscarPorId(int idDetalleVenta) throws SQLException {
        String sql = "SELECT * FROM tb_detalle_venta WHERE idDetalleVenta = ?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idDetalleVenta);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DetalleVenta d = new DetalleVenta();
                    d.setIdDetalleVenta(rs.getInt("idDetalleVenta"));
                    d.setIdCabeceraVenta(rs.getInt("idCabeceraVenta"));
                    d.setIdProducto(rs.getInt("idProducto"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecioUnitario(rs.getDouble("precioUnitario"));
                    d.setSubtotal(rs.getDouble("subtotal"));
                    d.setDescuento(rs.getDouble("descuento"));
                    d.setTotalPagar(rs.getDouble("totalPagar"));
                    d.setEstado(rs.getInt("estado"));
                    return d;
                }
            }
        }
        return null;
    }

    public boolean anular(DetalleVenta d) throws SQLException {
        String sql = "UPDATE tb_detalle_venta SET estado = ? WHERE idDetalleVenta = ?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, d.getEstado());
            ps.setInt(2, d.getIdDetalleVenta());

            return ps.executeUpdate() > 0;
        }
    }
}
