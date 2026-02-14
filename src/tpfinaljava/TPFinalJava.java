/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpfinaljava;

import com.formdev.flatlaf.FlatLightLaf;
import formulaires.LoginFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author sevtify
 */
public class TPFinalJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Activation du thème moderne
            UIManager.setLookAndFeel(new FlatLightLaf());
            System.out.println("Installation réussie");
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Échec de l'initialisation de FlatLaf");
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
