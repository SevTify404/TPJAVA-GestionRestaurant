/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilitaires;

/**
 *
 * @author sevtify
 */
public class Validateurs {
    
    public static boolean validerEntier(int entier){
        // Retourne vrai si l'entier est supérieur à 0
        return entier > 0;
    }
    
    public static boolean validerString(String chaineDeCaracteres){
        // Retourne vrai si l'entier n'est pas vide
        return ! chaineDeCaracteres.isBlank() ;
    }
    
}
