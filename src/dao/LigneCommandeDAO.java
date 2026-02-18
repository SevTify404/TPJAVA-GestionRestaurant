/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.Connection;
import java.util.List;
import entity.LigneCommande;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author loyale
 */

public class LigneCommandeDAO extends AbstractDAO<LigneCommande>{

    private LigneCommandeDAO() {}
    
    public static LigneCommandeDAO getInstance(){
        return new LigneCommandeDAO();
    }
    
    

        @Override
    public CrudResult<Boolean> enregistrer(LigneCommande ligneCommande) {
        
        CrudResult<Boolean> validation = estValide(ligneCommande);
        
        if (validation.estUneErreur()) return validation;
        
        String sql = "INSERT INTO LigneCommande (idCommande, idProduit, quantite, prixUnitaire, montantLigne) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = toConnect(); 
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, ligneCommande.getIdCommande());
            ps.setInt(2, ligneCommande.getIdProduit());
            ps.setInt(3, ligneCommande.getQuantite());
            ps.setDouble(4, ligneCommande.getPrixUnitaire());
            ps.setDouble(5, ligneCommande.getQuantite() * ligneCommande.getPrixUnitaire());

            int rows = ps.executeUpdate();
            if (rows == 0) return CrudResult.failure("Insertion échouée");

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    ligneCommande.setIdLC(rs.getInt(1));
                }
            }

            return CrudResult.success(true);

        } catch (SQLException e) {
            return CrudResult.failure("Erreur SQL : " + e.getMessage());
        }
    }

    @Override
    public CrudResult<LigneCommande> lire(int id) {
        String sql = "SELECT idLC, idCommande, idProduit, quantite, prixUnitaire, montantLigne, deletedAt FROM LigneCommande WHERE idLC = ? AND deletedAt IS NULL";

        try (Connection conn = toConnect(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return CrudResult.failure("Ligne introuvable");

                LigneCommande lc = new LigneCommande();
                lc.setIdLC(rs.getInt("idLC"));
                lc.setIdCommande(rs.getInt("idCommande"));
                lc.setIdProduit(rs.getInt("idProduit"));
                lc.setQuantite(rs.getInt("quantite"));
                lc.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                lc.setMontantLigne(rs.getDouble("montantLigne"));

                return CrudResult.success(lc);
            }

        } catch (SQLException e) {
            return CrudResult.failure("Erreur SQL : " + e.getMessage());
        }
    }

    @Override
    public CrudResult<LigneCommande> mettreAJour(LigneCommande AMettreAJour) {
        
        CrudResult<Boolean> validation = estValide(AMettreAJour);
        
        if (validation.estUneErreur()) return CrudResult.failure(validation.getErreur());
        
        String sql = "UPDATE LigneCommande SET quantite = ?, idProduit = ?, prixUnitaire = ?, montantLigne = ? WHERE idLC = ?";
        try (Connection conn = toConnect(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // recalculer le montant avant update
            AMettreAJour.recalculerMontant();
            ps.setInt(1, AMettreAJour.getQuantite());
            ps.setInt(2, AMettreAJour.getIdProduit());
            ps.setDouble(3, AMettreAJour.getPrixUnitaire());
            ps.setDouble(4, AMettreAJour.getMontantLigne());
            ps.setInt(5, AMettreAJour.getIdLC());
            System.out.println(ps.toString());

            int rows = ps.executeUpdate();
            if (rows == 0) return CrudResult.failure("Aucune ligne mise à jour");

            return CrudResult.success(AMettreAJour);

        } catch (SQLException e) {
            return CrudResult.failure("Erreur SQL : " + e.getMessage());
        }
    }

    @Override
    public CrudResult<Boolean> suppressionDefinitive(LigneCommande entiteASupprimer) {
        String sql = "DELETE FROM LigneCommande WHERE idLC = ?";

        try (Connection conn = toConnect(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entiteASupprimer.getIdLC());
            int rows = ps.executeUpdate();
            if (rows == 0) return CrudResult.failure("Aucune ligne supprimée");

            return CrudResult.success(true);

        } catch (SQLException e) {
            return CrudResult.failure("Erreur SQL : " + e.getMessage());
        }
    }

    @Override
    public CrudResult<Boolean> suppressionLogique(LigneCommande entiteASupprimer) {
        String sql = "UPDATE LigneCommande SET deletedAt = NOW() WHERE idLC = ?";

        try (Connection conn = toConnect(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entiteASupprimer.getIdLC());
            int rows = ps.executeUpdate();
            if (rows == 0) return CrudResult.failure("Aucune ligne supprimée logiquement");

            return CrudResult.success(true);

        } catch (SQLException e) {
            return CrudResult.failure("Erreur SQL : " + e.getMessage());
        }
    }

    @Override
    public CrudResult<Boolean> estValide(LigneCommande entiteAValider) {
        if (entiteAValider.getPrixUnitaire() <= 0) {
            return CrudResult.failure("Le PU d'une ligne de Commande doit etre supérieur à 0");
        }
        if (entiteAValider.getQuantite()<= 0) {
            return CrudResult.failure("La Quantité d'une ligne de Commande doit etre supérieur à 0");
        }
        entiteAValider.recalculerMontant();
        
        return CrudResult.success(true);
    }

    @Override
    public CrudResult<List<LigneCommande>> recupererTout() {
        String sql = "SELECT idLC, idCommande, idProduit, quantite, prixUnitaire, montantLigne, deletedAt FROM LigneCommande WHERE deletedAt IS NULL";

        List<LigneCommande> lignes = new ArrayList<>();
        try (Connection conn = toConnect(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LigneCommande lc = new LigneCommande();
                lc.setIdLC(rs.getInt("idLC"));
                lc.setIdCommande(rs.getInt("idCommande"));
                lc.setIdProduit(rs.getInt("idProduit"));
                lc.setQuantite(rs.getInt("quantite"));
                lc.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                lc.setMontantLigne(rs.getDouble("montantLigne"));
                lignes.add(lc);
            }

            return CrudResult.success(lignes);

        } catch (SQLException e) {
            return CrudResult.failure("Erreur SQL : " + e.getMessage());
        }
    }
}

    









   

