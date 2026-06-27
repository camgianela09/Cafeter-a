/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Conexión.Conexion;
import java.sql.Connection;
import Modelo.Usuario;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gianella Castañeda
 */
public class Ctrl_Usuario {
    
    //Metodo para iniciar sesión 
    public boolean loginUser(Usuario objeto) throws SQLException{
     
        boolean respuesta = false;
        
        Connection cn = Conexion.getConexion();
        String sql = "select usuario from tb_usuario where usuario = '"+ objeto.getUsuario() +"' and password = '"+objeto.getPassword()+"'";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()){
                respuesta = true;                 
            }            
        } catch (SQLException e){
            System.out.println("Error al iniciar");
            throw new SQLException("Error al iniciar Sesión");
        }   
        return respuesta;
    }
    public Usuario loginUserObj(Usuario objeto) throws SQLException {
        Connection cn = Conexion.getConexion();
        String sql = "SELECT * FROM tb_usuario WHERE usuario = ? AND password = ?";
        try {
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, objeto.getUsuario());
            ps.setString(2, objeto.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario user = new Usuario();
                user.setIdUsuario(rs.getInt("idUsuario"));
                user.setNombre(rs.getString("nombre"));
                user.setApellido(rs.getString("apellido"));
                user.setUsuario(rs.getString("usuario"));
                user.setPassword(rs.getString("password"));
                user.setTelefono(rs.getString("telefono"));
                user.setEstado(rs.getInt("estado"));
                return user;
            }
            cn.close();
        } catch (SQLException e) {
            throw new SQLException("Error al iniciar Sesion");
        }
        return null;
    }

    
    public List<Usuario> listar(String criterio) throws SQLException {

        // Crear una lisra de clientes vacio
        List<Usuario> lista = new ArrayList<>();

        // crea la variable sql
        String sql = """
                 SELECT *
                 FROM tb_usuario
                 WHERE nombre LIKE ?
                    OR apellido LIKE ?
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

            // Ejecuta el preparestatement y asigna el valor del rs
            try (ResultSet rs = ps.executeQuery()) {

                // Mientras, el rs pueda avanzar
                while (rs.next()) {
                    // Crea un objeto (cliente) de la clase cliente
                    Usuario user = new Usuario();

                    // Asigna datos a sus atribitos al objeto cliente
                    user.setIdUsuario(rs.getInt("idUsuario"));
                    user.setNombre(rs.getString("nombre"));
                    user.setApellido(rs.getString("apellido"));
                    user.setUsuario(rs.getString("usuario"));
                    user.setPassword(rs.getString("password"));
                    user.setTelefono(rs.getString("telefono"));
                    user.setEstado(rs.getInt("estado"));

                    // Agrega el objeto cliente a la lista de tipo cliente
                    lista.add(user);
                }
            }
        }

        return lista;
    }
    
    public boolean guardar(Usuario user) throws SQLException {
        String sql = """
                     INSERT INTO tb_usuario
                     (nombre, apellido, usuario, password, telefono, estado)
                     VALUES (?, ?, ?, ?, ?, ?)
                     """;
        // Insertar dentro de la tabla () los campos (nombre, apellido, dni, telefono, direccion, estado) con los valores ("", "", etc)

        try (
                Connection cn = Conexion.getConexion(); 
                PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, user.getNombre());
            ps.setString(2, user.getApellido());
            ps.setString(3, user.getUsuario());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getTelefono());
            ps.setInt(6, user.getEstado());

            // Ejejcuta el ps, siempre devuelve un valor
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean actualizar(Usuario user) throws SQLException {

        String sql = """
                     UPDATE tb_usuario
                     SET nombre = ?,
                         apellido = ?,
                         usuario = ?,
                         password = ?,
                         telefono = ?,
                         estado = ?
                     WHERE idUsuario = ?
                     """;

        try (
                Connection cn = Conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, user.getNombre());
            ps.setString(2, user.getApellido());
            ps.setString(3, user.getUsuario());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getTelefono());
            ps.setInt(6, user.getEstado());
            ps.setInt(7, user.getIdUsuario());

            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean anular(Usuario user) throws SQLException {

        String sql = """
                     UPDATE tb_usuario
                     SET estado = ?
                     WHERE idUsuario = ?
                     """;
        try (
                Connection cn = Conexion.getConexion(); 
                PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, user.getEstado());
            ps.setInt(2, user.getIdUsuario());

            return ps.executeUpdate() > 0;
        }
    }
    
    // ELIMINAR
    public boolean eliminar(int idUsuario) throws SQLException {

        String sql = "DELETE FROM tb_usuario WHERE idUsuario = ?";

        try (
                Connection cn = Conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        }
    }

    // VALIDAR DNI REPETIDO
    public boolean existeUsuario(String usuario) throws SQLException {

        String sql = "SELECT usuario FROM tb_usuario WHERE usuario = ?";

        try (
                Connection cn = Conexion.getConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
}
