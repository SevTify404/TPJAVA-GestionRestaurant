/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;
//import java.util.Date;
import java.time.LocalDateTime;
/**
 *
 * @author ACER
 */

public class MouvementDeStock {

    public enum TypeMouvement {
        ENTREE,
        SORTIE
    }

    private int ID;
    private TypeMouvement TYPE;
    private int QUANTITE;
    private LocalDateTime DATEMOUVEMENT;
    private String MOTIF;
    private LocalDateTime deletedAt;
    
     

    // 🔥 Référence objet
    private Produit produit;
    
    public MouvementDeStock(){}
    
    // Constructeur complet
    public MouvementDeStock(int ID, TypeMouvement TYPE, Produit produit , int QUANTITE, LocalDateTime DATEMOUVEMENT, String MOTIF, LocalDateTime deletedAt) {
        this.ID = ID;
        this.TYPE = TYPE;
        this.produit = produit;
        this.QUANTITE = QUANTITE;
        this.DATEMOUVEMENT = DATEMOUVEMENT;
        this.MOTIF = MOTIF;
        this.deletedAt = deletedAt;
    }
    // ===== GETTERS & SETTERS =====

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public TypeMouvement getTYPE() {
        return TYPE;
    }

    public void setTYPE(TypeMouvement TYPE) {
        this.TYPE = TYPE;
    }

    public int getQUANTITE() {
        return QUANTITE;
    }

    public void setQUANTITE(int QUANTITE) {
        this.QUANTITE = QUANTITE;
    }

    public LocalDateTime getDATEMOUVEMENT() {
        return DATEMOUVEMENT;
    }

    public void setDATEMOUVEMENT(LocalDateTime DATEMOUVEMENT) {
        this.DATEMOUVEMENT = DATEMOUVEMENT;
    }

    public String getMOTIF() {
        return MOTIF;
    }

    public void setMOTIF(String MOTIF) {
        this.MOTIF = MOTIF;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }
     public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }


    @Override
    public String toString() {
        return "mouvementdestock{" +
                "id=" + ID +
                ", TYPE=" + TYPE +
                ", PRODUIT=" + (produit != null ? produit.getNom() : "null")  +
                ", QUANTITE=" + QUANTITE +
                ", DATEMOUVEMENT=" + DATEMOUVEMENT +
                ", MOTIF='" + MOTIF + '\'' +
                ", deletedAt=" + deletedAt +
                '}';
    }
}

