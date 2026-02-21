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
    private final Boolean ok;

    public CrudResult(T donnes, String erreur, Boolean reussi) {
        this.donnes = donnes;
        this.erreur = erreur;
        this.ok = reussi;
    }

    public static <T> CrudResult<T> success(T donnes) {
        return new CrudResult<>(donnes, null, true);
    }

    public static <T> CrudResult<T> failure(String error) {
        return new CrudResult<>(null, error, false);
    }

    public T getDonnes() {
        return donnes;
    }

    public String getErreur() {
        return erreur;
    }

    public boolean estUnSucces() {
        return ok;
    }

    public boolean estUneErreur() {
        return !ok;
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
