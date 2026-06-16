/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Conexión.Conexion;
import Modelo.Categoria;
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
public class Ctrl_Categoria {
    // Funcion devuelve una lista de categoria, recibe un parametro (criterio) para buscar
    public List<Categoria> listar(String criterio) throws SQLException {

        // Crear una lisra de clientes vacio
        List<Categoria> lista = new ArrayList<>();

        // crea la variable sql
        String sql = """
                 SELECT *
                 FROM tb_categoria
                 WHERE descripcion LIKE ?
                 ORDER BY descripcion
                 """;
// Seleccionar todos los campos desde la tabla (tb_categoria) donde nombre debe coincidir con algun caratacter del parametro
// o apellido debe coincidir con algun caratacter del parametro o dni debe coincidir con algun caratacter del parametro
// ordenar por apellido, nombre. Por defecto se ordena de forma ascendente

        try (
                // Conecta la DB
                Connection cn = Conexion.getConexion(); 
                // Prepara la consulta (select) o sentencias (insertar, actualizar, eliminar), asociado a la conexion
                PreparedStatement ps = cn.prepareStatement(sql)) {

            // el % es un comodin que representa cualquier caracter
            String busqueda = "%" + criterio + "%";

            // Asigna valores a los parametros de la consulta
            ps.setString(1, busqueda);

            // Ejecuta el preparestatement y asigna el valor del rs
            try (ResultSet rs = ps.executeQuery()) {

                // Mientras, el rs pueda avanzar
                while (rs.next()) {
                    // Crea un objeto (cliente) de la clase cliente
                    Categoria cat = new Categoria();

                    // Asigna datos a sus atribitos al objeto cliente
                    cat.setIdCategoria(rs.getInt("idCategoria"));
                    cat.setDescripcion(rs.getString("descripcion"));
                    cat.setEstado(rs.getInt("estado"));

                    // Agrega el objeto cliente a la lista de tipo categoria
                    lista.add(cat);
                }
            }
        }

        return lista;
    }

    // INSERTAR
    public boolean guardar(Categoria cat) throws SQLException {

        String sql = """
                     INSERT INTO tb_categoria
                     (descripcion , estado)
                     VALUES (?, ?)
                     """;
        // Insertar dentro de la tabla () los campos (nombre, apellido, dni, telefono, direccion, estado) con los valores ("", "", etc)

        try (
                Connection cn = Conexion.getConexion(); 
                PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, cat.getDescripcion());
            ps.setInt(2, cat.getEstado());

            // Ejejcuta el ps, siempre devuelve un valor
            return ps.executeUpdate() > 0;
        }
    }

    // BUSCAR POR ID
    public Categoria buscarPorId(int idCategoria) throws SQLException {

        String sql = "SELECT * FROM tb_categoria WHERE idCategoria = ?";

        try (
                Connection cn = Conexion.getConexion(); 
                PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Categoria cat = new Categoria();

                    cat.setIdCategoria(rs.getInt("idCategoria"));
                    cat.setDescripcion(rs.getString("descripcion"));
                    cat.setEstado(rs.getInt("estado"));

                    return cat;
                }
            }
        }

        return null;
    }

    // ACTUALIZAR
    public boolean actualizar(Categoria cat) throws SQLException {

        String sql = """
                     UPDATE tb_categoria
                     SET descripcion = ?,
                         estado = ?
                     WHERE idCategoria = ?
                     """;

        try (
                Connection cn = Conexion.getConexion(); 
                PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, cat.getDescripcion());
            ps.setInt(2, cat.getEstado());
            ps.setInt(3, cat.getIdCategoria());

            return ps.executeUpdate() > 0;
        }
    }

    // ELIMINAR
    public boolean eliminar(int idCategoria) throws SQLException {

        String sql = "DELETE FROM tb_categoria WHERE idCategoria = ?";

        try (
                Connection cn = Conexion.getConexion(); 
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            return ps.executeUpdate() > 0;
        }
    }

    // VALIDAR DESCRIPCION REPETIDO
    public boolean existeCategoria(String descripcion) throws SQLException {

        String sql = "SELECT descripcion FROM tb_categoria WHERE descripcion = ?";

        try (
                Connection cn = Conexion.getConexion(); 
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, descripcion);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    // Anular
    public boolean anular(Categoria cat) throws SQLException {

        String sql = """
                     UPDATE tb_categoria
                     SET estado = ?
                     WHERE idCategoria = ?
                     """;
        try (
                Connection cn = Conexion.getConexion(); 
                PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, cat.getEstado());
            ps.setInt(2, cat.getIdCategoria());

            return ps.executeUpdate() > 0;
        }
    }
}
