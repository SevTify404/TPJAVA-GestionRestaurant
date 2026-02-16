/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilitaires;

/**
 *
 * @author sevtify
 */
public class AuthentificationManager {
    
    // Classe qui va gerer toute l'authentification
    // J'utilise le pattern Singleton pour éviter les bugs bizzar
    
    private static AuthentificationManager instanceUnique = null;
    private String personneActuellementConnecte = null;
    
    private AuthentificationManager(){}
    
    public static AuthentificationManager getInstance(){
        if (instanceUnique == null) instanceUnique = new AuthentificationManager();
        return instanceUnique;
    }
    
    public void connecterUnUtilisateur(String user){
        this.personneActuellementConnecte = user;
    }
    
    public void deconnecterUtilisateurActuel(){
        this.personneActuellementConnecte = null;
    }
    

    public String recupererUtilisateurConnecte(){
        return personneActuellementConnecte;
    }
    
    
    
    
    
    
}
