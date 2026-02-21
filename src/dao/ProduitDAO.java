/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.Produit;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import entity.Categorie;
import entity.Users;
import utilitaires.VariablesEnvirennement;




/**
 *
 * @author Rose
 */
public class ProduitDAO extends AbstractDAO<Produit> {
    
    public static ProduitDAO getInstance(){
        return new ProduitDAO();
    }

    @Override
    public CrudResult<Boolean> enregistrer(Produit unProduit) {
        CrudResult<Boolean> validation = estValide(unProduit);
        String requete = "INSERT INTO Produit(nom, idCategorie, idUser, prixDeVente, stockActuel, seuilAlerte) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = null;
        int inter = 0;
        if (validation.estUneErreur()){
            return validation;
        
        }else{
        
            try{
                Connection conn = toConnect();
                ps = conn.prepareStatement(requete);

                ps.setString(1, unProduit.getNom());
                ps.setInt(2, unProduit.getCategorie().getIDCAT());
                ps.setInt(3, unProduit.getUser().getIdUser());
                ps.setDouble(4, unProduit.getPrixDeVente());
                ps.setInt(5, unProduit.getStockActuel());
                ps.setInt(6, unProduit.getSeuilAlerte());

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
    public CrudResult<Produit> lire(int idProduit) {
        Produit inter = new Produit();
        String requete = "SELECT p.* , c.libelle ,u.idUser, u.login " +
                        "from produit p " +
                        "JOIN categorie c on c.idCat = p.idCategorie " +
                        "LEFT JOIN users u on p.idUser = u.idUser AND u.deletedAt IS NULL " +
                        "where idProduit = ? and p.deletedAt is null ";
        PreparedStatement ps = null;
        
        try{
            Connection conn = toConnect();
            ps = conn.prepareStatement(requete);

            ps.setInt(1, idProduit);
            
            ResultSet rs = null;
            rs = ps.executeQuery();
            
            if(rs.next()){
                Categorie categorie = new Categorie(rs.getInt(3), rs.getString(9));
                Users user = new Users();
                user.setIdUser(rs.getInt(10));
                user.setLogin(rs.getString(11));
                
                inter.setIdProduit(rs.getInt(1));
                inter.setNom(rs.getString(2));
                
                inter.setUser(user);
                inter.setCategorie(categorie);
                inter.setPrixDeVente(rs.getDouble(5));
                inter.setStockActuel(rs.getInt(6));
                inter.setSeuilAlerte(rs.getInt(7));
                
                rs.close();
                ps.close();
                conn.close();
                
            }
            
            
        }catch(SQLException ex) {
            return gererExceptionSQL(ex);

        }
        if (inter == null) {
            return CrudResult.failure("Une erreur est survenue");
        }
        
        return CrudResult.success(inter);
        
        
    }

    @Override
    public CrudResult<Produit> mettreAJour(Produit unProduit) {
        CrudResult<Boolean> validation = estValide(unProduit);
        if (validation.estUneErreur()) return CrudResult.failure(validation.getErreur());
        int inter = 0;
        String requete = "UPDATE Produit set nom=?, idCategorie=?, prixDeVente=?, stockActuel=?, seuilAlerte=? where idProduit=? AND deletedAt is null";
        PreparedStatement ps = null ;
        
        
        try {
            Connection conn = toConnect();
            ps = conn.prepareStatement(requete);
            
            
            ps.setString(1,unProduit.getNom());
            ps.setInt(2, unProduit.getCategorie().getIDCAT());
            
            ps.setDouble(3, unProduit.getPrixDeVente());
            ps.setInt(4, unProduit.getStockActuel());
            ps.setInt(5, unProduit.getSeuilAlerte());
            ps.setInt(6, unProduit.getIdProduit());
            
            inter = ps.executeUpdate();
            
            ps.close();
            conn.close();
            
        } catch (SQLException ex) {
            return gererExceptionSQL(ex);
        }
        
        if (inter == 0) {
            return CrudResult.failure("Une erreur est survenue");
        }
        
        return CrudResult.success(unProduit);     
    }

    
    @Override
    public CrudResult<Boolean> suppressionDefinitive(Produit unProduit) {
        int inter = 0;
        String requete = "delete from Produit where idProduit = ?";
        PreparedStatement ps = null;
        
        try {
            Connection conn = toConnect();
            ps = conn.prepareStatement(requete);
            ps.setInt(1, unProduit.getIdProduit());
            
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

    @Override
    public CrudResult<Boolean> suppressionLogique(Produit unProduit) {
         String requete = "UPDATE Produit SET deletedAt = NOW() WHERE idProduit = ?";
       

        try {

            Connection conn = toConnect();
            PreparedStatement ps = conn.prepareStatement(requete);

            ps.setInt(1, unProduit.getIdProduit());

            int lignes = ps.executeUpdate();

            if (lignes > 0) {
                return CrudResult.success(true);
            }

            return CrudResult.failure("Produit non trouvé");

        } catch (SQLException ex) {

            return gererExceptionSQL(ex);
        }
        
    }

    @Override
    
    public CrudResult<Boolean> estValide(Produit unProduit) {
        if(unProduit.getPrixDeVente() <= 0){
            return CrudResult.failure("Le prix de vente doit être strictement positif");
        }
       
        if (unProduit.getUser().getDeletedAt() != null) {
            return CrudResult.failure("Cet utilisateur ne peut pas enregistrer");
            
        }
        if(unProduit.getStockActuel() < 0){
            return CrudResult.failure("Le stock ne peut pas être négatif");
        }
        if(unProduit.getSeuilAlerte()< 0){
            return CrudResult.failure("Le seuil d'alerte  ne peut pas être négatif");
        }
        
        return CrudResult.success(true);
          
    }
    
    
    @Override
    public CrudResult<List<Produit>> recupererTout() {
        List<Produit> listeProduit = new ArrayList<>();

        String requete = "SELECT p.* , c.libelle ,u.idUser, u.login " +
                        "from produit p " +
                        "JOIN categorie c ON c.idCat = p.idCategorie " +
                        "LEFT JOIN users u on p.idUser = u.idUser AND u.deletedAt IS NULL " +
                        "where p.deletedAt is null ";
        PreparedStatement ps = null;

        try {
            Connection conn = this.toConnect();
            ps= conn.prepareStatement(requete);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {

                Categorie categorie = new Categorie(rs.getInt(3), rs.getString(9));
                Users user = new Users();
                user.setIdUser(rs.getInt(4));
                user.setLogin(rs.getString(11));
                Produit inter = new Produit();
                inter.setIdProduit(rs.getInt(1));
                inter.setNom(rs.getString(2));
                
                inter.setUser(user);
                inter.setCategorie(categorie);
                inter.setPrixDeVente(rs.getDouble(5));
                inter.setStockActuel(rs.getInt(6));
                inter.setSeuilAlerte(rs.getInt(7));
                

                listeProduit.add(inter);
            }


        } catch (SQLException ex) {

            return gererExceptionSQL(ex);
        }
        return CrudResult.success(listeProduit);
    }
    
    public CrudResult<List<Produit>> recupererProduitsCategorie(Categorie categorie){
        List<Produit> liste_Produit_Categorie = new ArrayList<>();
        String requete = "SELECT p.* , c.libelle ,u.idUser, u.login " +
                        "from produit p " +
                        "JOIN categorie c ON c.idCat = p.idCategorie " +
                        "LEFT JOIN users u on p.idUser = u.idUser AND u.deletedAt IS NULL " +
                        "where p.deletedAt is null AND p.idCategorie = ?";
        PreparedStatement ps = null;

        try {
            Connection conn = this.toConnect();
            ps= conn.prepareStatement(requete);
            ps.setInt(1, categorie.getIDCAT());
            ResultSet rs = ps.executeQuery();
            

            while (rs.next()) {

                
                Users user = new Users();
                user.setIdUser(rs.getInt(4));
                user.setLogin(rs.getString(11));
                Produit inter = new Produit();
                inter.setIdProduit(rs.getInt(1));
                inter.setNom(rs.getString(2));
                
                inter.setUser(user);
                inter.setCategorie(categorie);
                inter.setPrixDeVente(rs.getDouble(5));
                inter.setStockActuel(rs.getInt(6));
                inter.setSeuilAlerte(rs.getInt(7));
                liste_Produit_Categorie.add(inter);
            }
        }catch (SQLException ex) {

            return gererExceptionSQL(ex);
        }
        return CrudResult.success(liste_Produit_Categorie);
                
        
        
        }

    
    
    public static void main(String[] args) {
        
        VariablesEnvirennement.checkVariablesEnvironnement();
        CrudResult<List<Produit>> rr = getInstance().recupererProduitsCategorie(new Categorie(1, ""));
        System.err.println(rr.getDonnes());
        //System.out.println(rr.getDonnes().Afficher());
    }



    
}
