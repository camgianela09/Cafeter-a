/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Conexión.Conexion;
import Modelo.Categoria;
import Modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author AMIRA
 */
public class Ctrl_Producto {

    // Funcion devuelve una lista de clientes, recibe un parametro (criterio) para buscar
    public List<Producto> listar(String criterio) throws SQLException {

        // Crear una lisra de clientes vacio
        List<Producto> lista = new ArrayList<>();

        // crea la variable sql
        String sql = """
                 SELECT *
                 FROM tb_producto
                 WHERE nombre LIKE ?
                    OR cantidad LIKE ?
                    OR precio LIKE ?
                    OR descripcion LIKE ?
                 ORDER BY nombre
                 """;
// Seleccionar todos los campos desde la tabla (tb_cliente) donde nombre debe coincidir con algun caratacter del parametro
// o apellido debe coincidir con algun caratacter del parametro o dni debe coincidir con algun caratacter del parametro
// ordenar por apellido, nombre. Por defecto se ordena de forma ascendente

        try (
                // Conecta la DB
                Connection cn = Conexion.getConexion(); // Prepara la consulta (select) o sentencias (insertar, actualizar, eliminar), asociado a la conexion
                 PreparedStatement ps = cn.prepareStatement(sql)) {

            // el % es un comodin que representa cualquier caracter
            String busqueda = "%" + criterio + "%";

            // Asigna valores a los parametros de la consulta
            ps.setString(1, busqueda);
            ps.setString(2, busqueda);
            ps.setString(3, busqueda);
            ps.setString(4, busqueda);

            // Ejecuta el preparestatement y asigna el valor del rs
            try (ResultSet rs = ps.executeQuery()) {

                // Mientras, el rs pueda avanzar
                while (rs.next()) {
                    // Crea un objeto (pro) de la clase pro
                    Producto pro = new Producto();
                    Categoria cat = new Categoria();

                    // Asigna datos a sus atribitos al objeto pro
                    pro.setIdProducto(rs.getInt("idProducto"));
                    pro.setNombre(rs.getString("nombre"));
                    pro.setCantidad(rs.getInt("cantidad"));
                    pro.setPrecio(rs.getDouble("precio"));
                    pro.setDescripcion(rs.getString("descripcion"));
                    pro.setEstado(rs.getInt("estado"));

                    cat.setIdCategoria(rs.getInt("idCategoria"));
                    cat.setDescripcion(rs.getString("descripcion"));
                    pro.setCategoria(cat);

                    // Agrega el objeto pro a la lista de tipo pro
                    lista.add(pro);
                }
            }
        }

        return lista;
    }

    // INSERTAR
    public boolean guardar(Producto pro) throws SQLException {

        String sql = """
                     INSERT INTO tb_producto
                     (nombre, cantidad, precio, descripcion, idCategoria, estado)
                     VALUES (?, ?, ?, ?, ?, ?)
                     """;
        // Insertar dentro de la tabla () los campos (nombre, apellido, dni, telefono, direccion, estado) con los valores ("", "", etc)

        try (
                Connection cn = Conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, pro.getNombre());
            ps.setInt(2, pro.getCantidad());
            ps.setDouble(3, pro.getPrecio());
            ps.setString(4, pro.getDescripcion());
            ps.setInt(5, pro.getCategoria().getIdCategoria());
            ps.setInt(6, pro.getEstado());

            // Ejejcuta el ps, siempre devuelve un valor
            return ps.executeUpdate() > 0;
        }
    }

    // BUSCAR POR ID
    public Producto buscarPorId(int idProducto) throws SQLException {

        String sql = "SELECT * FROM tb_producto WHERE idProducto = ?";

        try (
                Connection cn = Conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Producto pro = new Producto();

                    pro.setIdProducto(rs.getInt("idProducto"));
                    pro.setNombre(rs.getString("nombre"));
                    pro.setCantidad(rs.getInt("cantidad"));
                    pro.setPrecio(rs.getDouble("precio"));
                    pro.setDescripcion(rs.getString("descripcion"));
                    
                    Categoria cat = new Categoria();
                    cat.setIdCategoria(rs.getInt("idCategoria"));
                    pro.setCategoria(cat);
                    
                    pro.setEstado(rs.getInt("estado"));

                    return pro;
                }
            }
        }

        return null;
    }

    // ACTUALIZAR
    public boolean actualizar(Producto pro) throws SQLException {

        String sql = """
                     UPDATE tb_producto
                     SET nombre = ?,
                         cantidad = ?,
                         precio = ?,
                         descripcion = ?,
                         idCategoria = ?,
                         estado = ?
                     WHERE idProducto = ?
                     """;

        try (
                Connection cn = Conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, pro.getNombre());
            ps.setInt(2, pro.getCantidad());
            ps.setDouble(3, pro.getPrecio());
            ps.setString(4, pro.getDescripcion());
            ps.setInt(5, pro.getCategoria().getIdCategoria());
            ps.setInt(6, pro.getEstado());
            ps.setInt(7, pro.getIdProducto());

            return ps.executeUpdate() > 0;
        }
    }

    
    // ELIMINAR
    public boolean eliminar(int idProducto) throws SQLException {

        String sql = "DELETE FROM tb_producto WHERE idProducto = ?";

        try (
                Connection cn = Conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0;
        }
    }

    // VALIDAR CATEGORIA REPETIDO
    public boolean existeIdCategoria(int idCategoria, String descripcion) throws SQLException {

        String sql = "SELECT idCategoria FROM tb_producto WHERE idCategoria = ? and descripcion = ?";

        try (
                Connection cn = Conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ps.setString(2, descripcion);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
