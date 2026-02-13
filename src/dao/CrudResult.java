/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author sevtify
 * @param <T>
 */
public class CrudResult<T> {
    
    private final T donnes;
    private final String erreur;

    public CrudResult(T donnes, String erreur) {
        this.donnes = donnes;
        this.erreur = erreur;
    }

    public static <T> CrudResult<T> success(T donnes) {
        return new CrudResult<>(donnes, null);
    }

    public static <T> CrudResult<T> failure(String error) {
        return new CrudResult<>(null, error);
    }

    public T getDonnes() {
        return donnes;
    }

    public String getErreur() {
        return erreur;
    }

    public boolean estUnSucces() {
        return erreur == null;
    }

    public boolean estUneErreur() {
        return erreur != null;
    }
    
    @Override
    public String toString(){
        if (estUnSucces()) {
            return "Succes : " + donnes.toString();
        } else {
            return "Erreur : " + erreur;
        }
    }
}
