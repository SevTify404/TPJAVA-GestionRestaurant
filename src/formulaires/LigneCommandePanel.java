/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package formulaires;

import entity.LigneCommande;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utilitaires.ApplicationColors;
import utilitaires.RoundedButton;
import utilitaires.RoundedPanel;

/**
 *
 * @author sevtify
 */
public class LigneCommandePanel extends RoundedPanel {
    
    // J'ai trop galéré sur ce truc
    private JLabel lblQty;
    private JLabel lblSubTotal;
    LigneCommande ligneCommande;
    private int  quantiteActuel;
    private double prixActuel;
   
    
    public LigneCommandePanel(LigneCommande laLigne, ActionListener soustraire, ActionListener incrementer, ActionListener supprimer) {
        super(15); // Coins arrondis de 15px
        this.ligneCommande = laLigne;
        this.prixActuel = laLigne.getProduit().getPrixDeVente();
        this.quantiteActuel = laLigne.getQuantite();
        
        setLayout(new BorderLayout(10, 0));
        setBackground(Color.WHITE); // Fond blanc pour chaque ligne
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        // Nom du produit
        JLabel lblNom = new JLabel(ligneCommande.getProduit().getNom());
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Panneau de contrôle (Qty + Boutons)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        controlPanel.setOpaque(false);
        
        // Sous-total
        lblSubTotal = new JLabel(String.format("%.0f FCFA", prixActuel));
        
        // Boutons - et +
        RoundedButton btnMoins = new RoundedButton("-");
        btnMoins.addActionListener(e -> {
            soustraire.actionPerformed(e);
        });
        
        lblQty = new JLabel(String.valueOf(ligneCommande.getQuantite()));
        RoundedButton btnPlus = new RoundedButton("+");
        btnPlus.addActionListener(e -> {
            incrementer.actionPerformed(e);
        });
        
        RoundedButton btnRemove = new RoundedButton("x");
        btnRemove.setBackground(ApplicationColors.ERROR); // Rouge pour le bouton remove
        btnRemove.addActionListener(e -> {
            supprimer.actionPerformed(e);
        });

        controlPanel.add(lblSubTotal);
        controlPanel.add(btnMoins);
        controlPanel.add(lblQty);
        controlPanel.add(btnPlus);
        controlPanel.add(btnRemove);

        add(lblNom, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        
        // Empêcher la ligne de s'étirer verticalement
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
    }
    

    

    
    public Boolean incrementeQuantite(){
    if (this.quantiteActuel + 1 > this.ligneCommande.getProduit().getStockActuel()) {
        return false;
    }
    
    this.quantiteActuel++;
    
    
    lblQty.setText(String.valueOf(quantiteActuel));
    lblSubTotal.setText(String.format("%.0f FCFA", prixActuel * quantiteActuel));
    revalidate();
    return true;
        
    }
    
    public Boolean decrementeQuantite(){
        
        if (--quantiteActuel <= 0) {
            return false;
        }
        
        lblQty.setText(String.valueOf(quantiteActuel));
        lblSubTotal.setText(String.format("%.0f FCFA", prixActuel * quantiteActuel));
        revalidate();
        return true;
        
    }
    
}
