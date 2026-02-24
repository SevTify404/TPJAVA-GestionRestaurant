/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package formulaires;

import com.formdev.flatlaf.FlatLightLaf;
import entity.Users;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import utilitaires.AuthentificationManager;

/**
 *
 * @author sevtify
 */
public class App {
    
    private LoginFrame ecranDeLogin = null;
    
    private MenuPrincipalFrame menuPrincipal = null;
    
    private static App instanceUnique = null;
    
    Boolean applicationDejaEnCours = false;

    private App() {
        
    }
    
    public static App getInstance(){
        if (instanceUnique == null) {
            instanceUnique = new App();
        }
        
        return instanceUnique;
    }
    
    public void lancerApplication(){
        
        if (!applicationDejaEnCours) {
            applicationDejaEnCours = true;
            
            // ici j'active un thème moderne
            try {
                // Force la gestion du DPI pour éviter l'effet "agrandi" sur Windows
                System.setProperty("flatlaf.useWindowDecorations", "true");
                System.setProperty("flatlaf.uiScale.enabled", "true");

                // Optionnel : force une échelle spécifique si tu veux que ce soit identique partout
                System.setProperty("flatlaf.uiScale", "1.0"); 

                com.formdev.flatlaf.FlatLightLaf.setup();
                UIManager.setLookAndFeel(new FlatLightLaf());
                System.out.println("Installation de flatlad réussie");
            } catch (UnsupportedLookAndFeelException ex) {
                System.err.println("Échec de l'installation de FlatLaf");
            } 
        }
        
        
        ecranDeLogin = new LoginFrame();
        
        ecranDeLogin.setVisible(true);
    }
    
    public void lancerMenuPrincipal(Users utilisateur){
        
        ecranDeLogin.dispose();
        ecranDeLogin = null;
        
        AuthentificationManager.getInstance().connecterUnUtilisateur(utilisateur);
        
        menuPrincipal = new MenuPrincipalFrame();
        System.out.println("ok");
        
        menuPrincipal.setVisible(true);   
    }
    
    public void fermerApp(){
        menuPrincipal.dispose();
        System.exit(0);
    }
    
    public void fermerSessionUtilisateur(){
        
        AuthentificationManager.getInstance().deconnecterUtilisateurActuel();
        
        menuPrincipal.dispose();
        
        menuPrincipal = null;
        
        lancerApplication();
    }
    
    
    
}
