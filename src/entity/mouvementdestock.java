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

public class mouvementdestock {

    public enum TypeMouvement {
        ENTREE,
        SORTIE
    }

    private int id;
    private TypeMouvement TYPE;
    private int IDPRODUIT;
    private int QUANTITE;
    private LocalDateTime DATEMOUVEMENT;
    private String MOTIF;
    private LocalDateTime deletedAt;

    // Constructeur vide
    public mouvementdestock() {
    }

    // Constructeur complet
    public mouvementdestock(int id, TypeMouvement TYPE, int IDPRODUIT, int QUANTITE, LocalDateTime DATEMOUVEMENT, String MOTIF, LocalDateTime deletedAt) {
        this.id = id;
        this.TYPE = TYPE;
        this.IDPRODUIT = IDPRODUIT;
        this.QUANTITE = QUANTITE;
        this.DATEMOUVEMENT = DATEMOUVEMENT;
        this.MOTIF = MOTIF;
        this.deletedAt = deletedAt;
    }

    // Getters et Setters
    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public TypeMouvement getTYPE() {
        return TYPE;
    }

    public void setTYPE(TypeMouvement TYPE) {
        this.TYPE = TYPE;
    }

    public int getIDPRODUIT() {
        return IDPRODUIT;
    }

    public void setIDPRODUIT(int IDPRODUIT) {
        this.IDPRODUIT = IDPRODUIT;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "mouvementdestock{" +
                "id=" + id +
                ", TYPE=" + TYPE +
                ", IDPRODUIT=" + IDPRODUIT +
                ", QUANTITE=" + QUANTITE +
                ", DATEMOUVEMENT=" + DATEMOUVEMENT +
                ", MOTIF='" + MOTIF + '\'' +
                ", deletedAt=" + deletedAt +
                '}';
    }
}

