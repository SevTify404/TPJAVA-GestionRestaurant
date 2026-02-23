/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.Categorie;
import entity.Commande;
import entity.LigneCommande;
import entity.Produit;
import entity.Users;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.sql.Timestamp;

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
        if (commande.getUtilisateur().getIdUser() == 0) return CrudResult.failure("Utilisateur invalide");
        
        String sql = "INSERT INTO Commande (dateCommande, etat, total, idUser) VALUES (?, ?, ?, ?)";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1, java.sql.Timestamp.valueOf(commande.getDateCommande()));
            ps.setString(2, commande.getEtat().name());
            ps.setDouble(3, commande.getTotal());
            ps.setInt(4, commande.getUtilisateur().getIdUser());

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
    public CrudResult<Commande> enregistrerAvecRetourId(Commande commande) {
        if (commande == null) return CrudResult.failure("Commande null");
        if (commande.getDateCommande() == null) return CrudResult.failure("Date manquante");
        if (commande.getEtat() == null) return CrudResult.failure("Etat manquant");
        if (commande.getTotal() <= 0) return CrudResult.failure("Total invalide");
        if (commande.getUtilisateur().getIdUser() == 0) return CrudResult.failure("Utilisateur invalide");
        
        String sql = "INSERT INTO Commande (dateCommande, etat, total, idUser) VALUES (?, ?, ?, ?)";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1, java.sql.Timestamp.valueOf(commande.getDateCommande()));
            ps.setString(2, commande.getEtat().name());
            ps.setDouble(3, commande.getTotal());
            ps.setInt(4, commande.getUtilisateur().getIdUser());

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

        } catch (SQLException ex) {
                return gererExceptionSQL(ex);
        }
        
        return CrudResult.success(commande);                       
    }
        
    @Override
    public CrudResult<Commande> lire(int id) {
        
        // 1. Initialisation de la requête
        String sql = """
                     SELECT c.idCommande, c.dateCommande, c.etat, c.total, u.idUser, u.login, u.isAdmin, u.sexe
                     FROM Commande c
                     LEFT JOIN Users u ON u.idUser = c.idUser AND u.deletedAt IS NULL WHERE c.idCommande = ? AND c.deletedAt IS NULL""";
        try (Connection conn = toConnect(); 
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
           
            try (ResultSet rs = ps.executeQuery()) {
                 if (!rs.next()) {
                    return CrudResult.failure("Commande introuvable");
                }
                Users utilisateur = new Users();
                if (rs.getInt("idUser") != 0) {
                    utilisateur.setIdUser(rs.getInt("idUser"));
                    utilisateur.setLogin(rs.getString("login"));
                    utilisateur.setIsAdmin(rs.getBoolean("isAdmin"));
                    utilisateur.setSexe(rs.getString("sexe"));
                }
                
                Commande c = new Commande();
                c.setIdCommande(rs.getInt("idCommande"));
                c.setDateCommande(rs.getTimestamp("dateCommande").toLocalDateTime());
                c.setEtat(Commande.EtatCommande.valueOf(rs.getString("etat").toUpperCase()));
                c.setTotal(rs.getDouble("total"));
                c.setUtilisateur(utilisateur);
                    
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
        String sql = """
                         SELECT c.idCommande, c.dateCommande, c.etat, c.total, u.idUser, u.login, u.isAdmin, u.sexe
                         FROM Commande c
                         LEFT JOIN Users u ON u.idUser = c.idUser AND u.deletedAt IS NULL WHERE c.deletedAt IS NULL""";

        List<Commande> commandes = new ArrayList<>();

        try (Connection conn = toConnect(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Users utilisateur = new Users();
                if (rs.getInt("idUser") != 0) {
                    utilisateur.setIdUser(rs.getInt("idUser"));
                    utilisateur.setLogin(rs.getString("login"));
                    utilisateur.setIsAdmin(rs.getBoolean("isAdmin"));
                    utilisateur.setSexe(rs.getString("sexe"));
                }
                Commande c = new Commande();
                c.setIdCommande(rs.getInt("idCommande"));
                c.setDateCommande(rs.getTimestamp("dateCommande").toLocalDateTime());
                c.setEtat(Commande.EtatCommande.valueOf(rs.getString("etat").toUpperCase()));
                c.setTotal(rs.getDouble("total"));
                c.setUtilisateur(utilisateur);
                commandes.add(c);
            }

            return CrudResult.success(commandes);

        } catch (SQLException ex) {
            return gererExceptionSQL(ex);
        }
    }
    public CrudResult<List<Commande>> recupererCommandeEnCoursUsers(Users user) {
        String sql = """
                         SELECT c.idCommande, c.dateCommande, c.etat, c.total, u.idUser, u.login, u.isAdmin, u.sexe
                         FROM Commande c
                         LEFT JOIN Users u ON u.idUser = c.idUser AND u.deletedAt IS NULL WHERE c.deletedAt IS NULL and c.etat = 'EN_COURS'""";

        List<Commande> commandes = new ArrayList<>();

        try (Connection conn = toConnect(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Users utilisateur = new Users();
                if (rs.getInt("idUser") != 0) {
                    utilisateur.setIdUser(rs.getInt("idUser"));
                    utilisateur.setLogin(rs.getString("login"));
                    utilisateur.setIsAdmin(rs.getBoolean("isAdmin"));
                    utilisateur.setSexe(rs.getString("sexe"));
                }
                Commande c = new Commande();
                c.setIdCommande(rs.getInt("idCommande"));
                c.setDateCommande(rs.getTimestamp("dateCommande").toLocalDateTime());
                c.setEtat(Commande.EtatCommande.valueOf(rs.getString("etat").toUpperCase()));
                c.setTotal(rs.getDouble("total"));
                c.setUtilisateur(utilisateur);
                commandes.add(c);
            }

            return CrudResult.success(commandes);

        } catch (SQLException ex) {
            return gererExceptionSQL(ex);
        }
    }
    
    public CrudResult<Map<String, Integer>> recupererInfosJourPourDashboard() {
        Map<String, Integer> dictionnaireResultat = new HashMap<>();
        
        String sql = """
                         SELECT 
                             COUNT(*) AS nbCommandesValidees,
                             COALESCE(SUM(total), 0) AS chiffreAffaireJour
                         FROM Commande
                         WHERE etat = 'VALIDE'
                         AND dateCommande >= CURDATE()
                         AND dateCommande < CURDATE() + INTERVAL 1 DAY;""";

        try (Connection conn = toConnect(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()){
                dictionnaireResultat.put("count", rs.getInt("nbCommandesValidees"));
                dictionnaireResultat.put("chiffreAffaireJour", rs.getInt("chiffreAffaireJour"));
            }
            
            return CrudResult.success(dictionnaireResultat);

        } catch (SQLException ex) {
            return gererExceptionSQL(ex);
        }
    }
    public CrudResult<List<Commande>> recuperer10DerniersCommandesAvecLignePourDashBoard(int nbrCommande) {
        String requete = """
                         SELECT 
                             c.idCommande,
                             c.total,
                             lc.idLC,
                             lc.quantite,
                             p.nom AS nomProduit
                         FROM (
                             SELECT idCommande
                             FROM Commande
                             ORDER BY dateCommande DESC
                             LIMIT 10
                         ) AS last10
                         JOIN Commande c ON c.idCommande = last10.idCommande
                         JOIN LigneCommande lc ON lc.idCommande = c.idCommande
                         JOIN Produit p ON p.idProduit = lc.idProduit
                         WHERE c.etat = 'VALIDEE' AND c.deletedAt IS NULL
                         ORDER BY c.dateCommande DESC""";
        
        Map<Integer, Commande> commandeMap = new LinkedHashMap<>();
        PreparedStatement ps = null;

        try {
            Connection conn = this.toConnect();
            ps= conn.prepareStatement(requete);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {

                int idCommande = rs.getInt("idCommande");
                Commande commande = commandeMap.get(idCommande);

                if (commande == null) {
                    commande = new Commande();
                    commande.setIdCommande(idCommande);
                    commande.setTotal(rs.getDouble("total"));
                    commandeMap.put(idCommande, commande);
                }

                
                LigneCommande ligne = new LigneCommande();
                ligne.setQuantite(rs.getInt("quantite"));

                
                Produit produit = new Produit();
                produit.setNom(rs.getString("nomProduit"));
                ligne.setProduit(produit);
                
                commande.getLigneCommnandes().add(ligne);
            }
            
            conn.close();
            ps.close();
            rs.close();


        } catch (SQLException ex) {
            return gererExceptionSQL(ex);
        }
        
        return CrudResult.success(new ArrayList<>(commandeMap.values()));
    }
    
    
    public CrudResult<List<LigneCommande>> recupererLignes(int idCommande) {

    if (idCommande <= 0) {
        return CrudResult.failure("ID commande invalide");
    }

    String sql = """
        SELECT lc.idLC, lc.idCommande, lc.idProduit, lc.quantite, lc.prixUnitaire, lc.montantLigne, 
                 p.prixDeVente, p.stockActuel, p.seuilAlerte, p.nom
        FROM LigneCommande lc JOIN Produit p ON p.idProduit = lc.idProduit
         WHERE lc.idCommande = ? AND p.deletedAt IS NULL AND lc.deletedAt IS NULL AND p.stockActuel >= lc.quantite
    """;

    List<LigneCommande> lignes = new ArrayList<>();

    try (Connection conn = toConnect();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idCommande);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LigneCommande laLigne = map(rs);
                Produit produit = new Produit();
                produit.setIdProduit(rs.getInt("idProduit"));
                produit.setNom(rs.getString("nom"));
                produit.setPrixDeVente(rs.getDouble("prixDeVente"));
                produit.setStockActuel(rs.getInt("stockActuel"));
                laLigne.setProduit(produit);
                lignes.add(laLigne);
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
    
    public CrudResult<Commande> enregistrerAvecConnexion(
        Connection conn,
        Commande commande) throws SQLException {

    String sql = "INSERT INTO Commande (dateCommande, etat, total, idUser) VALUES (?, ?, ?, ?)";

    try (PreparedStatement ps =
                 conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

        ps.setTimestamp(1, Timestamp.valueOf(commande.getDateCommande()));
        ps.setString(2, commande.getEtat().name());
        ps.setDouble(3, commande.getTotal());
        ps.setInt(4, commande.getUtilisateur().getIdUser());

        int rows = ps.executeUpdate();
        if (rows == 0) return CrudResult.failure("Insertion échouée");

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (!rs.next())
                return CrudResult.failure("Impossible de récupérer ID");

            commande.setIdCommande(rs.getInt(1));
        }

        return CrudResult.success(commande);
    }
}
    
    public CrudResult<Boolean> creerCommandeAvecLignes(
        Commande commande,
        List<LigneCommande> lignes
    ) {

        if (commande == null || lignes == null || lignes.isEmpty()) {
            return CrudResult.failure("Commande ou lignes invalides");
        }

        try (Connection conn = toConnect()) {

            conn.setAutoCommit(false); // 🔥 DEBUT TRANSACTION


            CrudResult<Commande> resCommande = enregistrerAvecConnexion(conn, commande);

            if (resCommande.estUneErreur()) {
                conn.rollback();
                return CrudResult.failure(resCommande.getErreur());
            }

            Commande commandePersisted = resCommande.getDonnes();
            
            for (LigneCommande ligne : lignes) {

                ligne.setCommande(commandePersisted);
                ligne.recalculerMontant();

                // 🔥 Vérification stock AVANT update
                int stockActuel = ProduitDAO.getInstance().recupererStockAvecConnexion(conn, ligne.getIdProduit());

                if (stockActuel < ligne.getQuantite()) {
                    conn.rollback();
                    return CrudResult.failure(
                            "Stock insuffisant pour le produit ID: "
                                    + ligne.getIdProduit());
                }

                // 🔥 Décrément stock
                CrudResult<Boolean> updateStock =
                        ProduitDAO.getInstance().decrementerStockAvecConnexion(
                                conn,
                                ligne.getIdProduit(),
                                ligne.getQuantite());

                if (updateStock.estUneErreur()) {
                    conn.rollback();
                    return updateStock;
                }
            }

            // 3️⃣ Insert lignes batch
            CrudResult<Boolean> resLignes =
                    LigneCommandeDAO.getInstance()
                            .enregistrerPlusieursAvecConnexion(conn, lignes);

            if (resLignes.estUneErreur()) {
                conn.rollback();
                return resLignes;
            }

            conn.commit(); // ✅ TOUT EST OK
            return CrudResult.success(true);

        } catch (SQLException e) {
            return CrudResult.failure(e.getMessage());
        }
    }

}
