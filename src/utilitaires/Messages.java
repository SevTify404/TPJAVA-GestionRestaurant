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
    
    public static String messageAvecErreur(String message){
        return "Une erreur est survenue lors du traitement : \n" + message;
    }
    
    // Ainsi de suite
    
    
}
