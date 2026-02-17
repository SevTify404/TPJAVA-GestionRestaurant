/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpfinaljava;

import com.mysql.cj.conf.PropertyKey;
import entity.Produit;


import dao.CrudResult;
import dao.UsersDAO;
import entity.Audit;
import entity.Users;
import java.util.List;
import entity.Produit;
import dao.ProduitDAO;

/**
 *
 * @author sevtify
 */
public class TPFinalJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*Users unUser = new Users(3,"Gra", "talokpo", true,"F");
        CrudResult<Boolean> ee = UsersDAO.getInstance().enregistrer(unUser);
       
        if (ee.estUneErreur()) {
            System.out.println("Erreur : "+ ee.getErreur());
        } else{
            System.out.println("Reusii");
        }*/
        //CrudResult<Users> ee= UsersDAO.getInstance().lire(2);
        
        //Mise à jour d'un user
        /*Users unUser = new Users();
        unUser.setIdUser(4);
        unUser.setLogin("queen");
        unUser.setMotDePasse("1234");
        unUser.setIsAdmin(true);
        unUser.setSexe("F");
        CrudResult<Users> result = UsersDAO.getInstance().mettreAJour(unUser);*/

       
       //Suppression logique d'un User
        /*Users unUser = new Users();
        unUser.setIdUser(5);

        CrudResult<Boolean> result = UsersDAO.getInstance().suppressionLogique(unUser);*/
       
        //Suppression définitive d'un user
        /*Users admin = new Users();
        admin.setIdUser(7);
        admin.setIsAdmin(true); 
        CrudResult<Boolean> result =
        UsersDAO.getInstance().suppressionDefinitive(admin);*/
        
        
        //Récupérer tous les utilisateurs
        /*CrudResult<List<Users>> result = UsersDAO.getInstance().recupererTout();
        if(result.estUneErreur()) {
            System.out.println("Erreur : " + result.getErreur());
        } else {
            List<Users> liste = result.getDonnes();
            System.out.println("Liste des utilisateurs :");
            for(Users unUser : liste){
                System.out.println(unUser.Afficher());
            }
        }*/
        
        
        //Se connecter
        /*String login = "queen"; 
        String motDePasse = "1234";

        CrudResult<Users> result = UsersDAO.getInstance().seConnecter(login, motDePasse);

        if(result.estUneErreur()) {
            System.out.println("Erreur : " + result.getErreur());
        } else {
            Users user = result.getDonnes();
            System.out.println("Connexion réussie !");
            System.out.println("Id de l'utilisateur : " + user.getIdUser());
            System.out.println("Login : " + user.getLogin());
            System.out.println("Admin : " + user.getIsAdmin());
            System.out.println("Sexe: " + user.getSexe());
        }*/


        

        /*Enrégistrer un produit
        Produit unProduit = new Produit(3,"Jus d'ananas",1,2,16000,165,10);
        CrudResult<Boolean> ee = ProduitDAO.getInstance().enregistrer(unProduit);
       
        if (ee.estUneErreur()) {
            System.out.println("Erreur : "+ ee.getErreur());
        } else{
            System.out.println("Reusii");
        }*/
        /*Lecture d'un produit
        CrudResult<Produit> ee= ProduitDAO.getInstance().lire(2);
        if (ee.estUneErreur()) {
            System.out.println("Erreur : "+ ee.getErreur());
        } else{
            Produit unProduit = ee.getDonnes();
            System.out.println(unProduit.Afficher());
        }*/
        
        //Mise à jour d'un produit
        /*Produit unProduit = new Produit();
        unProduit.setIdProduit(2);
        unProduit.setNom("pompompon");
        unProduit.setIdCategorie(1);
        unProduit.setIdUser(1);
        unProduit.setPrixDeVente(15000);
        unProduit.setStockActuel(19);
        unProduit.setSeuilAlerte(5);

        CrudResult<Produit> result = ProduitDAO.getInstance().mettreAJour(unProduit);
        if (result.estUneErreur()) {
            System.out.println("Erreur : "+ result.getErreur());
        } else{
            
             System.out.println(result.getDonnes().Afficher());
        }*/
        
        //Suppression logique d'un produit
       /*Produit unProduit = new Produit();
       unProduit.setIdProduit(1);

       CrudResult<Boolean> result = ProduitDAO.getInstance().suppressionLogique(unProduit);*/
       
       /*Suppression définitive d'un produit
        Produit produit = new Produit();
        produit.setIdProduit(2); 
        CrudResult<Boolean> result = ProduitDAO.getInstance().suppressionDefinitive(produit);

        if(result.estUneErreur()) {
            System.out.println("Erreur : " + result.getErreur());
        } else {
            System.out.println("Suppression définitive réussie !");
        }*/
     
        
        /*Récupérer tous les produits
        CrudResult<List<Produit>> result = ProduitDAO.getInstance().recupererTout();
        if(result.estUneErreur()) {
            System.out.println("Erreur : " + result.getErreur());
        } else {
            List<Produit> liste = result.getDonnes();
            System.out.println("Liste des produits :");

            for(Produit unProduit : liste){
                System.out.println(unProduit.Afficher());
            }
        }*/
        
    }
    
    
    
}
