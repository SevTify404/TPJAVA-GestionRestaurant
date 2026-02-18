/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpfinaljava;
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
        

        // Je verifie si le fiechier .env existe
        VariablesEnvirennement.checkVariablesEnvironnement();
        
        App application = App.getInstance();
      
        application.lancerApplication();

    }
}
