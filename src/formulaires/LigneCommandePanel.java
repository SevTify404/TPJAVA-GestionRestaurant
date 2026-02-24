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
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import utilitaires.ApplicationColors;
import utilitaires.RoundedButton;
import utilitaires.RoundedPanel;

/**
 *
 * @author sevtify
 */
public class LigneCommandePanel extends RoundedPanel {
    
    private JLabel lblQty;
    private JLabel lblSubTotal; // Le label pour Qty * PU
    private JTextField txtPrix; // Le champ pour modifier le PU
    private LigneCommande ligneCommande;
    private int quantiteActuel;
    private double prixUnitaireActuel;
    private Runnable onUpdateCallback;
    
    public LigneCommandePanel(LigneCommande laLigne, ActionListener soustraire, ActionListener incrementer, ActionListener supprimer, Runnable onUpdate) {
        super(15);
        this.ligneCommande = laLigne;
        this.prixUnitaireActuel = laLigne.getPrixUnitaire(); 
        this.quantiteActuel = laLigne.getQuantite();
        this.onUpdateCallback = onUpdate;
        
        setLayout(new BorderLayout(20, 0));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel lblNom = new JLabel(ligneCommande.getProduit().getNom());
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        controlPanel.setOpaque(false);
        
        
        lblSubTotal = new JLabel(String.format("%.0f FCFA", prixUnitaireActuel * quantiteActuel));
        
        
        txtPrix = new JTextField(String.format("%.0f", prixUnitaireActuel), 6);
        txtPrix.addActionListener(e -> updatePrix());
        
        // Boutons
        RoundedButton btnMoins = new RoundedButton("-");
        btnMoins.setBackground(ApplicationColors.PRIMARY);
        btnMoins.addActionListener(soustraire);
        
        lblQty = new JLabel(String.valueOf(quantiteActuel));
        
        RoundedButton btnPlus = new RoundedButton("+");
        btnPlus.addActionListener(incrementer);
        btnPlus.setBackground(ApplicationColors.PRIMARY);
        
        RoundedButton btnRemove = new RoundedButton("x");
        btnRemove.setBackground(ApplicationColors.ERROR);
        btnRemove.addActionListener(supprimer);

        controlPanel.add(lblSubTotal);
        controlPanel.add(txtPrix);
        controlPanel.add(btnMoins);
        controlPanel.add(lblQty);
        controlPanel.add(btnPlus);
        controlPanel.add(btnRemove);

        add(lblNom, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
    }

    private void updatePrix() {
        try {
            double nouveauPrix = Double.parseDouble(txtPrix.getText());
            if (nouveauPrix<=0) {
                txtPrix.setText(String.format("%.0f", prixUnitaireActuel));
                return;
            }
            this.prixUnitaireActuel = nouveauPrix;
            this.ligneCommande.setPrixUnitaire(nouveauPrix);
            refreshUI();
            if (onUpdateCallback != null) onUpdateCallback.run();
        } catch (NumberFormatException ex) {
            txtPrix.setText(String.format("%.0f", prixUnitaireActuel));
        }
    }

    public void refreshUI() {
        lblQty.setText(String.valueOf(quantiteActuel));
        lblSubTotal.setText(String.format("%.0f FCFA", prixUnitaireActuel * quantiteActuel));
    }

    public Boolean incrementeQuantite() {
        if (this.quantiteActuel + 1 > this.ligneCommande.getProduit().getStockActuel()) return false;
        this.quantiteActuel++;
        refreshUI();
        return true;
    }

    public Boolean decrementeQuantite() {
        if (--quantiteActuel <= 0) return false;
        refreshUI();
        return true;
    }
}
