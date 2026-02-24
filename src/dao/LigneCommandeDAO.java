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
import java.sql.Timestamp;
import java.sql.Types;
import entity.Commande;
import entity.Produit;
import java.time.LocalDateTime;

/**
 *
 * @author loyale
 */


public class LigneCommandeDAO extends AbstractDAO<LigneCommande>{
    
    public static LigneCommandeDAO getInstance(){
        return new LigneCommandeDAO();
    }
    
    
    
    private CrudResult<Boolean> validerLigneCommande(LigneCommande ligne) {

    if (ligne == null)
        return CrudResult.failure("LigneCommande null");

    if (ligne.getIdLC() <= 0)
        return CrudResult.failure("ID ligne invalide");

    if (ligne.getCommande() == null || ligne.getCommande().getIdCommande() <= 0)
        return CrudResult.failure("Commande invalide");

    if (ligne.getProduit() == null || ligne.getProduit().getIdProduit() <= 0)
        return CrudResult.failure("Produit invalide");

    if (ligne.getQuantite() <= 0)
        return CrudResult.failure("Quantité invalide");

    double prix = ligne.getPrixUnitaire();

    if (prix <= 0) {
        double prixProduit = ligne.getProduit().getPrixDeVente();
        if (prixProduit <= 0)
            return CrudResult.failure("Prix unitaire invalide");
        ligne.setPrixUnitaire(prixProduit);
    }

    return CrudResult.success(true);
}
   
    private CrudResult<Boolean> validerPourInsert(LigneCommande ligne) {

    if (ligne == null)
        return CrudResult.failure("LigneCommande null");

    if (ligne.getIdCommande() <= 0)
        return CrudResult.failure("ID commande invalide");

    if (ligne.getIdProduit() <= 0)
        return CrudResult.failure("ID produit invalide");

    if (ligne.getQuantite() <= 0)
        return CrudResult.failure("Quantité invalide");

    if (ligne.getPrixUnitaire() <= 0)
        return CrudResult.failure("Prix unitaire invalide");

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
            
            ligneCommande.recalculerMontant();
            ps.setDouble(5, ligneCommande.getMontantLigne());
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
    
    public CrudResult<Boolean> enregistrerPlusieursAvecConnexion(
        Connection conn,
        List<LigneCommande> lignes) throws SQLException {

    String sql = "INSERT INTO LigneCommande (idCommande, idProduit, quantite, prixUnitaire, montantLigne) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {

        for (LigneCommande ligne : lignes) {

            ps.setInt(1, ligne.getIdCommande());
            ps.setInt(2, ligne.getIdProduit());
            ps.setInt(3, ligne.getQuantite());
            ps.setDouble(4, ligne.getPrixUnitaire());
            ps.setDouble(5, ligne.getMontantLigne());

            ps.addBatch();
        }

        ps.executeBatch();
        return CrudResult.success(true);
    }
}
    public CrudResult<Boolean> enregistrerPlusieurs(List<LigneCommande> lignes) {

        if (lignes == null || lignes.isEmpty()) {
            return CrudResult.failure("Aucune ligne à enregistrer");
        }

        String sql = "INSERT INTO LigneCommande (idCommande, idProduit, quantite, prixUnitaire, montantLigne) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false); // 🔥 transaction manuelle

            for (LigneCommande ligne : lignes) {

                CrudResult<Boolean> validation = validerPourInsert(ligne);
                if (!validation.estUnSucces()) {
                    conn.rollback();
                    return validation;
                }

                ligne.recalculerMontant();

                ps.setInt(1, ligne.getIdCommande());
                ps.setInt(2, ligne.getIdProduit());
                ps.setInt(3, ligne.getQuantite());
                ps.setDouble(4, ligne.getPrixUnitaire());
                ps.setDouble(5, ligne.getMontantLigne());

                ps.addBatch(); // 💥 ajout au batch
            }

            int[] results = ps.executeBatch(); 

            
            for (int r : results) {
                if (r == PreparedStatement.EXECUTE_FAILED) {
                    conn.rollback();
                    return CrudResult.failure("Une des insertions a échouéeeeee ogbooooo");
                }
            }

            

            conn.commit(); 
            return CrudResult.success(true);

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<LigneCommande> lire(int id) {
        String sql =
        "SELECT lc.idLC, lc.idCommande, lc.idProduit, lc.quantite, lc.prixUnitaire, lc.montantLigne, lc.deletedAt, " +
        "c.idCommande AS cmd_id, p.idProduit AS prod_id, p.prixDeVente " +
        "FROM LigneCommande lc " +
        "JOIN Commande c ON c.idCommande = lc.idCommande AND c.deletedAt IS NULL " +
        "JOIN Produit p ON p.idProduit = lc.idProduit AND p.deletedAt IS NULL " +
        "WHERE lc.idLC = ? AND lc.deletedAt IS NULL";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return CrudResult.failure("Ligne introuvable");

                LigneCommande lc = new LigneCommande();
                lc.setIdLC(rs.getInt("idLC"));
                lc.setQuantite(rs.getInt("quantite"));
                lc.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                lc.setDeletedAt(rs.getTimestamp("deletedAt") != null ? rs.getTimestamp("deletedAt").toLocalDateTime() : null);

                // Références à Commande et Produit
                lc.setCommande(new Commande());
                lc.getCommande().setIdCommande(rs.getInt("idCommande"));

                lc.setProduit(new Produit());
                lc.getProduit().setIdProduit(rs.getInt("idProduit"));

                return CrudResult.success(lc);
            }

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<LigneCommande> mettreAJour(LigneCommande lc) {
        CrudResult<Boolean> validation = validerLigneCommande(lc);
        if (!validation.estUnSucces()) return CrudResult.failure(validation.getErreur());

        String sql = "UPDATE LigneCommande SET idCommande = ?, idProduit = ?, quantite = ?, " +
                     "prixUnitaire = ?, montantLigne = ?, deletedAt = ? WHERE idLC = ? AND deletedAt IS NULL ";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // recalculer montant
            lc.recalculerMontant();

            ps.setInt(1, lc.getIdCommande());
            ps.setInt(2, lc.getIdProduit());
            ps.setInt(3, lc.getQuantite());
            ps.setDouble(4, lc.getPrixUnitaire());
            ps.setDouble(5, lc.getMontantLigne());
            if (lc.getDeletedAt() != null) {
                ps.setTimestamp(6, Timestamp.valueOf(lc.getDeletedAt()));
            } else {
                ps.setNull(6, Types.TIMESTAMP);
            }
            ps.setInt(7, lc.getIdLC());

            int rows = ps.executeUpdate();
            if (rows == 0) return CrudResult.failure("Aucune ligne mise à jour");

            return CrudResult.success(lc);

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<Boolean> suppressionDefinitive(LigneCommande lc) {
        if (lc == null || lc.getIdLC() <= 0) return CrudResult.failure("ID invalide pour suppression");

        String sql = "DELETE FROM LigneCommande WHERE idLC = ?";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lc.getIdLC());
            int rows = ps.executeUpdate();
            if (rows == 0) return CrudResult.failure("Aucune ligne supprimée");

            return CrudResult.success(true);

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<Boolean> suppressionLogique(LigneCommande lc) {
        if (lc == null || lc.getIdLC() <= 0) return CrudResult.failure("ID invalide pour suppression");

        String sql = "UPDATE LigneCommande SET deletedAt = ? WHERE idLC = ?";



        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, lc.getIdLC());
            int rows = ps.executeUpdate();
            if (rows == 0) return CrudResult.failure("Aucune ligne supprimée logiquement");

            return CrudResult.success(true);

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<Boolean> estValide(LigneCommande lc) {
        if (lc == null || lc.getIdLC() <= 0) return CrudResult.failure("ID invalide pour validation");

        String sql =
            "SELECT COUNT(*) " +
            "FROM LigneCommande lc " +
            "JOIN Commande c ON c.idCommande = lc.idCommande AND c.deletedAt IS NULL " +
            "JOIN Produit p ON p.idProduit = lc.idProduit AND p.deletedAt IS NULL " +
            "WHERE lc.idLC = ? " +
            "AND lc.deletedAt IS NULL " +
            "AND lc.quantite > 0 " +
            "AND lc.prixUnitaire > 0";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lc.getIdLC());

            try (ResultSet rs = ps.executeQuery()) {
                boolean valide = rs.next() && rs.getInt(1) > 0;
                return CrudResult.success(valide);
            }

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<List<LigneCommande>> recupererTout() {
        String sql = "SELECT lc.idLC,lc.idCommande,lc.idProduit,lc.quantite,lc.prixUnitaire,lc.montantLigne,lc.deletedAt,p.nom AS nomProduit,p.prixDeVent FROM LigneCommande lc LEFT JOIN Produit p ON p.idProduit = lc.idProduit WHERE lc.deletedAt IS NULL;";

        List<LigneCommande> lignes = new ArrayList<>();

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LigneCommande lc = new LigneCommande();
                lc.setIdLC(rs.getInt("idLC"));
                lc.setQuantite(rs.getInt("quantite"));
                lc.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                lc.setDeletedAt(rs.getTimestamp("deletedAt") != null ? rs.getTimestamp("deletedAt").toLocalDateTime() : null);

                lc.setCommande(new Commande());
                lc.getCommande().setIdCommande(rs.getInt("idCommande"));

                lc.setProduit(new Produit());
                lc.getProduit().setIdProduit(rs.getInt("idProduit"));
                
                Produit produit = new Produit();
                produit.setIdProduit(rs.getInt("idProduit"));
                produit.setNom(rs.getString("nomProduit"));
                
                lc.setProduit(produit);
                lignes.add(lc);
            }

            return CrudResult.success(lignes);

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }
}
