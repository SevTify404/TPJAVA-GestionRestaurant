/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;
import entity.enums.ActionType;
import java.time.Instant;

/**
 *
 * @author sevtify
 */

public class Audit {
    private int idAudit, idUser;
    private ActionType action;
    private String message;
    private Instant tempsAction;

    public Audit(int idAudit, int idUser, ActionType action, String message, Instant tempsAction) {
        this.idAudit = idAudit;
        this.idUser = idUser;
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

    public int getIdUser() {
        return idUser;
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

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTempsAction(Instant tempsAction) {
        this.tempsAction = tempsAction;
    }
    
    
    
    
}
