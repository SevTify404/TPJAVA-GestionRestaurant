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
public class Users {
    private int idUser;
    private boolean isAdmin;
    private String login, motDePasse, sexe;
    private Timestamp deletedAt;
    
    public Users(){}
    public Users(int idUser , String login, String motDePasse,boolean isAdmin, String sexe){
        this.idUser = idUser;
        this.login= login;
        this.motDePasse = motDePasse;
        this.isAdmin = isAdmin;
        this.sexe = sexe;
    }
    
    public int getIdUser(){
        return this.idUser;
    }
    
    public String getLogin(){
        return this.login;
    }
    
    public boolean getIsAdmin(){
        return this.isAdmin;
    }
    
    public String getMotDePasse(){
        return this.motDePasse;
    }
    
    public Timestamp getDeletedAt() {
        return deletedAt;
    }
    
    public String getSexe(){
        return this.sexe;
    }


    public void setIdUser(int idUser){
        this.idUser = idUser;
    }
    
    public void setLogin(String login){
        this.login = login;
    }
    
    public void setMotDePasse(String motDePasse){
        this.motDePasse = motDePasse;
    }
    
    public void setIsAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }
    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }
    
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }
    
    public String Afficher(){
        return "Id de l'utilisateur: " + idUser + "\nLogin: " + login + "\nSexe: " + sexe ;
    }
    
    
}
