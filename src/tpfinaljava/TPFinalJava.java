/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpfinaljava;


//import dao.AuditDAO;
import dao.CrudResult;
//import entity.Audit;
//import entity.Categorie;
//import dao.CategorieDAO;
import dao.mouvementdestockDAO;
import entity.mouvementdestock;

import java.time.LocalDateTime;
        
import java.util.List;

/**
 *
 * @author sevtify
 */
public class TPFinalJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       /*CategorieDAO dao = new CategorieDAO();

        // =====================
        // INSERTION
        // =====================

        Categorie cat = new Categorie();
        cat.setLIBELLE("desert");

        CrudResult<Categorie> insertResult = dao.enregistrer(cat);

        System.out.println("=== INSERTION ===");
        System.out.println(insertResult);

        if (insertResult.estUneErreur()) return;

        int id = insertResult.getDonnes().getIDCAT();

        // =====================
        // LECTURE
        // =====================

        CrudResult<Categorie> lireResult = dao.lire(id);

        System.out.println("\n=== LECTURE ===");
        System.out.println(lireResult);

        // =====================
        // MISE A JOUR
        // =====================

        Categorie maj = lireResult.getDonnes();
        maj.setLIBELLE("desert alcoolisées");

        CrudResult<Categorie> majResult = dao.mettreAJour(maj);

        System.out.println("\n=== MISE A JOUR ===");
        System.out.println(majResult);

        // =====================
        // LISTE COMPLETE
        // =====================

        CrudResult<List<Categorie>> liste = dao.recupererTout();

        System.out.println("\n=== LISTE ===");

        if (liste.estUnSucces()) {
            for (Categorie c : liste.getDonnes()) {
                System.out.println(c);
            }
        }

        // =====================
        // SUPPRESSION LOGIQUE
        // =====================
        
        CrudResult<Boolean> suppr = dao.suppressionLogique(maj);

        System.out.println("\n=== SUPPRESSION LOGIQUE ===");
        System.out.println(suppr);*/
       mouvementdestockDAO dao = new mouvementdestockDAO();

        // =====================
        // TEST INSERTION
        // =====================

        mouvementdestock mv = new mouvementdestock();
        mv.setTYPE(mouvementdestock.TypeMouvement.ENTREE);
        mv.setQUANTITE(10);
        mv.setDATEMOUVEMENT(LocalDateTime.now());
        mv.setIDPRODUIT(3);
        mv.setMOTIF("Test insertion");

        CrudResult<mouvementdestock> insertResult = dao.enregistrer(mv);

        System.out.println("=== INSERTION ===");
        System.out.println(insertResult);

        if (insertResult.estUneErreur()) return;

        int id = insertResult.getDonnes().getID();

        // =====================
        // TEST LECTURE
        // =====================

        CrudResult<mouvementdestock> lireResult = dao.lire(id);

        System.out.println("\n=== LECTURE ===");
        System.out.println(lireResult);

        // =====================
        // TEST MISE A JOUR
        // =====================

        mouvementdestock mvMaj = lireResult.getDonnes();
        mvMaj.setQUANTITE(50);

        CrudResult<mouvementdestock> majResult = dao.mettreAJour(mvMaj);

        System.out.println("\n=== MISE A JOUR ===");
        System.out.println(majResult);

        // =====================
        // TEST RECUPERER TOUT
        // =====================

        CrudResult<List<mouvementdestock>> listeResult = dao.recupererTout();

        System.out.println("\n=== LISTE COMPLETE ===");

        if (listeResult.estUnSucces()) {
            for (mouvementdestock m : listeResult.getDonnes()) {
                System.out.println(m);
            }
        }

        // =====================
        // TEST SUPPRESSION LOGIQUE
        // =====================

        CrudResult<Boolean> supprResult = dao.suppressionLogique(mvMaj);

        System.out.println("\n=== SUPPRESSION LOGIQUE ===");
        System.out.println(supprResult);
    
        //CrudResult<List<Audit>> ee = AuditDAO.getInstance().recupererTout();
        //System.out.println(ee);
        //if (ee.estUnSucces()) {
          //  System.out.println(ee.getDonnes());
        //}
    
    }
}
