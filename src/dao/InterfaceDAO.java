/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;

/**
 *
 * @author sevtify
 * @param <TypeDesEntity>
 * Interface centralisé pour tout nos DAO
 */
public interface InterfaceDAO<TypeDesEntity> {
    // J'ai crée cette interface pour uniformisé toutes nos classes de DAo pour
    // que personne n'oublie d'implémenter une méthode specifiqze
    
    // Toutes les fonctions sui retourne des booléens doivent retourner True ssi
    // les trucs ont bien marché
    
    // Pour sauvegarder les entity comme on a fait en classe
    CrudResult<Boolean> enregistrer(TypeDesEntity objetRelieeAuDao);
    
    // Pour lire les entity à travers leur ID
    CrudResult<TypeDesEntity> lire(int id);
    
    // Pour gerer les changements
    CrudResult<TypeDesEntity> mettreAJour(TypeDesEntity entiteAMettreAJour);
    
    // Pour suppression définitive des entity
    // mais on va pas tropp utiliser çà hein
    CrudResult<Boolean> suppressionDefinitive(TypeDesEntity entiteASupprimer);
    
    // Pour suppression logique des entity
    CrudResult<Boolean> suppressionLogique(TypeDesEntity entiteASupprimer);
    
    // Pour vérifier si une entité est valide
    CrudResult<Boolean> estValide(TypeDesEntity entiteAValider);
    
    // Apres on va voir si on va ajouter d'autres trucs, pour le moment
    // c'est tout ce que j'ai en tete
    
    CrudResult<List<TypeDesEntity>> recupererTout();
    
}
