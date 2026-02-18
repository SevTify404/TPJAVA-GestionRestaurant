/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author ACER
 */
public class Categorie {
    private int idCat ;
    private String libelle;
    
    public Categorie(){};
    
    public Categorie (int idCat, String libelle){
        this.idCat = idCat;
        this.libelle = libelle;
    }
    public int getIDCAT(){
        return idCat;
    }
    public void setIDCAT(int idCat){
        this.idCat = idCat;
    }
    public String getLIBELLE(){
        return libelle;
    }
    public void setLIBELLE(String libelle){
        this.libelle = libelle;
    }
    @Override
    public String toString() {
        return "Categorie{" +
                "idCat=" + idCat +
                ", libelle='" + libelle + '\'' +
                '}';
    }
    
    
}
