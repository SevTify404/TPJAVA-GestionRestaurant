/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import utilitaires.VariablesEnvirennement;
import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 * @author sevtify
 */
public class Connexion {
    private static final HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/gestionResto");
        config.setUsername(VariablesEnvirennement.recupererVariables().get("UTILISATEUR_BD"));
        config.setPassword(VariablesEnvirennement.recupererVariables().get("MDP_BD"));
        config.setMaximumPoolSize(5);
        ds = new HikariDataSource(config);
    }
    
    protected static Connection getConnection() throws SQLException{
        System.out.println((VariablesEnvirennement.recupererVariables().get("UTILISATEUR_DB")));
        return ds.getConnection();
    
    }
    
}
