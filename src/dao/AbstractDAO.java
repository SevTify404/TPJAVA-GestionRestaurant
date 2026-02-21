/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utilitaires.Messages;
/**
 *
 * @author sevtify
 * @param <TypeDesEntity>
 */
public abstract class AbstractDAO<TypeDesEntity> implements InterfaceDAO<TypeDesEntity>{
    
    // Cette classe va etre hérité par toutes les classes de DAO, çà fait + POO
    // J'ai ajouté la méthode de connexion ici pour qu'on ai plus a faire des 
    // import dans les DAO meme donc chaque DAO vien déja avec sa 
    
    
    private final Map<String, String> dicoDesContraitesBD = Map.ofEntries(
        Map.entry("fk_audit_user", Messages.CBD_FK_AUDIT_USER),
        Map.entry("libelle", Messages.CBD_LIBELLE),
        Map.entry("check_total_commande", Messages.CBD_CHECK_TOTAL_COMMANDE),
        Map.entry("LigneCommande_index_12", Messages.CBD_LIGNECOMMANDE_INDEX_12),
        Map.entry("fk_ligne_commande", Messages.CBD_FK_LIGNE_COMMANDE),
        Map.entry("fk_ligne_produit", Messages.CBD_FK_LIGNE_PRODUIT),
        Map.entry("LigneCommande_chk_1", Messages.CBD_LIGNECOMMANDE_CHK_1),
        Map.entry("LigneCommande_chk_2", Messages.CBD_LIGNECOMMANDE_CHK_2),
        Map.entry("fk_mouvement_produit", Messages.CBD_FK_MOUVEMENT_PRODUIT),
        Map.entry("MouvementDeStock_chk_1", Messages.CBD_MOUVEMENTDESTOCK_CHK_1),
        Map.entry("nom", Messages.CBD_NOM),
        Map.entry("Produit_index_0", Messages.CBD_PRODUIT_INDEX_0),
        Map.entry("fk_produit_categorie", Messages.CBD_FK_PRODUIT_CATEGORIE),
        Map.entry("fk_produit_user", Messages.CBD_FK_PRODUIT_USER),
        Map.entry("Produit_chk_1", Messages.CBD_PRODUIT_CHK_1),
        Map.entry("Produit_chk_2", Messages.CBD_PRODUIT_CHK_2),
        Map.entry("login", Messages.CBD_LOGIN)
    );
    
    

    protected Connection toConnect() throws SQLException {
       return Connexion.getConnection();
    }
    
    protected CrudResult gererExceptionSQL(SQLException exception){
        String erreurSQL = exception.getSQLState();
        
        if (erreurSQL.equals("42000")) return CrudResult.failure("Erreur de syntaxe SQL : " + exception.getMessage());
        if (erreurSQL.equals("08001")) return CrudResult.failure("Erreur Connexion BD : " + exception.getMessage());
        
        String messageErreurSQL = exception.getMessage();

        String contrainte = null;

        // Les différents codes d'erreur SQL contrainte d'unicité
        switch (exception.getErrorCode()) {
            case 1062 -> contrainte = extraireParRegex(messageErreurSQL, "for key '(.+?)'");
            case 3819 -> contrainte = extraireParRegex(messageErreurSQL, "Check constraint '(.+?)'");
            case 4025, 1451, 1452 -> contrainte = extraireParRegex(messageErreurSQL, "CONSTRAINT `(.+?)`");
            default -> {
                
            }
        }
        
        String erreurSimple = "Erreur BD : " + messageErreurSQL;


        if (contrainte == null) {
            return CrudResult.failure(erreurSimple);
        }
        
        String resultat = dicoDesContraitesBD.get(contrainte);
        
        if (resultat == null) {
            return CrudResult.failure(erreurSimple);
        }
        
        return CrudResult.failure(resultat);
    }
    
    private String extraireParRegex(String texte, String regex) {
        Matcher m = Pattern.compile(regex).matcher(texte);
        return m.find() ? m.group(1) : null;
    }
    // Si y'a des codes dupliqués partout on pourra juste ramener çà ici 
}

