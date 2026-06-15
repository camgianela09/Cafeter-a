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
public class Conexión {

//conexión local
    public static Connection conectar() {

        try {

            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/bd_cafeteria", "root", "1234");
            return cn;

        } catch (SQLException e) {
            System.out.println("Erro en la conexión local" + e);
        }
        return null;
    }
}
