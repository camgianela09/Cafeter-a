/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Conexión.Conexión;
import java.sql.Connection;
import Modelo.Usuario;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Gianella Castañeda
 */
public class Ctrl_Usuario {
    
    //Metodo para iniciar sesión 
    public boolean loginUser(Usuario objeto){
     
        boolean respuesta = false;
        
        Connection cn = Conexión.conectar();
        String sql = "select usuario, password from tb_usuario where usuario = '"+ objeto.getUsuario() +"' and password = '"+objeto.getPassword()+"'";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()){
                respuesta = true; 
                
            }
            
        } catch (SQLException e){
            System.out.println("Error al iniciar");
            JOptionPane.showMessageDialog(null, "Error al iniciar Sesión");
        }   
        return respuesta;
    }
}
