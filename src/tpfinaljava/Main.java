/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpfinaljava;
import formulaires.App;
import formulaires.Audit_Log;
import formulaires.Utilisateurs;
import javax.swing.JFrame;
import utilitaires.VariablesEnvirennement;



/**
 *
 * @author sevtify
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        VariablesEnvirennement.checkVariablesEnvironnement();
        
        App application = App.getInstance();
        
        application.lancerApplication();
//JFrame frame = new JFrame("Test audits");
//frame.setContentPane(new Audit_Log());
//frame.pack();
//frame.setLocationRelativeTo(null);
//frame.setVisible(true);
    }
}
    
