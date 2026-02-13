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

    @Override 
    public CrudResult<Boolean> enregistrer(Users unUser) {
        int inter = 0;
        String requete = "INSERT INTO users idUser,login, motDePasse, isAdmin VALUES (?,?,?,?) ";
        
        PreparedStatement ps = null;
        
        if (!estValide(unUser).getDonnes()){
            CrudResult.failure("Login déjà pris");
        } 
        try {
            Connection conn =  toConnect();
            ps = conn.prepareStatement(requete);
            
            ps.setInt(1, unUser.getIdUser());
            ps.setString(2, unUser.getLogin());
            ps.setString(3, unUser.getMotDePasse());
            ps.setBoolean(4, unUser.getIsAdmin());
            
            inter = ps.executeUpdate();
            
            ps.close();
            conn.close();
            
        } catch (SQLException ex) {
            return CrudResult.failure("Une Erreur Bd est survenue : "+ ex.getMessage());
        }
        
        if (inter == 0) {
            return CrudResult.failure("Une erreur est survenue");
        }
        
        return CrudResult.success(true);
        
        
    }

    @Override
    public CrudResult<Users> lire(int idUser) {
        Users inter = new Users();
        String requete = "SELECT(idUser, login, isAdmin) from users where id = ? and deletedAt is NULL";
        PreparedStatement ps = null;
        
        try {
            Connection conn = toConnect();
            ps = conn.prepareStatement(requete);
            
           ps.setInt(1, idUser);
           
           ResultSet rs = null;
           rs = ps.executeQuery();
           if(rs.next()){
               inter.setIdUser(rs.getInt(1));
               inter.setLogin(rs.getString(2));
               inter.setIsAdmin(rs.getBoolean(4));
           }
           rs.close();
           ps.close();
           conn.close();
        } catch (SQLException ex) {
           return CrudResult.failure("Une Erreur Bd est survenue : "+ ex.getMessage());
        }
        if (inter == null) {
            return CrudResult.failure("Une erreur est survenue");
        }
        
        return CrudResult.success(inter);
        
        
        
        
        
    }

    @Override
    public CrudResult<Users> mettreAJour(Users unUser) {
        int inter = 0;
        String requete= "UPDATE users set login =?, motDePasse =?, isAdmin =? where idUsers = ?";
        
        PreparedStatement ps = null;
        
        
        try {
            Connection conn = toConnect();
        
            ps = conn.prepareStatement(requete);
            
            ps.setInt(1, unUser.getIdUser());
            ps.setString(2, unUser.getLogin());
            ps.setString(3, unUser.getMotDePasse());
            ps.setBoolean(4, unUser.getIsAdmin());
            
            inter = ps.executeUpdate();
            
        } catch (SQLException ex) {
            return CrudResult.failure("Une Erreur Bd est survenue : "+ ex.getMessage());
        }
        if (inter == 0) {
            return CrudResult.failure("Une erreur est survenue");
        }
        
        return CrudResult.success(unUser);
        
        
        
    }

    @Override
    public CrudResult<Boolean> suppressionDefinitive(Users unUser) {
        int inter = 0;
        String requete ="DELETE from users where idUser=?";
        
        PreparedStatement ps = null;
        if(unUser.getIsAdmin()== false){
            CrudResult.failure("Vous devez être un Administrateur pour effectuer cette suppression");
        }else{
        
            try {
                Connection conn = toConnect();
                ps = conn.prepareStatement(requete);

                ps.setInt(1, unUser.getIdUser());

                inter = ps.executeUpdate();

                ps.close();
                conn.close();
            } catch (SQLException ex) {
                return CrudResult.failure("Erreur de bd"+ ex.getMessage());
            }     
            if(inter == 0){
                return CrudResult.failure("Une erreur est survenue");
            }     
        }
        return CrudResult.success(true);
    }

    @Override
    public CrudResult<Boolean> suppressionLogique(Users entiteASupprimer) {
        String requete = "UPDATE users SET deletedAt = NOW() WHERE idUser = ?";
        Users unUser = new Users();

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

            return CrudResult.failure("Erreur BD : " + ex.getMessage());
        }
    }

    @Override
    public CrudResult<Boolean> estValide(Users unUser) {
        boolean valide = true;
        String requete = "SELECT * from users where login =? AND deletedAt is null ";
        try (
            Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(requete)) {

            
            ps.setString(1, unUser.getLogin());
            
            

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                valide = false;
            }

        } catch (SQLException ex) {
            return CrudResult.failure("Erreur de bd"+ ex.getMessage());
        }

        return CrudResult.success(valide);
        
    }

    @Override
    public CrudResult<List<Users>> recupererTout() {
        List<Users> listeUsers = new ArrayList<>();

        String requete = "SELECT * FROM users" ;
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

            return CrudResult.failure("Erreur BD : " + ex.getMessage());
        }
        return CrudResult.success(listeUsers);
    }
    
    public CrudResult<Users> seConnecter(String login, String motDePasse){
        Users inter = null;
        String requete = "SELECT * from Users where login =? and motDePasse =?";
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
               inter.setIsAdmin(rs.getBoolean(4));
           }
           rs.close();
           ps.close();
           conn.close();
           
        } catch (SQLException ex) {
            return CrudResult.failure("Erreur de bd"+ex.getMessage());
            
        }
        if(inter == null){
            return CrudResult.failure("Login ou mot de passe incorret");
        }
        return CrudResult.success(inter);
        
        
        
    }
    

}

    