/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpfinaljava;

import dao.CommandeDAO;
import dao.CrudResult;
import entity.Commande;
import java.time.LocalDateTime;


import dao.AuditDAO;
import dao.CrudResult;
import entity.Audit;

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
<<<<<<< Upstream, based on origin/main
        CrudResult<List<Audit>> ee = AuditDAO.getInstance().recupererTout();
        System.out.println(ee);
        if (ee.estUnSucces()) {
            System.out.println(ee.getDonnes());
        }
=======
        System.out.println("===== TEST INSERT COMMANDE =====");

    Commande com = new Commande();

    // ❗ NE PAS METTRE L’ID SI AUTO_INCREMENT

    com.setDateCommande(LocalDateTime.now());

    // 🔴 À ADAPTER SELON TA TABLE
    com.setIdCommande(1);
    com.setDateCommande(LocalDateTime.now());

    CrudResult<Boolean> res =
            CommandeDAO.getInstance().enregistrer(com);

    if (res.estUnSucces()) {
        System.out.println("Insertion réussie");
        System.out.println("ID généré : " + com.getIdCommande());
    } else {
        System.out.println("Erreur : " + res.getErreur());
    }
>>>>>>> 07a0b97 LigneCommandeDAO
    }
    
}
