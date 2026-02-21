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
        
        App application = App.getInstance();
        application.lancerApplication();
        VariablesEnvirennement.checkVariablesEnvironnement();
        
}

}
    
