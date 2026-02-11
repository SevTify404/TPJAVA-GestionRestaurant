/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author sevtify
 */public class Connexion {
    private final static String URL = "jdbc:mysql://127.0.0.1:3306/gestPerso";
    private final static  String USER = System.getProperty("db.utilisateur");
    private final static String PASSWORD = System.getProperty("db.mdp");
    
    public static Connection toConnect(){
        Connection ma_connexion;
        
        try {
            ma_connexion = DriverManager.getConnection(URL, USER, PASSWORD);
            return ma_connexion;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        
        return null;
        
    }
    
}
