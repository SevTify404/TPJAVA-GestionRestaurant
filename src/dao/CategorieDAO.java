/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.Categorie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//import java.sql.DriverManager;
//import java.sql.SQLException;

/**
 *
 * @author ACER
 */
public class CategorieDAO extends AbstractDAO <Categorie>{
    @Override
    public CrudResult enregistrer(Categorie categorie) {
        String sql = "INSERT INTO categorie(libelle) VALUES (?)";

        try (Connection conn = toConnect();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categorie.getLIBELLE());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                categorie.setIDCAT(rs.getInt(1));
            }

            return CrudResult.success("Catégorie enregistrée");

        } catch (SQLException e) {
            return CrudResult.failure(e.getMessage());
        }
    }

    @Override
    public CrudResult<Categorie> lire(int id) {
        String sql = "SELECT * FROM categorie WHERE idCat = ?";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Categorie cat = new Categorie();
                cat.setIDCAT(rs.getInt("idCat"));
                cat.setLIBELLE(rs.getString("libelle"));
                return CrudResult.success(cat);
            }

            return CrudResult.failure("Introuvable");

        } catch (SQLException e) {
            return CrudResult.failure(e.getMessage());
        }
    }

    @Override
    public CrudResult mettreAJour(Categorie categorie) {
        String sql = "UPDATE categorie SET libelle = ? WHERE idCat = ?";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categorie.getLIBELLE());
            ps.setInt(2, categorie.getIDCAT());
            ps.executeUpdate();

            return CrudResult.success("Mise à jour OK");

        } catch (SQLException e) {
            return CrudResult.failure(e.getMessage());
        }
    }

    @Override
    public CrudResult suppressionDefinitive(Categorie categorie) {
        String sql = "DELETE FROM categorie WHERE idCat = ?";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categorie.getIDCAT());
            ps.executeUpdate();
            return CrudResult.success("Suppression définitive OK");

        } catch (SQLException e) {
            return CrudResult.failure(e.getMessage());
        }
    }

    @Override
    public CrudResult<Boolean> suppressionLogique(Categorie categorie) {
        // Pour suppression logique, il faut ajouter "deletedAt" dans la table
        String sql = "UPDATE categorie SET deletedAt = NOW() WHERE idCat = ?";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categorie.getIDCAT());
            int lignes = ps.executeUpdate();
            return CrudResult.success(lignes > 0);

        } catch (SQLException e) {
            return CrudResult.failure(e.getMessage());
        }
    }

    @Override
    public CrudResult<Boolean> estValide(Categorie categorie) {
        if (categorie == null) return CrudResult.failure("Objet null");
        if (categorie.getLIBELLE() == null || categorie.getLIBELLE().isBlank())
            return CrudResult.failure("Libellé obligatoire");

        return CrudResult.success(true);
    }

    @Override
    public CrudResult<List<Categorie>> recupererTout() {
        String sql = "SELECT * FROM categorie";
        List<Categorie> liste = new ArrayList<>();

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categorie cat = new Categorie();
                cat.setIDCAT(rs.getInt("idCat"));
                cat.setLIBELLE(rs.getString("libelle"));
                liste.add(cat);
            }

            return CrudResult.success(liste);

        } catch (SQLException e) {
            return CrudResult.failure(e.getMessage());
        }
    }
    
    
}
