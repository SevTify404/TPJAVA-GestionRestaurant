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
    
    private <T> CrudResult<T> gererException(SQLException e, String action) {
    System.getLogger(LigneCommandeDAO.class.getName())
          .log(System.Logger.Level.ERROR, "Erreur lors de " + action, e);
    return CrudResult.failure("Erreur SQL lors de " + action + " : " + e.getMessage());
    }
    
    private CrudResult<Boolean> validerLigneCommande(LigneCommande ligne) {
    if (ligne == null) return CrudResult.failure("LigneCommande null");
    if (ligne.getIdLC() <= 0) return CrudResult.failure("ID invalide");
    if (ligne.getIdCommande() <= 0) return CrudResult.failure("ID commande invalide");
    if (ligne.getQuantite() <= 0) return CrudResult.failure("Quantité invalide");
    if (ligne.getPrixUnitaire() <= 0) return CrudResult.failure("Prix unitaire invalide");
    return CrudResult.success(true);
    }
   
    private CrudResult<Boolean> validerPourInsert(LigneCommande ligne) {

    if (ligne == null) return CrudResult.failure("LigneCommande null");
    if (ligne.getIdCommande() <= 0) return CrudResult.failure("ID commande invalide");
    if (ligne.getIdProduit() <= 0) return CrudResult.failure("ID produit invalide");
    if (ligne.getQuantite() <= 0) return CrudResult.failure("Quantité invalide");
    if (ligne.getPrixUnitaire() <= 0) return CrudResult.failure("Prix unitaire invalide");

    return CrudResult.success(true);
    }

    
    @Override
    public CrudResult<Boolean> enregistrer(LigneCommande ligneCommande) {
        
        CrudResult<Boolean> validation = validerPourInsert(ligneCommande);
        if (!validation.estUnSucces()) return validation;
    
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
            return gererExceptionSQL(e);
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
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<LigneCommande> mettreAJour(LigneCommande AMettreAJour) {
        CrudResult<Boolean> validation = validerLigneCommande(AMettreAJour);
        if (!validation.estUnSucces()) {
            return CrudResult.failure(validation.getErreur());
        }
        String sql = "UPDATE LigneCommande SET idCommande = ?, idProduit = ?, quantite = ?, prixUnitaire = ?, montantLigne = ?, deletedAt = ? WHERE idLC = ?";

        try (Connection conn = toConnect(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // recalculer le montant avant update
            AMettreAJour.setMontantLigne(AMettreAJour.getQuantite() * AMettreAJour.getPrixUnitaire());

            ps.setInt(1, AMettreAJour.getIdCommande());
            ps.setInt(2, AMettreAJour.getIdProduit());
            ps.setInt(3, AMettreAJour.getQuantite());
            ps.setDouble(4, AMettreAJour.getPrixUnitaire());
            ps.setDouble(5, AMettreAJour.getMontantLigne());
            if (AMettreAJour.getDeletedAt() != null) {
                ps.setTimestamp(6, java.sql.Timestamp.valueOf(AMettreAJour.getDeletedAt()));
            } else {
                ps.setNull(6, java.sql.Types.TIMESTAMP);
            }
            ps.setInt(7, AMettreAJour.getIdLC());

            int rows = ps.executeUpdate();
            if (rows == 0) return CrudResult.failure("Aucune ligne mise à jour");

            return CrudResult.success(AMettreAJour);

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<Boolean> suppressionDefinitive(LigneCommande entiteASupprimer) {
        if (entiteASupprimer == null || entiteASupprimer.getIdLC() <= 0) return CrudResult.failure("ID invalide pour suppression");
        String sql = "DELETE FROM LigneCommande WHERE idLC = ?";
        
        try (Connection conn = toConnect(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entiteASupprimer.getIdLC());
            int rows = ps.executeUpdate();
            if (rows == 0) return CrudResult.failure("Aucune ligne supprimée");

            return CrudResult.success(true);

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<Boolean> suppressionLogique(LigneCommande entiteASupprimer) {
        if (entiteASupprimer == null || entiteASupprimer.getIdLC() <= 0) return CrudResult.failure("ID invalide pour suppression");
        String sql = "UPDATE LigneCommande SET deletedAt = NOW() WHERE idLC = ?";

        try (Connection conn = toConnect(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entiteASupprimer.getIdLC());
            int rows = ps.executeUpdate();
            if (rows == 0) return CrudResult.failure("Aucune ligne supprimée logiquement");

            return CrudResult.success(true);

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<Boolean> estValide(LigneCommande entiteAValider) {
        if (entiteAValider == null || entiteAValider.getIdLC() <= 0) return CrudResult.failure("ID invalide pour validation");
        String sql = "SELECT COUNT(*) FROM LigneCommande WHERE idLC = ? AND deletedAt IS NULL AND quantite > 0 AND prixUnitaire > 0";

        try (Connection conn = toConnect(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entiteAValider.getIdLC());

            try (ResultSet rs = ps.executeQuery()) {
                boolean valide = rs.next() && rs.getInt(1) > 0;
                return CrudResult.success(valide);
            }

        } catch (SQLException e) {
            return gererException(e, "Validation");
        }
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
            return gererExceptionSQL(e);
        }

    }
}
