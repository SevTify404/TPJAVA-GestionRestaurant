/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpfinaljava;
//import formulaires.App;
import com.formdev.flatlaf.FlatLightLaf;
import dao.UsersDAO;
import formulaires.App;

import utilitaires.VariablesEnvirennement;
//import utilitaires.VariablesEnvirennement;



/**
 *
 * @author sevtify
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
                VariablesEnvirennement.checkVariablesEnvironnement();
                AuthentificationManager.getInstance().connecterUnUtilisateur(
                    UsersDAO.getInstance().lire(4).getDonnes()
                );

        
        application.lancerApplication();
//JFrame frame = new JFrame("Test audits");
//frame.setContentPane(new Audit_Log());
//frame.pack();
//frame.setLocationRelativeTo(null);
//frame.setVisible(true);

    }
}
    
