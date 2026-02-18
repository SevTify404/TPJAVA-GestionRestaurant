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
public class FormsUtils {
    
    // Centralisation de toute les dimensions de l'application ici pour plus de
    // facilité lors des modifications
    
    protected static final int MENU_PRINCIPAL_WIDTH = 1800;
    protected static final int MENU_PRINCIPAL_HEIGHT = 1000;
    protected static final int MENU_PRINCIPAL_SIDEBAR_WIDTH = 320;
    protected static final int MENU_PRINCIPAL_TOPBAR_HEIGHT = 60;
    protected static final int LOGIN_WIDTH = 620;
    protected static final int LOGIN_HEIGHT = 520;
    
    // Tous les Jpannel devront forcément avoir cette dimension vu qu'ils seront
    // dans un conteneur qui aura exactement cette taille
    protected static Dimension JPANNEL_DIMENSION = new Dimension(
        MENU_PRINCIPAL_WIDTH - MENU_PRINCIPAL_SIDEBAR_WIDTH,
        MENU_PRINCIPAL_HEIGHT - MENU_PRINCIPAL_TOPBAR_HEIGHT
    );
    
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
