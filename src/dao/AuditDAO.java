/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import entity.Audit;
import entity.Users;
import entity.enums.ActionType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import utilitaires.Messages;
import java.util.ArrayList;
import utilitaires.VariablesEnvirennement;
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
            ps.setInt(1, auditAEnregistrer.getUser().getIdUser());
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
    public CrudResult<Boolean> enregistrerAuditSansUtilisateur(Audit auditAEnregistrer) {
        
        //1-Initialisation de la requete 
        String requete = "INSERT INTO Audit(`action`, `message`, `tempsAction`) VALUES (?, ?, ?)";
        
         //2-Initialisation d'un objet SQL précompilé
        PreparedStatement ps = null;
        int inter = 0;
        
        try {
            
            //3-Ouverture de la connexion
            Connection conn = this.toConnect();
            
            //4-Préparation de la requete
            ps = conn.prepareStatement(requete);
            
            //5-Initialisation des parametres de la requete
            ps.setString(1, auditAEnregistrer.getAction().name());
            ps.setString(2, auditAEnregistrer.getMessage());
            ps.setObject(3, auditAEnregistrer.getTempsAction());
            
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
        String requete = "SELECT a.idAudit, a.idUser, a.action, a.message,"
            + " a.tempsAction, u.login, u.isAdmin, u.deletedAt, u.sexe FROM "
            + "Audit a LEFT JOIN Users u ON(a.idUser=u.idUser) WHERE idAudit=?";

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
                auditALire = this.mapperResultSet(rs);      // La methode est en bas
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
        String requete = "SELECT a.idAudit, a.idUser, a.action, a.message,"
            + " a.tempsAction, u.login, u.isAdmin, u.deletedAt, u.sexe FROM "
            + "Audit a LEFT JOIN Users u ON(a.idUser=u.idUser) ORDER BY a.tempsAction DESC";

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
                listeDesAudits.add(this.mapperResultSet(rs));   // La methode est en bas
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
    
    public CrudResult<List<Audit>> recupererAuditDUnUtilisateur(Users utilisateur) {
        //1-Initialisation de la requete 
        String requete = "SELECT a.idAudit, a.idUser, a.action, a.message,"
            + " a.tempsAction, u.login, u.isAdmin, u.deletedAt, u.sexe FROM "
            + "Audit a LEFT JOIN Users u ON(a.idUser=u.idUser) WHERE u.idUser=? ORDER BY a.tempsAction DESC";

        //2-Initialisation d'un objet SQL précompilé
        PreparedStatement ps = null;
        

        List<Audit> listeDesAudits = new ArrayList<>();
        try {
            
            
            //3-Ouverture de la connexion
            Connection conn = toConnect();
            
            //4-Préparation de la requete
            ps = conn.prepareStatement(requete);
            
            ps.setInt(1, utilisateur.getIdUser());
            
            //6-Initialisation d'une table de donnée
            ResultSet rs = null;
            
            //7-Execution de la requete    
            rs = ps.executeQuery();
            
            while (rs.next()){                
                listeDesAudits.add(this.mapperResultSet(rs));   // La methode est en bas
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
    
    private Audit mapperResultSet(ResultSet rsAMapper) throws SQLException{
        Users utilisateur = new Users(
                    rsAMapper.getInt(2),
                    rsAMapper.getString(6),
                    null,
                    rsAMapper.getBoolean(7),
                    rsAMapper.getString(9)
                );
                return new Audit(rsAMapper.getInt(1), utilisateur, ActionType.valueOf(rsAMapper.getString(3)), rsAMapper.getString(4), rsAMapper.getTimestamp(5).toInstant());

    }
    
    // Je test ici pour ne pas aller dans le grand Main
    public static void main(String[] args) {
        
        // Vous devez mettre cette ligne sinon çà marchera pas
        VariablesEnvirennement.checkVariablesEnvironnement();
        
        // Ici vous faites vos tests
        CrudResult<List<Audit>> ss = getInstance().recupererTout();
        for (Audit audit : ss.getDonnes()) {
            System.out.println(audit.afficher());
            System.out.println("\n");
            
        }
        
    }
    
}
