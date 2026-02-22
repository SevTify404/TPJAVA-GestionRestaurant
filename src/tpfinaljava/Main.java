/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpfinaljava;
//import formulaires.App;
import formulaires.DashBoardPanel;
import java.awt.BorderLayout;
import javax.swing.JFrame;
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
        
       java.awt.EventQueue.invokeLater(() -> {
           JFrame frame = new JFrame("Test Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        frame.add(new DashBoardPanel(), BorderLayout.CENTER);

        frame.setVisible(true);
    });
    }
}
    
