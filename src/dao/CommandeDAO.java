/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.Commande;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import utilitaires.VariablesEnvirennement;


/**
 *
 * @author loyale
 */


public class CommandeDAO extends AbstractDAO<Commande> {

    private CommandeDAO() {}

    
    public static CommandeDAO getInstance() {
        return new CommandeDAO();
    }

   @Override
public CrudResult<Boolean> enregistrer(Commande commande) {

    String sql = "INSERT INTO Commande (dateCommande, etat, total) VALUES (?, ?, ?)";

    try (Connection conn = toConnect();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setTimestamp(1, java.sql.Timestamp.valueOf(commande.getDateCommande()));
        ps.setString(2, commande.getEtat().name());
        ps.setDouble(3, commande.getTotal());

        int rows = ps.executeUpdate();

        if (rows == 0) {
            return CrudResult.failure("Insertion échouée");
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
            
            ps.executeUpdate();
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
        // 1. Initialisation de la requête
        String sql = "DELETE FROM Commande WHERE idCommande = ?";

        try (Connection conn = toConnect(); 
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, SupprDef.getIdCommande());    
            ps.executeUpdate();
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
    public CrudResult<Boolean> suppressionLogique(Commande SupprLo) {
        // 1. Initialisation de la requête
        String sql = "UPDATE Commande SET deletedAt = NOW() WHERE idCommande = ?";

        try (Connection conn = toConnect(); 
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, SupprLo.getIdCommande());
            
            ps.executeUpdate();
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
    public CrudResult<Boolean> estValide(Commande etreValide) {
        // 1. Initialisation de la requête
        String sql = "SELECT COUNT(*) FROM Commande WHERE idCommande = ? AND deletedAt IS NULL AND total > 0";

        boolean valide = false;
        try (Connection conn = toConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, etreValide.getIdCommande());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                valide = rs.getInt(1) > 0; // mise à jour de la variable 
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
                commandes.add(c); // ajout à la liste
            }

            return CrudResult.success(commandes);

        } catch (SQLException ex) {
            return gererExceptionSQL(ex);
        }
    }
    
    public static void main(String[] args) {
        VariablesEnvirennement.checkVariablesEnvironnement();
        Commande maCoomange = new Commande(0, LocalDateTime.now(), Commande.EtatCommande.EN_COURS, -8222, LocalDateTime.now());
        CrudResult<Boolean> eee = getInstance().enregistrer(maCoomange);
        
        if (eee.estUnSucces()) {
            System.out.println(eee.getDonnes());
        }else{
            System.out.println(eee.getErreur());
        }
    }

    }
