/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpfinaljava;

import com.formdev.flatlaf.FlatLightLaf;
import formulaires.MenuPrincipalFrame;
import formulaires.LoginFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import utilitaires.VariablesEnvirennement;

/**
 *
 * @author sevtify
 */
public class TPFinalJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Je verifie si le fiechier .env existe
        VariablesEnvirennement.checkVariablesEnvironnement();
        
        // ici j'active un thème moderne
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            System.out.println("Installation de flatlad réussie");
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Échec de l'installation de FlatLaf");
        }
        
        // Lancement de l'app
        java.awt.EventQueue.invokeLater(() -> {
            new MenuPrincipalFrame().setVisible(true);
        });
    }
}
