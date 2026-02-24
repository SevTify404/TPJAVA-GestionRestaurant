/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilitaires;

import dao.AuditDAO;
import dao.CrudResult;
import entity.Audit;
import entity.Users;
import entity.enums.ActionType;
import java.time.Instant;
import javax.swing.JOptionPane;

/**
 *
 * @author sevtify
 */
public class AuthentificationManager {
    
    // Classe qui va gerer toute l'authentification
    // J'utilise le pattern Singleton pour éviter les bugs bizzar
    
    private static AuthentificationManager instanceUnique = null;
    private Users personneActuellementConnecte = null;
    
    private AuthentificationManager(){}
    
    public static AuthentificationManager getInstance(){
        if (instanceUnique == null) instanceUnique = new AuthentificationManager();
        return instanceUnique;
    }
    
    public void connecterUnUtilisateur(Users user){
        Audit audit = new Audit(
            0,
            user,
            ActionType.MODIFICATION,
            user.getLogin() + " s'est connecté",
            Instant.now()
        );
        
        CrudResult<Boolean> resultat = AuditDAO.getInstance().enregistrer(audit);
        
        if (resultat.estUnSucces()) {
            System.out.println("Enregistrement audit reussssssssi ");
        }else{
            JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement en audit : " + resultat.getErreur(), "Erreur Audit", JOptionPane.ERROR_MESSAGE);
        }
        this.personneActuellementConnecte = user;
    }
    
    public void deconnecterUtilisateurActuel(){
        Audit audit = new Audit(
            0,
            personneActuellementConnecte,
            ActionType.MODIFICATION,
            personneActuellementConnecte.getLogin() + " s'est déconnecté",
            Instant.now()
        );
        
        CrudResult<Boolean> resultat = AuditDAO.getInstance().enregistrer(audit);
        
        if (resultat.estUnSucces()) {
            System.out.println("Enregistrement audit reussssssssi ");
        }else{
            JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement en audit : " + resultat.getErreur(), "Erreur Audit", JOptionPane.ERROR_MESSAGE);
        }
        this.personneActuellementConnecte = null;
    }
    

    public Users recupererUtilisateurConnecte(){
        return personneActuellementConnecte;
    }
    
    public void enregistrerActionDansAudit(ActionType actionEffectuer, String MessageLisible){
        Audit audit = new Audit(0, personneActuellementConnecte, actionEffectuer, MessageLisible, Instant.now());
        
        CrudResult<Boolean> resultat = AuditDAO.getInstance().enregistrer(audit);
        
        if (resultat.estUnSucces()) {
            System.out.println("Enregistrement audit reussssssssi ");
        }else{
            JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement en audit : " + resultat.getErreur(), "Erreur Audit", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void enregistrerActionDansAuditSansUtilisateur(ActionType actionEffectuer, String MessageLisible){
        Audit audit = new Audit(0, null, actionEffectuer, MessageLisible, Instant.now());
        
        CrudResult<Boolean> resultat = AuditDAO.getInstance().enregistrerAuditSansUtilisateur(audit);
        
        if (resultat.estUnSucces()) {
            System.out.println("Enregistrement audit reussssssssi ");
        }else{
            JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement en audit : " + resultat.getErreur(), "Erreur Audit", JOptionPane.ERROR_MESSAGE);
        }
    } 
    
    
    
    
    
    
}
