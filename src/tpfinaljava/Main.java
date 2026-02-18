/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpfinaljava;

<<<<<<< HEAD:src/tpfinaljava/Main.java
import formulaires.App;
import utilitaires.VariablesEnvirennement;
=======
import dao.AuditDAO;



import dao.CrudResult;
import entity.Audit;
import java.util.List;
>>>>>>> main:src/tpfinaljava/TPFinalJava.java


/**
 *
 * @author sevtify
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
<<<<<<< HEAD:src/tpfinaljava/Main.java
        
        // Je verifie si le fiechier .env existe
        VariablesEnvirennement.checkVariablesEnvironnement();
        
        App application = App.getInstance();
        
        application.lancerApplication();
    }
=======
        CrudResult<List<Audit>> ee = AuditDAO.getInstance().recupererTout();
        System.out.println(ee);
        if (ee.estUnSucces()) {
            System.out.println(ee.getDonnes());
        }
       
    }
    
    
    
>>>>>>> main:src/tpfinaljava/TPFinalJava.java
}
