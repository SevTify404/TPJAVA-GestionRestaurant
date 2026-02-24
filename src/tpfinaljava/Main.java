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
//import utilitaires.VariablesEnvirennement;



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

    }
}
    
