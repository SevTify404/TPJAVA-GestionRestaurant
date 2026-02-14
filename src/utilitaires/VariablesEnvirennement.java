/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilitaires;

import io.github.cdimascio.dotenv.Dotenv;
import javax.swing.JOptionPane;

/**
 *
 * @author sevtify
 */
public class VariablesEnvirennement {
    
    public static Dotenv variables; 
    
    public static Dotenv recupererVariables(){            
        return variables;        
    }
    
    public static void checkVariablesEnvironnement(){
        try {
            if (variables == null) {
                variables = Dotenv.load();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Allez ajouter le fichier .env à la racine du projet avec vos informations de connexion à la bd svp", "Erreur De Variable d'envirennement", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        
    }
    
    
}
