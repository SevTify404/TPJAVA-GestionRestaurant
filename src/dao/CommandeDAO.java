/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.Commande;
import entity.LigneCommande;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author loyale
 */


public class CommandeDAO extends AbstractDAO<Commande> {

     private static CommandeDAO instance;

    // constructeur privé
    private CommandeDAO() {}

    public static CommandeDAO getInstance() {
        if (instance == null) {
            instance = new CommandeDAO();
        }
        return instance;
    }
    
    private LigneCommande map(ResultSet rs) throws SQLException {

    LigneCommande lc = new LigneCommande();

    lc.setIdLC(rs.getInt("idLC"));
    lc.setQuantite(rs.getInt("quantite"));
    lc.setPrixUnitaire(rs.getDouble("prixUnitaire"));
    lc.setMontantLigne(rs.getDouble("montantLigne"));
    return lc;
    }
    

    
    @Override
    public CrudResult<Boolean> enregistrer(Commande commande) {
    if (commande == null) return CrudResult.failure("Commande null");
    if (commande.getDateCommande() == null) return CrudResult.failure("Date manquante");
    if (commande.getEtat() == null) return CrudResult.failure("Etat manquant");
    if (commande.getTotal() <= 0) return CrudResult.failure("Total invalide");
    String sql = "INSERT INTO Commande (dateCommande, etat, total) VALUES (?, ?, ?)";

    try (Connection conn = toConnect();
         PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

        ps.setTimestamp(1, java.sql.Timestamp.valueOf(commande.getDateCommande()));
        ps.setString(2, commande.getEtat().name());
        ps.setDouble(3, commande.getTotal());

        int rows = ps.executeUpdate();

        if (rows == 0) {
            return CrudResult.failure("Insertion échouée");
        }

        //RÉCUPÉRATION DE L’ID AUTO_INCREMENT
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (!rs.next()) {
                return CrudResult.failure("Impossible de récupérer l'ID généré");
            }
            commande.setIdCommande(rs.getInt(1));
        }

        return CrudResult.success(true);

    } catch (SQLException ex) {
            return gererExceptionSQL(ex);
    }
}
        
    @Override
    public CrudResult<Commande> lire(int id) {
        
        // 1. Initialisation de la requête
        String sql = "SELECT idCommande, dateCommande, etat, total FROM Commande WHERE idCommande = ? AND deletedAt IS NULL";
        try (Connection conn = toConnect(); 
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
           
            try (ResultSet rs = ps.executeQuery()) {
                 if (!rs.next()) {
                    return CrudResult.failure("Commande introuvable");
                }
                Commande c = new Commande();
                c.setIdCommande(rs.getInt("idCommande"));
                c.setDateCommande(rs.getTimestamp("dateCommande").toLocalDateTime());
                c.setEtat(Commande.EtatCommande.valueOf(rs.getString("etat").toUpperCase()));
                c.setTotal(rs.getDouble("total"));
                    
                return CrudResult.success(c);
            }
        } catch (SQLException ex) {
            return gererExceptionSQL(ex);
        }
        
    }

    @Override
    public CrudResult<Commande> mettreAJour(Commande AUpdate) {
        
        // 1. Initialisation de la requête
        String sql = "UPDATE Commande SET dateCommande = ?, etat = ?, total = ?, deletedAt = ? WHERE idCommande = ?";

        try (Connection conn = toConnect(); 
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(AUpdate.getDateCommande()));
            ps.setString(2, AUpdate.getEtat().name());
            ps.setDouble(3, AUpdate.getTotal());
            if (AUpdate.getDeletedAt() != null) {
                ps.setTimestamp(4, java.sql.Timestamp.valueOf(AUpdate.getDeletedAt()));
            } else {
                ps.setNull(4, java.sql.Types.TIMESTAMP);
            }
            ps.setInt(5, AUpdate.getIdCommande());
            
            int rows = ps.executeUpdate();

            if (rows == 0) {
                return CrudResult.failure("Commande introuvable");
            }
            return CrudResult.success(AUpdate);
        } catch (SQLException ex) {
            return gererExceptionSQL(ex);

        }
    }

    @Override
    public CrudResult<Boolean> suppressionDefinitive(Commande SupprDef) {

    String sql = "DELETE FROM Commande WHERE idCommande = ?";

    try (Connection conn = toConnect();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, SupprDef.getIdCommande());
           // UNE SEULE FOIS
        if (SupprDef == null || SupprDef.getIdCommande() == 0) {
            return CrudResult.failure("ID invalide");
        }
        int rows = ps.executeUpdate();
        if (rows == 0) {
            return CrudResult.failure("Commande introuvable");
        }
        return CrudResult.success(true);
    } catch (SQLException ex) {
        return gererExceptionSQL(ex);
    }
}

    @Override
    public CrudResult<Boolean> suppressionLogique(Commande cmd) {
    if (cmd == null || cmd.getIdCommande() <= 0) {
        return CrudResult.failure("ID invalide");
    }

    String sqlCommande = "UPDATE Commande SET deletedAt = NOW() WHERE idCommande = ?";
    String sqlLignes   = "UPDATE LigneCommande SET deletedAt = NOW() WHERE idCommande = ?";

    Connection conn = null;
    try {
        conn = toConnect();
        conn.setAutoCommit(false); // transaction manuelle

        try (PreparedStatement psCmd = conn.prepareStatement(sqlCommande);
             PreparedStatement psLigne = conn.prepareStatement(sqlLignes)) {

            psCmd.setInt(1, cmd.getIdCommande());
            psCmd.executeUpdate();

            psLigne.setInt(1, cmd.getIdCommande());
            psLigne.executeUpdate();

            conn.commit(); // commit seulement si tout a réussi
            return CrudResult.success(true);
        }

        } catch (SQLException ex) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException e) { e.printStackTrace(); }
            }
            return gererExceptionSQL(ex);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public CrudResult<Boolean> estValide(Commande etreValide) {
        // 1. Initialisation de la requête
        String sql = "SELECT COUNT(*) FROM Commande WHERE idCommande = ? AND deletedAt IS NULL AND total > 0";

        boolean valide = false;
        try (Connection conn = toConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, etreValide.getIdCommande());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                valide = rs.getInt(1) > 0; // mise à jour de la variable correcte
            }
            }
            return CrudResult.success(valide);
        } catch (SQLException ex) {
            return gererExceptionSQL(ex);
        }
    }

    @Override
    public CrudResult<List<Commande>> recupererTout() {
    String sql = "SELECT idCommande, dateCommande, etat, total, deletedAt FROM Commande WHERE deletedAt IS NULL";

    List<Commande> commandes = new ArrayList<>();

    try (Connection conn = toConnect(); 
         PreparedStatement ps = conn.prepareStatement(sql); 
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Commande c = new Commande();
            c.setIdCommande(rs.getInt("idCommande"));
            c.setDateCommande(rs.getTimestamp("dateCommande").toLocalDateTime());
            c.setEtat(Commande.EtatCommande.valueOf(rs.getString("etat").toUpperCase()));
            c.setTotal(rs.getDouble("total"));
            commandes.add(c);
        }

        return CrudResult.success(commandes);

    } catch (SQLException ex) {
        return gererExceptionSQL(ex);
    }
}

    public CrudResult<List<LigneCommande>> recupererLignes(int idCommande) {

    if (idCommande <= 0) {
        return CrudResult.failure("ID commande invalide");
    }

    String sql = """
        SELECT idLC, idCommande, idProduit, quantite, prixUnitaire, montantLigne
        FROM LigneCommande
        WHERE idCommande = ? AND deletedAt IS NULL
    """;

    List<LigneCommande> lignes = new ArrayList<>();

    try (Connection conn = toConnect();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idCommande);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lignes.add(map(rs));
            }
        }

        return CrudResult.success(lignes);

        } catch (SQLException ex) {
            return gererExceptionSQL(ex);
        }
    }
    
    public CrudResult<Boolean> recalculerTotalCommandeJava(int idCommande) {
    if (idCommande <= 0) {
        return CrudResult.failure("ID commande invalide");
    }

    // 1. Récupérer toutes les lignes de la commande
    CrudResult<List<LigneCommande>> lignesResult = recupererLignes(idCommande);
    if (!lignesResult.estUnSucces()) {
        return CrudResult.failure("Impossible de récupérer les lignes: " + lignesResult.estUneErreur());
    }

    List<LigneCommande> lignes = lignesResult.getDonnes();

    // 2. Calculer le total côté Java
    double total = lignes.stream()
                         .mapToDouble(LigneCommande::getMontantLigne)
                         .sum();

    // 3. Mettre à jour le total dans la table Commande
    String sql = "UPDATE Commande SET total = ? WHERE idCommande = ? AND deletedAt IS NULL";

    try (Connection conn = toConnect();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setDouble(1, total);
        ps.setInt(2, idCommande);

        int rows = ps.executeUpdate();
        if (rows == 0) {
            return CrudResult.failure("Commande introuvable ou supprimée");
        }

        return CrudResult.success(true);

    } catch (SQLException ex) {
        return gererExceptionSQL(ex);
    }
}

}
