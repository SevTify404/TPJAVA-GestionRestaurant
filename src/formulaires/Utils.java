/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package formulaires;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author sevtify
 */
public class Utils {
    
    protected static void configurationDeBaseDeFenetre(JFrame laFenetre, int largeur, int longueur){
        
        // Cette fonction redimensionne et centre la fenetre par rapport à l'écran
        
        laFenetre.setSize(largeur, longueur);
        laFenetre.setResizable(false);      // On enleve la possibilité de redimenssionner
        laFenetre.setLocationRelativeTo(null);      // Ici on essaye de redimensionner
        
        // Un peu de calcul niveau CM2
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - laFenetre.getWidth()) / 2;
        int y = (screenSize.height - laFenetre.getHeight()) / 2;
            
        laFenetre.setLocation(x, y);
    
    }
    
}
