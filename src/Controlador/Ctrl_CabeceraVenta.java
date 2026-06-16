/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Conexión.Conexion;
import Modelo.CabeceraVenta;
import Modelo.Usuario;
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
public class Ctrl_CabeceraVenta {
    // Funcion devuelve una lista de clientes, recibe un parametro (criterio) para buscar
    public List<CabeceraVenta> listar(String criterio) throws SQLException {

        // Crear una lisra de clientes vacio
        List<CabeceraVenta> lista = new ArrayList<>();

        // crea la variable sql
        String sql = """
                 SELECT *
                 FROM tb_cabecera_venta
                 WHERE valorPaga LIKE ?
                    OR fechaVenta LIKE ?
                 ORDER BY fechaVenta
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

            // Ejecuta el preparestatement y asigna el valor del rs
            try (ResultSet rs = ps.executeQuery()) {

                // Mientras, el rs pueda avanzar
                while (rs.next()) {
                    // Crea un objeto (pro) de la clase pro
                    CabeceraVenta cv = new CabeceraVenta();
                    Usuario user = new Usuario();
                    Cliente client = new Cliente();

                    // Asigna datos a sus atribitos al objeto pro
                    cv.setIdCabeceraVenta(rs.getInt("idCabeceraVenta"));
                    cv.setValorPaga(rs.getDouble("valorPaga"));
                    cv.setFechaVenta(rs.getDate("fechaVenta"));
                    cv.setEstado(rs.getInt("estado"));

                    user.setIdUsuario(rs.getInt("idUsuario"));
                    user.setNombre(rs.getString("nombre"));
                    user.setApellido(rs.getString("apellido"));
                    user.setUsuario(rs.getString("usuario"));
                    user.setPassword(rs.getString("password"));
                    user.setTelefono(rs.getString("telefono"));
                    user.setEstado(rs.getInt("estado"));
                    cv.setUsuario(user);
                    
                    client.setIdCliente(rs.getInt("idCliente"));
                    client.setNombre(rs.getString("nombre"));
                    client.setApellido(rs.getString("apellido"));
                    client.setDni(rs.getString("dni"));
                    client.setTelefono(rs.getString("telefono"));
                    client.setDireccion(rs.getString("direccion"));
                    client.setEstado(rs.getInt("estado"));
                    cv.setCliente(client);

                    // Agrega el objeto pro a la lista de tipo pro
                    lista.add(cv);
                }
            }
        }

        return lista;
    }
}

