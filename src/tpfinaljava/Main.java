/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpfinaljava;
import formulaires.App;
import utilitaires.VariablesEnvirennement;
import dao.MouvementDeStockDAO;
import dao.ProduitDAO;
import entity.MouvementDeStock;
import entity.Produit;
import java.time.LocalDateTime;


/**
 *
 * @author sevtify
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        

        // Je verifie si le fiechier .env existe
        VariablesEnvirennement.checkVariablesEnvironnement();
          // 🔹 1. Récupérer un produit existant
        ProduitDAO produitDAO = ProduitDAO.getInstance();
        Produit produit = produitDAO.lire(1).getDonnes(); // ID existant dans ta BD

        if (produit == null) {
            System.out.println("Produit introuvable");
            return;
        }

    // 🔹 2. Créer un mouvement
        MouvementDeStock mouvement = new MouvementDeStock();
        mouvement.setTYPE(MouvementDeStock.TypeMouvement.ENTREE);
        mouvement.setQUANTITE(5);
        mouvement.setDATEMOUVEMENT(LocalDateTime.now());
        mouvement.setMOTIF("Test depuis main");
        mouvement.setProduit(produit);

    // 🔹 3. Enregistrer
        MouvementDeStockDAO dao = MouvementDeStockDAO.getInstance();
        var resultat = dao.enregistrer(mouvement);

        if (resultat.estUnSucces()) {
            System.out.println("Mouvement enregistré avec ID : " + mouvement.getID());
        } else {
            System.out.println("Erreur : " + resultat.getErreur());
        }
        //App application = App.getInstance();
      
        //application.lancerApplication();
        
        var liste = dao.recupererTout();

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
    }

    }
}
