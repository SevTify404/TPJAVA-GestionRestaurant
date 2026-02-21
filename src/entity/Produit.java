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
    private int idProduit, stockActuel, seuilAlerte ;
    private double prixDeVente;
    private String nom;
    private Timestamp deletedAt;
    private Categorie categorie;
    private Users user;
    
    public Produit(){};
    public Produit(int idProduit, String nom, Categorie categorie,Users user, double prixDeVente, int stockActuel, int seuilAlerte){
     this.idProduit = idProduit;
     this.categorie = categorie;
     this.user = user;
     this.prixDeVente = prixDeVente;
     this.nom = nom;
     this.stockActuel = stockActuel;
     this.seuilAlerte = seuilAlerte;
     
    }
    public int getIdProduit(){
     return idProduit;
    }
    
    public Categorie getCategorie(){
     return categorie;
    }
    
    

    public Users getUser(){
     return user;
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
    
    public void setCategorie(Categorie categorie){
        this.categorie = categorie ;
    }
    
    public void setUser(Users user){
        this.user = user;
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
        return "Id du produit :" + idProduit + "\nNom du produit : " +
                nom + "\nPrix de vente: "+ prixDeVente +"\nStock actuel: " +
                stockActuel + "\nSeuil d'alerte : " + seuilAlerte +
                "\nCatégorie : " + categorie.getLIBELLE()+ " User :" + user.Afficher(); //+libelle 
    }


    
    
    
}
