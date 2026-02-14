/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilitaires;

import formulaires.LoginFrame;
import formulaires.MenuPrincipalFrame;

/**
 *
 * @author sevtify
 */
public class AuthentificationManager {
    
    // Classe qui va gerer toute l'authentification
    // J'utilise le pattern Singleton pour éviter les bugs bizzar
    
    private static AuthentificationManager instanceUnique = null;
    
    private String personneActuellementConnecte = null;
    
    private LoginFrame formulaireDeConnexion = null;
    
    private MenuPrincipalFrame notreMenuPrincipal = null;
    
    private AuthentificationManager(){}
    
    public static AuthentificationManager getInstance(){
        if (instanceUnique == null) instanceUnique = new AuthentificationManager();
        return instanceUnique;
    }
    
    public void lancerApp(){
        if (formulaireDeConnexion == null) formulaireDeConnexion = new LoginFrame();
        
        formulaireDeConnexion.setVisible(true);
    }
    
    public void connecterUnUtilisateur(String utilisateur){
        
        formulaireDeConnexion.dispose();
        formulaireDeConnexion = null;
        
        personneActuellementConnecte = utilisateur;
        notreMenuPrincipal = new MenuPrincipalFrame();
        System.out.println("Utilisatier " + utilisateur + " est conn");
        
        notreMenuPrincipal.setVisible(true);
    }
    
    public void deconnecterUtilisateurActuel(){
        
        
        notreMenuPrincipal.dispose();
        notreMenuPrincipal = null;
        
        System.out.println("Utilisaateur" + personneActuellementConnecte + " déconnecté");
        personneActuellementConnecte = null;
        
        lancerApp();
    }
    
    public String recupererUtilisateurConnecte(){
        return personneActuellementConnecte;
    }
    
    
    
    
    
    
}
