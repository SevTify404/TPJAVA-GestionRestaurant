/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.mouvementdestock;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ACER
 */

public class mouvementdestockDAO extends AbstractDAO<mouvementdestock> {
@Override
    public CrudResult enregistrer(mouvementdestock mouvement) {
        String sql = "INSERT INTO mouvementdestock(type, quantite, dateMouvement, idProduit, motif, deletedAt) "
                   + "VALUES (?, ?, ?, ?, ?, NULL)";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mouvement.getTYPE().name());
            ps.setInt(2, mouvement.getQUANTITE());
            ps.setTimestamp(3, Timestamp.valueOf(mouvement.getDATEMOUVEMENT()));
            ps.setInt(4, mouvement.getIDPRODUIT());
            ps.setString(5, mouvement.getMOTIF());

            ps.executeUpdate();
            return CrudResult.success("Mouvement enregistré");

        } catch (SQLException e) {
            return CrudResult.failure(e.getMessage());
        }
    }

    @Override
    public CrudResult lire(int id) {
        String sql = "SELECT * FROM mouvementdestock WHERE id = ? AND deletedAt IS NULL";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                mouvementdestock mv = new mouvementdestock();
                mv.setID(rs.getInt("id"));
                mv.setTYPE(mouvementdestock.TypeMouvement.valueOf(rs.getString("type")));
                mv.setQUANTITE(rs.getInt("quantite"));
                mv.setDATEMOUVEMENT(rs.getTimestamp("dateMouvement").toLocalDateTime());
                mv.setIDPRODUIT(rs.getInt("idProduit"));
                mv.setMOTIF(rs.getString("motif"));
                return CrudResult.success(mv);
            }

            return CrudResult.failure("Introuvable");

        } catch (SQLException e) {
            return CrudResult.failure(e.getMessage());
        }
    }

    @Override
    public CrudResult mettreAJour(mouvementdestock mv) {
        String sql = """
            UPDATE mouvementdestock
            SET type = ?, quantite = ?, dateMouvement = ?, idProduit = ?, motif = ?
            WHERE id = ? AND deletedAt IS NULL
            """;

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mv.getTYPE().name());
            ps.setInt(2, mv.getQUANTITE());
            ps.setTimestamp(3, Timestamp.valueOf(mv.getDATEMOUVEMENT()));
            ps.setInt(4, mv.getIDPRODUIT());
            ps.setString(5, mv.getMOTIF());
            ps.setInt(6, mv.getID());

            ps.executeUpdate();
            return CrudResult.success("Mise à jour OK");

        } catch (SQLException e) {
            return CrudResult.failure(e.getMessage());
        }
    }

    @Override
    public CrudResult suppressionDefinitive(mouvementdestock mv) {
        String sql = "DELETE FROM mouvementdestock WHERE id = ?";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mv.getID());
            ps.executeUpdate();
            return CrudResult.success("Suppression définitive OK");

        } catch (SQLException e) {
            return CrudResult.failure(e.getMessage());
        }
    }

    @Override
    public CrudResult<Boolean> suppressionLogique(mouvementdestock mv) {
        String sql = "UPDATE mouvementdestock SET deletedAt = NOW() WHERE id = ?";

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mv.getID());
            int lignes = ps.executeUpdate();
            return CrudResult.success(lignes > 0);

        } catch (SQLException e) {
            return CrudResult.failure(e.getMessage());
        }
    }

    @Override
    public CrudResult<Boolean> estValide(mouvementdestock mv) {
        if (mv == null)
            return CrudResult.failure("Objet null");
        if (mv.getTYPE() == null)
            return CrudResult.failure("Type obligatoire");
        if (mv.getQUANTITE() <= 0)
            return CrudResult.failure("Quantité invalide");
        if (mv.getDATEMOUVEMENT() == null)
            return CrudResult.failure("Date obligatoire");
        if (mv.getIDPRODUIT() <= 0)
            return CrudResult.failure("Produit invalide");

        return CrudResult.success(true);
    }

    @Override
    public CrudResult<List<mouvementdestock>> recupererTout() {
        String sql = "SELECT * FROM mouvementdestock WHERE deletedAt IS NULL";

        List<mouvementdestock> liste = new ArrayList<>();

        try (Connection conn = toConnect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                mouvementdestock mv = new mouvementdestock();
                mv.setID(rs.getInt("id"));
                mv.setTYPE(mouvementdestock.TypeMouvement.valueOf(rs.getString("type")));
                mv.setQUANTITE(rs.getInt("quantite"));
                mv.setDATEMOUVEMENT(rs.getTimestamp("dateMouvement").toLocalDateTime());
                mv.setIDPRODUIT(rs.getInt("idProduit"));
                mv.setMOTIF(rs.getString("motif"));
                liste.add(mv);
            }

            return CrudResult.success(liste);

        } catch (SQLException e) {
            return CrudResult.failure(e.getMessage());
        }
    }
}