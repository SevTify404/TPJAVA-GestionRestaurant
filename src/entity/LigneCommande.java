/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.time.LocalDateTime;

public class LigneCommande {

    private int idLC;
    private int quantite;
    private double prixUnitaire;
    private double montantLigne;
    private LocalDateTime deletedAt;

    private Commande commande;
    private Produit produit;

    public LigneCommande() {
    }

    public LigneCommande(int idLC, int quantite, double prixUnitaire,
        Commande commande, Produit produit,
        LocalDateTime deletedAt) {

        this.idLC = idLC;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.commande = commande;
        this.produit = produit;
        this.deletedAt = deletedAt;

        recalculerMontant();
    }

    // GETTERS

    public int getIdLC() {
        return idLC;
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

    public Commande getCommande() {
        return commande;
    }

    public Produit getProduit() {
        return produit;
    }
    
    public int getIdCommande() {
        return (commande != null) ? commande.getIdCommande() : 0;
    }

    public int getIdProduit() {
        return (produit != null) ? produit.getIdProduit() : 0;
    }

    public void setIdLC(int idLC) {
        this.idLC = idLC;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
        recalculerMontant();
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        recalculerMontant();
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
        recalculerMontant();
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void recalculerMontant() {

        double prix = this.prixUnitaire;
        
        if (prix <= 0 && produit != null) {
            prix = produit.getPrixDeVente();
            this.prixUnitaire = prix;
        }
        if (quantite <= 0 || prix <= 0) {
            this.montantLigne = 0;
            return;
        }
        this.montantLigne = quantite * prix;
    }
    public void setMontantLigne(double montantLigne) {
        this.montantLigne = montantLigne;
        recalculerMontant();
        
    }
}