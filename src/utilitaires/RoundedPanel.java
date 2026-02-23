/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilitaires;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

/**
 *
 * @author sevtify
 */
public class RoundedPanel extends JPanel {
    private int radius = 20; // Rayon par défaut

    public RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false); // Indispensable pour voir l'arrondi
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        // Active l'antialiasing pour des bords lisses (pas pixélisés)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Remplit le fond avec la couleur du panneau
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
        
        g2.dispose();
        super.paintComponent(g);
    }
}