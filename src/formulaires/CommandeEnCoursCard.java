/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package formulaires;

import entity.Commande;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 *
 * @author sevtify
 */
public class CommandeEnCoursCard extends JPanel {
    public CommandeEnCoursCard(Commande cmd, Runnable onContinuer, Runnable onSupprimer) {
        setMaximumSize(new Dimension(Short.MAX_VALUE, 70));
        setPreferredSize(new Dimension(500, 70));
        setLayout(new BorderLayout(15, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // Info : Heure et détails
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);
        infoPanel.add(new JLabel("Commande du : " + cmd.getDateCommande().toString())); // Ajuste le format date
        infoPanel.add(new JLabel("Articles : " + cmd.getLigneCommnandes().size() + " | Total : " + cmd.getTotal() + " FCFA"));

        // Boutons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        btnPanel.setOpaque(false);
        
        JButton btnContinuer = new JButton("Continuer");
        btnContinuer.addActionListener(e -> onContinuer.run());
        
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBackground(new Color(231, 76, 60)); // Rouge
        btnSupprimer.setForeground(Color.WHITE);
        btnSupprimer.addActionListener(e -> onSupprimer.run());

        btnPanel.add(btnContinuer);
        btnPanel.add(btnSupprimer);

        add(infoPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.EAST);
        
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
    }
}
