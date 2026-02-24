/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilitaires;

/**
 *
 * @author sevtify
 */
public final class Messages {
    
    // Ici on va centraliser tous les messages qui seront utilisés dans l'application
    // et on va rendre dynamique avec String.format
    
    public final static String LOGIN_SUCCESS = "Connexion réussie !";

    public final static String LOGIN_FAILED = "Login ou mot de passe incorrect.";

    public final static String ERROR_OCCURED = "Une erreur est survenue lors "
        + "du traitement, veuillez réessayer";
    
    public final static String CANNOT_MODIFY_AUDIT = "Vous ne pouvez pas modifier un Audit";
    
    public final static String CANNOT_DELETE_AUDIT = "Vous ne pouvez pas supprimer un Audit";
    
    // ici les messages à affichés par rapport aux contaraintes bd
    // CBD=Contrainte BD
    
    public static final String CBD_LIBELLE = "Une catégorie avec ce libellé existe déjà.";
    
    public static final String CBD_NOM = "Un produit avec ce nom existe déjà.";
    
    public static final String CBD_PRODUIT_INDEX_0 = "Ce produit existe déjà dans cette catégorie.";
    
    public static final String CBD_LIGNECOMMANDE_INDEX_12 = "Ce produit est déjà présent dans cette commande.";
    
    public static final String CBD_LOGIN = "Ce login est déjà utilisé.";
    
    public static final String CBD_FK_AUDIT_USER = "Utilisateur associé à l'audit introuvable.";
    
    public static final String CBD_FK_USERS_IDUSER = "Utilisateur associé à la commande introuvable.";
    
    public static final String CBD_FK_LIGNE_COMMANDE = "Commande inexistante.";
    
    public static final String CBD_FK_LIGNE_PRODUIT = "Produit inexistant.";
    
    public static final String CBD_FK_MOUVEMENT_PRODUIT = "Produit inexistant pour le mouvement de stock.";
    
    public static final String CBD_FK_PRODUIT_CATEGORIE = "Catégorie inexistante.";
    
    public static final String CBD_FK_PRODUIT_USER = "Utilisateur associé au produit inexistant.";
    
    public static final String CBD_CHECK_TOTAL_COMMANDE = "Le total de la commande doit être supérieur ou égal à 0.";
    
    public static final String CBD_LIGNECOMMANDE_CHK_1 = "La quantité de la ligne de commande doit être supérieure à 0.";
    
    public static final String CBD_LIGNECOMMANDE_CHK_2 = "Le prix unitaire doit être supérieur à 0.";
    
    public static final String CBD_PRODUIT_CHK_1 = "Le prix de vente doit être supérieur à 0.";
    
    public static final String CBD_PRODUIT_CHK_2 = "Le stock actuel ne peut pas être négatif.";
    
    public static final String CBD_MOUVEMENTDESTOCK_CHK_1 = "La quantité du mouvement doit être supérieure à 0.";
    
    public static String messageAvecErreur(String message){
        return "Une erreur est survenue lors du traitement : \n" + message;
    }
    
    // Ainsi de suite
    
    
}
