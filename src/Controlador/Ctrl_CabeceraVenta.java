package Controlador;

import Conexión.Conexion;
import Modelo.CabeceraVenta;
import Modelo.DetalleVenta;
import Modelo.Usuario;
import Modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Ctrl_CabeceraVenta {

    public List<CabeceraVenta> listar(String criterio) throws SQLException {
        List<CabeceraVenta> lista = new ArrayList<>();

        String sql = """
                 SELECT cv.idCabeceraVenta, cv.idCliente, cv.idUsuario, cv.valorPagar, cv.fechaVenta, cv.estado,
                        cl.nombre as cli_nombre, cl.apellido as cli_apellido, cl.dni as cli_dni,
                        u.nombre as usu_nombre, u.apellido as usu_apellido, u.usuario
                 FROM tb_cabecera_venta cv
                 INNER JOIN tb_cliente cl ON cv.idCliente = cl.idCliente
                 INNER JOIN tb_usuario u ON cv.idUsuario = u.idUsuario
                 WHERE cv.valorPagar LIKE ?
                    OR cv.fechaVenta LIKE ?
                 ORDER BY cv.fechaVenta
                 """;

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            String busqueda = "%" + criterio + "%";
            ps.setString(1, busqueda);
            ps.setString(2, busqueda);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CabeceraVenta cv = new CabeceraVenta();
                    cv.setIdCabeceraVenta(rs.getInt("idCabeceraVenta"));
                    cv.setIdCliente(rs.getInt("idCliente"));
                    cv.setIdUsuario(rs.getInt("idUsuario"));
                    cv.setValorPaga(rs.getDouble("valorPagar"));
                    cv.setFechaVenta(rs.getDate("fechaVenta"));
                    cv.setEstado(rs.getInt("estado"));

                    Usuario user = new Usuario();
                    user.setIdUsuario(rs.getInt("idUsuario"));
                    user.setNombre(rs.getString("usu_nombre"));
                    user.setApellido(rs.getString("usu_apellido"));
                    user.setUsuario(rs.getString("usuario"));
                    cv.setUsuario(user);

                    Cliente client = new Cliente();
                    client.setIdCliente(rs.getInt("idCliente"));
                    client.setNombre(rs.getString("cli_nombre"));
                    client.setApellido(rs.getString("cli_apellido"));
                    client.setDni(rs.getString("cli_dni"));
                    cv.setCliente(client);

                    lista.add(cv);
                }
            }
        }
        return lista;
    }

    public int guardar(CabeceraVenta cv) throws SQLException {
        String sql = """
                     INSERT INTO tb_cabecera_venta
                     (idCliente, idUsuario, valorPagar, fechaVenta, estado)
                     VALUES (?, ?, ?, ?, ?)
                     """;

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, cv.getIdCliente());
            ps.setInt(2, cv.getIdUsuario());
            ps.setDouble(3, cv.getValorPaga());
            ps.setDate(4, cv.getFechaVenta());
            ps.setInt(5, cv.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public boolean guardarVentaCompleta(CabeceraVenta cabecera, List<DetalleVenta> detalles) throws SQLException {
        Connection cn = null;
        try {
            cn = Conexion.getConexion();
            cn.setAutoCommit(false);

            String sqlCab = """
                            INSERT INTO tb_cabecera_venta
                            (idCliente, idUsuario, valorPagar, fechaVenta, estado)
                            VALUES (?, ?, ?, ?, ?)
                            """;
            int idCabecera;
            try (PreparedStatement ps = cn.prepareStatement(sqlCab, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, cabecera.getIdCliente());
                ps.setInt(2, cabecera.getIdUsuario());
                ps.setDouble(3, cabecera.getValorPaga());
                ps.setDate(4, cabecera.getFechaVenta());
                ps.setInt(5, cabecera.getEstado());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idCabecera = rs.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID de la cabecera");
                    }
                }
            }

            String sqlDet = """
                            INSERT INTO tb_detalle_venta
                            (idCabeceraVenta, idProducto, cantidad, precioUnitario, subtotal, descuento, totalPagar, estado)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                            """;
            try (PreparedStatement ps = cn.prepareStatement(sqlDet)) {
                for (DetalleVenta d : detalles) {
                    ps.setInt(1, idCabecera);
                    ps.setInt(2, d.getIdProducto());
                    ps.setInt(3, d.getCantidad());
                    ps.setDouble(4, d.getPrecioUnitario());
                    ps.setDouble(5, d.getSubtotal());
                    ps.setDouble(6, d.getDescuento());
                    ps.setDouble(7, d.getTotalPagar());
                    ps.setInt(8, d.getEstado());
                    ps.executeUpdate();
                }
            }

            cn.commit();
            return true;
        } catch (SQLException e) {
            if (cn != null) {
                try {
                    cn.rollback();
                } catch (SQLException ex) {
                    throw new SQLException("Error al hacer rollback: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (cn != null) {
                try {
                    cn.setAutoCommit(true);
                    cn.close();
                } catch (SQLException ex) {
                    throw new SQLException("Error al cerrar conexion: " + ex.getMessage());
                }
            }
        }
    }

    public boolean actualizar(CabeceraVenta cv) throws SQLException {
        String sql = """
                     UPDATE tb_cabecera_venta
                     SET idCliente = ?,
                         idUsuario = ?,
                         valorPagar = ?,
                         fechaVenta = ?,
                         estado = ?
                     WHERE idCabeceraVenta = ?
                     """;

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, cv.getIdCliente());
            ps.setInt(2, cv.getIdUsuario());
            ps.setDouble(3, cv.getValorPaga());
            ps.setDate(4, cv.getFechaVenta());
            ps.setInt(5, cv.getEstado());
            ps.setInt(6, cv.getIdCabeceraVenta());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean anular(CabeceraVenta cv) throws SQLException {
        String sql = """
                     UPDATE tb_cabecera_venta
                     SET estado = ?
                     WHERE idCabeceraVenta = ?
                     """;

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, cv.getEstado());
            ps.setInt(2, cv.getIdCabeceraVenta());

            return ps.executeUpdate() > 0;
        }
    }
}
