/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpfinaljava;
//import formulaires.App;
import com.formdev.flatlaf.FlatLightLaf;
import dao.UsersDAO;
import formulaires.App;

import utilitaires.VariablesEnvirennement;


/**
 *
 * @author sevtify
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        VariablesEnvirennement.checkVariablesEnvironnement();

        App application = App.getInstance();
        application.lancerApplication();
        
        /*var liste = dao.recupererTout();

        if (liste.estUnSucces()) {
            liste.getDonnes().forEach(m ->
                System.out.println("ID: " + m.getID() +
                        " | Produit: " + m.getProduit().getNom() +
                        " | Quantité: " + m.getQUANTITE())
            );
        }

    int idTest = 1; // ⚠️ Mets un ID qui existe dans ta base

    var resulta = dao.lire(idTest);

    if (resulta.estUnSucces()) {

        MouvementDeStock mv = resulta.getDonnes();

        System.out.println("=== MOUVEMENT TROUVÉ ===");
        System.out.println("ID : " + mv.getID());
        System.out.println("Type : " + mv.getTYPE());
        System.out.println("Quantité : " + mv.getQUANTITE());
        System.out.println("Date : " + mv.getDATEMOUVEMENT());
        System.out.println("Motif : " + mv.getMOTIF());

        if (mv.getProduit() != null) {
            System.out.println("Produit : " + mv.getProduit().getNom());
        }

    } else {
        System.out.println("Erreur : " + resultat.getErreur());
    }*/

    }
}
    
