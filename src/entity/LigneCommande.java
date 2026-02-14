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
public class LigneCommande {

    private int idLC;
    private int idCommande;
    private int idProduit;
    private int quantite;
    private double prixUnitaire;
    private double montantLigne;
    private LocalDateTime deletedAt;

    public LigneCommande() {
    }
    
    public LigneCommande(int idCommande, int idProduit, int quantite, double prixUnitaire) {
        this.idCommande = idCommande;
        this.idProduit = idProduit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.montantLigne = quantite * prixUnitaire;
    }

    public int getIdLC() {
        return idLC;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public int getQuantite() {
        return quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public double getMontantLigne() {
        return montantLigne;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setIdLC(int idLC) {
        this.idLC = idLC;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
        recalculerMontant();
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        recalculerMontant();
    }

    public void setMontantLigne(double montantLigne) {
        this.montantLigne = montantLigne;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    private void recalculerMontant() {
        if (quantite > 0 && prixUnitaire > 0) {
            this.montantLigne = this.quantite * this.prixUnitaire;
        }
    }
    
}
