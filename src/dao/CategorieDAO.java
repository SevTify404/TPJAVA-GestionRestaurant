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

    private CategorieDAO() {
    }
    
    public static CategorieDAO getInstance(){
        return new CategorieDAO();
    }
    
    
   @Override
    public CrudResult<Boolean> enregistrer(Categorie categorie) {

        CrudResult<Boolean> validation = estValide(categorie);
        if (validation.estUneErreur())
            return CrudResult.failure(validation.getErreur());

        String sql = "INSERT INTO categorie(libelle) VALUES (?)";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categorie.getLIBELLE());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                categorie.setIDCAT(rs.getInt(1));
            }

            return CrudResult.success(true);

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<Categorie> lire(int id) {

        String sql = "SELECT * FROM categorie WHERE idCat = ? AND deleteAt IS NULL";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return CrudResult.success(mapper(rs));
            }

            return CrudResult.failure("Catégorie introuvable");

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<Categorie> mettreAJour(Categorie categorie) {

        CrudResult<Boolean> validation = estValide(categorie);
        if (validation.estUneErreur())
            return CrudResult.failure(validation.getErreur());

        String sql = "UPDATE categorie SET libelle = ? WHERE idCat = ? AND deleteAt IS NULL";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categorie.getLIBELLE());
            ps.setInt(2, categorie.getIDCAT());

            int lignes = ps.executeUpdate();

            if (lignes > 0)
                return CrudResult.success(categorie);

            return CrudResult.failure("Aucune modification effectuée");

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<Boolean> suppressionDefinitive(Categorie categorie) {

        String sql = "DELETE FROM categorie WHERE idCat = ?";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categorie.getIDCAT());
            return CrudResult.success(ps.executeUpdate() > 0);

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<Boolean> suppressionLogique(Categorie categorie) {

        String sql = "UPDATE categorie SET deleteAt = NOW() WHERE idCat = ?";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categorie.getIDCAT());
            return CrudResult.success(ps.executeUpdate() > 0);

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    @Override
    public CrudResult<Boolean> estValide(Categorie categorie) {

        if (categorie == null)
            return CrudResult.failure("Objet catégorie null");

        if (categorie.getLIBELLE() == null || categorie.getLIBELLE().isBlank())
            return CrudResult.failure("Libellé obligatoire");

        return CrudResult.success(true);
    }

    @Override
    public CrudResult<List<Categorie>> recupererTout() {

        String sql = "SELECT * FROM categorie WHERE deleteAt IS NULL";
        List<Categorie> liste = new ArrayList<>();

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(mapper(rs));
            }

            return CrudResult.success(liste);

        } catch (SQLException e) {
            return gererExceptionSQL(e);
        }
    }

    // ======================
    // Méthode Mapper
    // ======================
    private Categorie mapper(ResultSet rs) throws SQLException {

        Categorie cat = new Categorie();
        cat.setIDCAT(rs.getInt("idCat"));
        cat.setLIBELLE(rs.getString("libelle"));

        return cat;
    }
    
    
}
