/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.Audit;
import entity.enums.ActionType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import utilitaires.Messages;
import java.time.Instant;
/**
 *
 * @author sevtify
 */
public class AuditDAO extends AbstractDAO<Audit>{

    @Override
    public CrudResult<Boolean> enregistrer(Audit auditAEnregistrer) {
        
        //1-Initialisation de la requete 
        String requete = "INSERT INTO Audit(`idUser`, `action`, `message`, `tempsAction`) VALUES (?, ?, ?, ?)";
        
         //2-Initialisation d'un objet SQL précompilé
        PreparedStatement ps = null;
        int inter = 0;
        
        try {
            
            //3-Ouverture de la connexion
            Connection conn = this.toConnect();
            
            //4-Préparation de la requete
            ps = conn.prepareStatement(requete);
            
            //5-Initialisation des parametres de la requete
            ps.setInt(1, auditAEnregistrer.getIdUser());
            ps.setString(2, auditAEnregistrer.getAction().name());
            ps.setString(3, auditAEnregistrer.getMessage());
            ps.setObject(4, auditAEnregistrer.getTempsAction());
            
            //7-Execution de la requete        
            inter = ps.executeUpdate();
            
            //8- Fermeture de l'objet SQL précompilé
            ps.close();
            
            //9-Fermeture de la connexion
            conn.close();
            
        } catch (SQLException ex) {
            return CrudResult.failure(String.format(Messages.ERROR_OCCURED_WITH_ERROR, ex.getMessage()));
        }
        
        if (inter == 1) {
            return CrudResult.failure(Messages.ERROR_OCCURED);
        }
        
        return CrudResult.success(true);
    }

    @Override
    public CrudResult<Audit> lire(int id) {
        //1-Initialisation de la requete 
        String requete = "SELECT * FROM Audit WHERE `id`=?";

        //2-Initialisation d'un objet SQL précompilé
        PreparedStatement ps = null;
        
        Audit auditALire = null;
        
        try {
            
            //3-Ouverture de la connexion
            Connection conn = toConnect();
            
            //4-Préparatioorn de la requete
            ps = conn.prepareStatement(requete);
            
            //5-Initialisation des parametres de la requete
            ps.setInt(1, id);
            
            
            //6-Initialisation d'une table de donnée
            ResultSet rs = null;
            
            //7-Execution de la requete    
            rs = ps.executeQuery();
            
            if (rs.next()){
                auditALire = new Audit(
                    rs.getInt(1), rs.getInt(2), rs.getObject(3, ActionType.class),
                    rs.getString(4), rs.getObject(5, Instant.class)
                );
            }
            
            rs.close();

            
            //8- Fermeture de l'objet SQL précompilé
            ps.close();
            
            //9-Fermeture de la connexion
            conn.close();
            
        } catch (SQLException ex) {
            return CrudResult.failure(String.format(Messages.ERROR_OCCURED_WITH_ERROR, ex.getMessage()));
        }
        
        if (auditALire == null) {
            return CrudResult.failure("Cet Audit n'existe pas");
        }
        return CrudResult.success(auditALire);
        
    }

    @Override
    public CrudResult<Audit> mettreAJour(Audit entiteAMettreAJour) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public CrudResult<Boolean> suppressionDefinitive(Audit entiteASupprimer) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public CrudResult<Boolean> suppressionLogique(Audit entiteASupprimer) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public CrudResult<Boolean> estValide(Audit entiteAValider) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public CrudResult<List<Audit>> recupererTout() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
