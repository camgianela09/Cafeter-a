/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Conexión;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Gianella Castañeda
 */
public class Conexion {
    
    private static final String URL = "jdbc:mysql://localhost:3306/bd_cafeteria";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";

    private Conexion() {
        // Evita instanciación
    }

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

//conexión local
    /*
    public static Connection conectar() {

        try {

            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/bd_cafeteria", "root", "1234");
            return cn;

        } catch (SQLException e) {
            System.out.println("Erro en la conexión local" + e);
        }
        return null;
    }
*/
}
