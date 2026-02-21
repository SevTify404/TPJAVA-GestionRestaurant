/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Users;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.util.ArrayList;



/**
 *
 * @author Rose
 */
public class UsersDAO extends AbstractDAO<Users> {
    
    public static UsersDAO getInstance(){
        return new UsersDAO();
    }

    @Override 
    public CrudResult<Boolean> enregistrer(Users unUser) {
        int inter = 0;
        CrudResult<Boolean> validation = estValide(unUser);
        String requete = "INSERT INTO Users(login, motDePasse, isAdmin, sexe) VALUES (?,?,?,?) ";
        
        PreparedStatement ps = null;
        
        if(validation.estUneErreur()) {
            return CrudResult.failure(validation.getErreur()); // erreur BD
        }
        if (!validation.getDonnes()){
            return CrudResult.failure("Login déjà pris");
        }else{
            try {
                Connection conn =  toConnect();
                ps = conn.prepareStatement(requete);


                ps.setString(1, unUser.getLogin());
                ps.setString(2, unUser.getMotDePasse());
                ps.setBoolean(3, unUser.getIsAdmin());
                ps.setString(4, unUser.getSexe());
                inter = ps.executeUpdate();

                ps.close();
                conn.close();

            } catch (SQLException ex) {
                return gererExceptionSQL(ex);
            }

            if (inter == 0) {
                return CrudResult.failure("Une erreur est survenue");
            }

            return CrudResult.success(true);

        }    
    }

    @Override
    public CrudResult<Users> lire(int idUser) {
        Users inter = null;
        String requete = "SELECT idUser, login, isAdmin, sexe from Users where idUser = ? and deletedAt is NULL";
        PreparedStatement ps = null;
        
        try {
            Connection conn = toConnect();
            ps = conn.prepareStatement(requete);
            
           ps.setInt(1, idUser);
           
           ResultSet rs = null;
           rs = ps.executeQuery();
           if(rs.next()){
               inter = new Users();
               inter.setIdUser(rs.getInt(1));
               inter.setLogin(rs.getString(2));
               inter.setIsAdmin(rs.getBoolean(3));
               inter.setSexe(rs.getString(4));
           }
           rs.close();
           ps.close();
           conn.close();
        } catch (SQLException ex) {
           return gererExceptionSQL(ex);
        }
        if (inter == null) {
            return CrudResult.failure("Cet Utilisateur n'existe pas");
        }
        
        return CrudResult.success(inter);
        
        
        
        
        
    }

    @Override
    public CrudResult<Users> mettreAJour(Users unUser) {
        int inter = 0;
        String requete= "UPDATE Users set login =?, motDePasse =?, isAdmin =?, sexe =? where idUser = ?";
        
        PreparedStatement ps = null;
        
        
        try {
            Connection conn = toConnect();
        
            ps = conn.prepareStatement(requete);
            
            
            ps.setString(1, unUser.getLogin());
            ps.setString(2, unUser.getMotDePasse());
            ps.setBoolean(3, unUser.getIsAdmin());
            ps.setString(4, unUser.getSexe());
            ps.setInt(5, unUser.getIdUser());
            
            inter = ps.executeUpdate();
            
        } catch (SQLException ex) {
            return gererExceptionSQL(ex);
        }
        if (inter == 0) {
            return CrudResult.failure("Une erreur est survenue");
        }
        
        return CrudResult.success(unUser);
        
        
        
    }

    @Override
    public CrudResult<Boolean> suppressionDefinitive(Users unUser) {
        int inter = 0;
        String requete ="DELETE from Users where idUser=?";
        
        PreparedStatement ps = null;
        if(unUser.getIsAdmin()== false){
            return CrudResult.failure("Vous devez être un Administrateur pour effectuer cette suppression");
        }else{
        
            try {
                Connection conn = toConnect();
                ps = conn.prepareStatement(requete);

                ps.setInt(1, unUser.getIdUser());

                inter = ps.executeUpdate();

                ps.close();
                conn.close();
            } catch (SQLException ex) {
                return gererExceptionSQL(ex);
            }     
            if(inter == 0){
                return CrudResult.failure("Une erreur est survenue");
            }     
        }
        return CrudResult.success(true);
    }

    @Override
    public CrudResult<Boolean> suppressionLogique(Users unUser) {
        String requete = "UPDATE Users SET deletedAt = NOW() WHERE idUser = ?";
        

        try {

            Connection conn = toConnect();
            PreparedStatement ps = conn.prepareStatement(requete);

            ps.setInt(1, unUser.getIdUser());

            int lignes = ps.executeUpdate();

            if (lignes > 0) {
                return CrudResult.success(true);
            }

            return CrudResult.failure("Utilisateur non trouvé");

        } catch (SQLException ex) {

            return gererExceptionSQL(ex);
        }
    }

    @Override
    public CrudResult<Boolean> estValide(Users unUser) {
        boolean valide = true;
        String sexe = unUser.getSexe();
        String requete = "SELECT * from Users where login =? AND deletedAt is null ";
        try (
            Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(requete)) {

            
            ps.setString(1, unUser.getLogin());

            ResultSet rs = ps.executeQuery();
            if(!"F".equals(sexe) && !"M".equals(sexe) ){
                return CrudResult.failure("Vous devez entrer M pour masculin et F pour Féminin");
            }

            if (rs.next()) {
                valide = false;
            }

        } catch (SQLException ex) {
            return gererExceptionSQL(ex);
        }
        
        if (!valide) {
            return CrudResult.failure("Utilisateur existe déja");
        }

        return CrudResult.success(valide);
        
    }

    @Override
    public CrudResult<List<Users>> recupererTout() {
        List<Users> listeUsers = new ArrayList<>();

        String requete = "SELECT * FROM Users where deletedAt is null" ;
        PreparedStatement ps = null;

        try {
            Connection conn = this.toConnect();
            ps= conn.prepareStatement(requete);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {

                Users Unuser = new Users();

                Unuser.setIdUser(rs.getInt("idUser"));
                Unuser.setLogin(rs.getString("login"));

                Unuser.setMotDePasse(rs.getString("motDePasse"));
                Unuser.setIsAdmin(rs.getBoolean("isAdmin"));

                listeUsers.add(Unuser);
            }


        } catch (SQLException ex) {

            return gererExceptionSQL(ex);
        }
        return CrudResult.success(listeUsers);
    }
    
    public CrudResult<Users> seConnecter(String login, String motDePasse){
        Users inter = null;
        String requete = "SELECT idUser, login, sexe, isAdmin  from Users where login =? and motDePasse =? and deletedAt is null";
        PreparedStatement ps = null;
       
        try {
             Connection conn = this.toConnect();
            ps = conn.prepareStatement(requete);
            ps.setString(1, login);
            ps.setString(2, motDePasse);
            
            ResultSet rs = null;
           rs = ps.executeQuery();
           if(rs.next()){
               inter = new Users();
               inter.setIdUser(rs.getInt(1));
               inter.setLogin(rs.getString(2));
               inter.setSexe(rs.getString(3));
               inter.setIsAdmin(rs.getBoolean(4));
           }
           rs.close();
           ps.close();
           conn.close();
           
        } catch (SQLException ex) {
            return gererExceptionSQL(ex);
            
        }
        if(inter == null){
            return CrudResult.failure("Login ou mot de passe incorret");
        }
        return CrudResult.success(inter);
        
        
        
    }
    

}

    