/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;
import java.time.LocalDateTime;

/**
 *
 * @author loyale
 */
public class Commande {
    private int idCommande;
    private LocalDateTime dateCommande;
    private EtatCommande etat;
    private double total;
    private LocalDateTime deletedAt;
    

    public enum EtatCommande {
        EN_COURS,
        VALIDEE,
        ANNULEE
    }

    public Commande() {
    }

    public Commande(int idCommande, LocalDateTime dateCommande, EtatCommande etat, double total, LocalDateTime deletedAt) {
        this.idCommande = idCommande;
        this.dateCommande = dateCommande;
        this.etat = etat;
        this.total = total;
        this.deletedAt = deletedAt;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public EtatCommande getEtat() {
        return etat;
    }

    public double getTotal() {
        return total;
    }
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }
    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }
    public void setEtat(EtatCommande etat) {
        this.etat = etat;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
