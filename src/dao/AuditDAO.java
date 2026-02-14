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
import utilitaires.Messages;
import java.util.ArrayList;
/**
 *
 * @author sevtify
 */
public class AuditDAO extends AbstractDAO<Audit>{
    
    public static AuditDAO getInstance(){
        return new AuditDAO();
    }
    
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
            return CrudResult.failure(Messages.messageAvecErreur(ex.getMessage()));
        }
        
        if (inter == 0) {
            return CrudResult.failure(Messages.ERROR_OCCURED);
        }
        
        return CrudResult.success(true);
    }

    @Override
    public CrudResult<Audit> lire(int id) {
        //1-Initialisation de la requete 
        String requete = "SELECT * FROM Audit WHERE `idAudit`=?";

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
                    rs.getInt(1), rs.getInt(2),
                    ActionType.valueOf(rs.getString(3)),
                    rs.getString(4), rs.getTimestamp(5).toInstant()
                );
            }
            
            rs.close();

            
            //8- Fermeture de l'objet SQL précompilé
            ps.close();
            
            //9-Fermeture de la connexion
            conn.close();
            
        } catch (SQLException ex) {
            return CrudResult.failure(Messages.messageAvecErreur(ex.getMessage()));
        }
        
        if (auditALire == null) {
            return CrudResult.failure("Cet Audit n'existe pas");
        }
        return CrudResult.success(auditALire);
        
    }

    @Override
    public CrudResult<Audit> mettreAJour(Audit auditAMettreAJour) {
        return CrudResult.failure(Messages.CANNOT_MODIFY_AUDIT);
    }

    @Override
    public CrudResult<Boolean> suppressionDefinitive(Audit auditASupprimer) {
        return CrudResult.failure(Messages.CANNOT_DELETE_AUDIT);
    }

    @Override
    public CrudResult<Boolean> suppressionLogique(Audit entiteASupprimer) {
        return CrudResult.failure(Messages.CANNOT_DELETE_AUDIT);
    }

    @Override
    public CrudResult<Boolean> estValide(Audit entiteAValider) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public CrudResult<List<Audit>> recupererTout() {
        //1-Initialisation de la requete 
        String requete = "SELECT * FROM Audit";

        //2-Initialisation d'un objet SQL précompilé
        PreparedStatement ps = null;
        

        List<Audit> listeDesAudits = new ArrayList<>();
        try {
            
            //3-Ouverture de la connexion
            Connection conn = toConnect();
            
            //4-Préparation de la requete
            ps = conn.prepareStatement(requete);
            
            //6-Initialisation d'une table de donnée
            ResultSet rs = null;
            
            //7-Execution de la requete    
            rs = ps.executeQuery();
            
            while (rs.next()){                
                listeDesAudits.add(
                    new Audit(
                        rs.getInt(1), rs.getInt(2),
                        ActionType.valueOf(rs.getString(3)),
                        rs.getString(4), rs.getTimestamp(5).toInstant()
                    )
                );
            }
            
            rs.close();

            
            //8- Fermeture de l'objet SQL précompilé
            ps.close();
            
            //9-Fermeture de la connexion
            conn.close();
            
        } catch (SQLException ex) {
            return CrudResult.failure(Messages.messageAvecErreur(ex.getMessage()));
        }
        
        return CrudResult.success(listeDesAudits);
    }
    
}
