/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.security.Timestamp;

/**
 *
 * @author Rose
 */
public class Produit {
    private int idProduit, idCategorie, idUser, stockActuel, seuilAlerte ;
    private double prixDeVente;
    private String nom;
    private Timestamp deletedAt;
    
    public Produit(){};
    public Produit(int idProduit, String nom, int idCategorie,int idUser, double prixDeVente, int stockActuel, int seuilAlerte){
     this.idProduit = idProduit;
     this.idCategorie = idCategorie;
     this.idUser = idUser;
     this.prixDeVente = prixDeVente;
     this.nom = nom;
     this.stockActuel = stockActuel;
     this.seuilAlerte = seuilAlerte;
     
    }
    public int getIdProduit(){
     return idProduit;
    }
    
    public int getIdcategorie(){
     return idCategorie;
    }
    
    

    public int getIdUser(){
     return idUser;
    }
    

    public int getStockActuel(){
     return stockActuel;
    }
    
    
    public int getSeuilAlerte(){
     return seuilAlerte;
    }
    
    
    public String getNom(){
     return nom ;
    }
    
    
    public double getPrixDeVente(){
     return prixDeVente ;
    }
    
    public Timestamp getDeletedAt(){
        return deletedAt;
    }
    
    public void setIdProduit(int idProduit){
        this.idProduit = idProduit;
    }
    public void setNom(String nom){
        this.nom = nom;
    }
    
    public void setIdCategorie(int idCategorie){
        this.idCategorie = idCategorie ;
    }
    
    public void setIdUser(int idUser){
        this.idUser = idUser;
    }
    public void setPrixDeVente(double prixDeVente){
        this.prixDeVente = prixDeVente;
    }
    
    public void setStockActuel(int stockActuel){
        this.stockActuel = stockActuel ;
    }
    
    public void setSeuilAlerte (int seuilAlerte){
        this.seuilAlerte = seuilAlerte ;
    }
    public void setDeletedAt(Timestamp deletedAt){
        this.deletedAt = deletedAt;
    }
    
    
    
    public String Afficher(){
        return "Id du produit :" + idProduit + "\nNom du produit : " + nom + "\nPrix de vente: "+ prixDeVente +"\nStock actuel: " + stockActuel + "\nSeuil d'alerte : " + seuilAlerte + "\ncatégorie : "; //+libelle 
    }


    
    
    
}
