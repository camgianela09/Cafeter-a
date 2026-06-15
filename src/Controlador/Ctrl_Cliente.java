/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Conexión.Conexion;
import Modelo.Cliente;
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
public class Ctrl_Cliente {

    // Funcion devuelve una lista de clientes, recibe un parametro (criterio) para buscar
    public List<Cliente> listar(String criterio) throws SQLException {

        // Crear una lisra de clientes vacio
        List<Cliente> lista = new ArrayList<>();

        // crea la variable sql
        String sql = """
                 SELECT *
                 FROM tb_cliente
                 WHERE nombre LIKE ?
                    OR apellido LIKE ?
                    OR dni LIKE ?
                 ORDER BY apellido, nombre
                 """;
// Seleccionar todos los campos desde la tabla (tb_cliente) donde nombre debe coincidir con algun caratacter del parametro
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
            ps.setString(2, busqueda);
            ps.setString(3, busqueda);

            // Ejecuta el preparestatement y asigna el valor del rs
            try (ResultSet rs = ps.executeQuery()) {

                // Mientras, el rs pueda avanzar
                while (rs.next()) {
                    // Crea un objeto (cliente) de la clase cliente
                    Cliente cliente = new Cliente();

                    // Asigna datos a sus atribitos al objeto cliente
                    cliente.setIdCliente(rs.getInt("idCliente"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellido(rs.getString("apellido"));
                    cliente.setDni(rs.getString("dni"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setDireccion(rs.getString("direccion"));
                    cliente.setEstado(rs.getInt("estado"));

                    // Agrega el objeto cliente a la lista de tipo cliente
                    lista.add(cliente);
                }
            }
        }

        return lista;
    }

    // INSERTAR
    public boolean guardar(Cliente cliente) throws SQLException {

        String sql = """
                     INSERT INTO tb_cliente
                     (nombre, apellido, dni, telefono, direccion, estado)
                     VALUES (?, ?, ?, ?, ?, ?)
                     """;
        // Insertar dentro de la tabla () los campos (nombre, apellido, dni, telefono, direccion, estado) con los valores ("", "", etc)

        try (
                Connection cn = Conexion.getConexion(); 
                PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getDni());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getDireccion());
            ps.setInt(6, cliente.getEstado());

            // Ejejcuta el ps, siempre devuelve un valor
            return ps.executeUpdate() > 0;
        }
    }

    // BUSCAR POR ID
    public Cliente buscarPorId(int idCliente) throws SQLException {

        String sql = "SELECT * FROM tb_cliente WHERE idCliente = ?";

        try (
                Connection cn = Conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Cliente cliente = new Cliente();

                    cliente.setIdCliente(rs.getInt("idCliente"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellido(rs.getString("apellido"));
                    cliente.setDni(rs.getString("dni"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setDireccion(rs.getString("direccion"));
                    cliente.setEstado(rs.getInt("estado"));

                    return cliente;
                }
            }
        }

        return null;
    }

    // ACTUALIZAR
    public boolean actualizar(Cliente cliente) throws SQLException {

        String sql = """
                     UPDATE tb_cliente
                     SET nombre = ?,
                         apellido = ?,
                         dni = ?,
                         telefono = ?,
                         direccion = ?,
                         estado = ?
                     WHERE idCliente = ?
                     """;

        try (
                Connection cn = Conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getDni());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getDireccion());
            ps.setInt(6, cliente.getEstado());
            ps.setInt(7, cliente.getIdCliente());

            return ps.executeUpdate() > 0;
        }
    }

    // ELIMINAR
    public boolean eliminar(int idCliente) throws SQLException {

        String sql = "DELETE FROM tb_cliente WHERE idCliente = ?";

        try (
                Connection cn = Conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            return ps.executeUpdate() > 0;
        }
    }

    // VALIDAR DNI REPETIDO
    public boolean existeDni(String dni) throws SQLException {

        String sql = "SELECT dni FROM tb_cliente WHERE dni = ?";

        try (
                Connection cn = Conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
