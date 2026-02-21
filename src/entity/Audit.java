/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;
import entity.enums.ActionType;
import java.time.Instant;
import java.util.Date;

/**
 *
 * @author sevtify
 */

public class Audit {
    private int idAudit;
    private Users utilisateurRelatifALAudit;
    private ActionType action;
    private String message;
    private Instant tempsAction;

    public Audit(int idAudit, Users utilisateur, ActionType action, String message, Instant tempsAction) {
        this.idAudit = idAudit;
        this.utilisateurRelatifALAudit = utilisateur;
        this.action = action;
        this.message = message;
        this.tempsAction = tempsAction;
    }

    
    public ActionType getAction() {
        return action;
    }

    public int getIdAudit() {
        return idAudit;
    }

    public Users getUser() {
        return utilisateurRelatifALAudit;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTempsAction() {
        return tempsAction;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public void setIdAudit(int idAudit) {
        this.idAudit = idAudit;
    }

    public void setUser(Users utilisaeur) {
        this.utilisateurRelatifALAudit = utilisaeur;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTempsAction(Instant tempsAction) {
        this.tempsAction = tempsAction;
    }
    
    public String afficher(){

        String aRetourner = "Id : " + idAudit;
        aRetourner += "\nUtilisateur : " + utilisateurRelatifALAudit.getLogin();
        aRetourner += "\nMessage : " + message;
        aRetourner += "\nAction : " + action;
        aRetourner += "\nTemps de l'action : " + Date.from(tempsAction);
        
        return aRetourner;
    
    }
    
    
    
}
