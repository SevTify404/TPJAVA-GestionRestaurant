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
 * @param <TypeDesEntity>
 */
public abstract class AbstractDAO<TypeDesEntity> implements InterfaceDAO<TypeDesEntity>{
    
    // Cette classe va etre hérité par toutes les classes de DAO, çà fait + POO
    // J'ai ajouté la méthode de connexion ici pour qu'on ai plus a faire des 
    // import dans les DAO meme donc chaque DAO vien déja avec sa 
    
    private final static String URL = "jdbc:mysql://127.0.0.1:3306/gestionResto";
    private final static  String USER = System.getProperty("db.utilisateur");
    private final static String PASSWORD = System.getProperty("db.mdp");
    
    private static Connection connection;

    protected Connection toConnect() {
       Connection ma_connexion = null;
        
        try {
            ma_connexion = DriverManager.getConnection(URL, USER, PASSWORD);
            return ma_connexion;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        return connection;
    }
}
