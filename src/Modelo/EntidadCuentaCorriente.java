/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JorgeA
 */
public class EntidadCuentaCorriente extends Conexion {

    public boolean registrar(Cuenta c) {
        PreparedStatement ps = null;
        try {
            Connection con = getConect();
            String sql = "INSERT INTO cuentacorriente (id,dniCliente,fecha,debe,haber)  VALUES (NULL,?,?,?,?)";
            try {
                ps = con.prepareStatement(sql);
                
                ps.setInt(1, c.getDniCliente());
                ps.setDate(2, c.getFecha());
                ps.setDouble(3, c.getDebe());
                ps.setDouble(4, c.getHaber());
                
                
                ps.execute();
                con.close();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(EntidadTarifa.class.getName()).log(Level.SEVERE, null, ex);

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EntidadTarifa.class.getName()).log(Level.SEVERE, null, ex);
             
            return false;
            
           
        }
       
        return false;
        
    }
}
