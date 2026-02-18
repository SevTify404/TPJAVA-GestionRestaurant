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
        String requete = "INSERT INTO Produit(idProduit, nom, idCategorie, idUser, prixDeVente, stockActuel, seuilAlerte) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        int inter = 0;
        if (validation.estUneErreur()){
            return validation;
        
        }else{
        
            try{
                Connection conn = toConnect();
                ps = conn.prepareStatement(requete);

                ps.setInt(1, unProduit.getIdProduit());
                ps.setString(2, unProduit.getNom());
                ps.setInt(3, unProduit.getIdcategorie());
                ps.setInt(4, unProduit.getIdUser());
                ps.setDouble(5, unProduit.getPrixDeVente());
                ps.setInt(6, unProduit.getStockActuel());
                ps.setInt(7, unProduit.getSeuilAlerte());

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
    }

    @Override
    public CrudResult<Produit> lire(int idProduit) {
        Produit inter = new Produit();
        String requete = "SELECT * from Produit where idProduit = ? and deletedAt is null";
        PreparedStatement ps = null;
        
        try{
            Connection conn = toConnect();
            ps = conn.prepareStatement(requete);

            ps.setInt(1, idProduit);
            
            ResultSet rs = null;
            rs = ps.executeQuery();
            
            if(rs.next()){
                inter.setIdProduit(rs.getInt(1));
                inter.setNom(rs.getString(2));
                inter.setIdCategorie(rs.getInt(3));
                inter.setIdUser(rs.getInt(4));
                inter.setPrixDeVente(rs.getDouble(5));
                inter.setStockActuel(rs.getInt(6));
                inter.setSeuilAlerte(rs.getInt(7));
                
                rs.close();
                ps.close();
                conn.close();
                
            }
            
            
        }catch(SQLException ex) {
            return CrudResult.failure("Une Erreur Bd est survenue : "+ ex.getMessage());

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
        String requete = "UPDATE Produit set nom=?, idCategorie=?, idUser=?, prixDeVente=?, stockActuel=?, seuilAlerte=? where idProduit=? AND deletedAt is null";
        PreparedStatement ps = null ;
        
        
        try {
            Connection conn = toConnect();
            ps = conn.prepareStatement(requete);
            
            
            ps.setString(1,unProduit.getNom());
            ps.setInt(2, unProduit.getIdcategorie());
            ps.setInt(3, unProduit.getIdUser());
            ps.setDouble(4, unProduit.getPrixDeVente());
            ps.setInt(5, unProduit.getStockActuel());
            ps.setInt(6, unProduit.getSeuilAlerte());
            ps.setInt(7, unProduit.getIdProduit());
            
            inter = ps.executeUpdate();
            
            ps.close();
            conn.close();
            
        } catch (SQLException ex) {
            return CrudResult.failure("Une Erreur Bd est survenue : "+ ex.getMessage());
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
           return CrudResult.failure("Une Erreur Bd est survenue : "+ ex.getMessage());
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

            return CrudResult.failure("Erreur BD : " + ex.getMessage());
        }
        
    }

    @Override
    
    public CrudResult<Boolean> estValide(Produit unProduit) {
        if(unProduit.getPrixDeVente() <= 0){
            return CrudResult.failure("Le prix de vente doit être strictement positif");
            
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

        String requete = "SELECT * FROM Produit where deletedAt is null ";
        PreparedStatement ps = null;

        try {
            Connection conn = this.toConnect();
            ps= conn.prepareStatement(requete);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {

                Produit UnProduit = new Produit();

                UnProduit.setIdProduit(rs.getInt("idProduit"));
                UnProduit.setNom(rs.getString("nom"));
                UnProduit.setIdCategorie(rs.getInt("idCategorie"));
                UnProduit.setIdUser(rs.getInt("idUser"));

                UnProduit.setPrixDeVente(rs.getDouble("prixDeVente"));
                UnProduit.setStockActuel(rs.getInt("stockActuel"));
                UnProduit.setSeuilAlerte(rs.getInt("seuilAlerte"));
                

                listeProduit.add(UnProduit);
            }


        } catch (SQLException ex) {

            return CrudResult.failure("Erreur BD : " + ex.getMessage());
        }
        return CrudResult.success(listeProduit);
        
        
    }

    
    




    
}
