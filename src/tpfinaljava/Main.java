/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpfinaljava;
//import formulaires.App;
import com.formdev.flatlaf.FlatLightLaf;
import dao.UsersDAO;
import formulaires.App;
import formulaires.CommandesPanel;
import formulaires.DashBoardPanel;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import utilitaires.AuthentificationManager;
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

        
         // Force la gestion du DPI pour éviter l'effet "agrandi" sur Windows
                System.setProperty("flatlaf.useWindowDecorations", "true");
                System.setProperty("flatlaf.uiScale.enabled", "true");

                // Optionnel : force une échelle spécifique si tu veux que ce soit identique partout
                System.setProperty("flatlaf.uiScale", "1.0"); 

                com.formdev.flatlaf.FlatLightLaf.setup();
                UIManager.setLookAndFeel(new FlatLightLaf());
                System.out.println("Installation de flatlad réussie");
//        
//                App appl = App.getInstance();
//                appl.lancerApplication();
       java.awt.EventQueue.invokeLater(() -> {
           JFrame frame = new JFrame("Test Dashboard");
           
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        frame.add(new CommandesPanel(), BorderLayout.CENTER);

        frame.setVisible(true);
    });
    }
}
    
