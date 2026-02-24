/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formulaires;

import entity.Users;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author VotreNom
 */
public class GestionProduitCategoriePanel extends JPanel {
    
    private JTabbedPane onglets;
    private GestionCategoriePanel panelCategorie;
    private GestionProduitPanel panelProduit;
    private Users utilisateurConnecte;
    
    public GestionProduitCategoriePanel(Users utilisateur) {
        this.utilisateurConnecte = utilisateur;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        onglets = new JTabbedPane();
        
        // Panneau des catégories
        panelCategorie = new GestionCategoriePanel();
        onglets.addTab("Gestion des catégories", panelCategorie);
        
        // Panneau des produits
        panelProduit = new GestionProduitPanel(utilisateurConnecte);
        onglets.addTab("Gestion des produits", panelProduit);
        
        // Ajouter un listener pour détecter le changement d'onglet
        onglets.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = onglets.getSelectedIndex();
                // Si on sélectionne l'onglet des produits (index 1)
                if (selectedIndex == 1) {
                    // Recharger les catégories dans le panneau des produits
                    panelProduit.rechargerCategories();
                }
            }
        });
        
        add(onglets, BorderLayout.CENTER);
    }
    
    // Méthode pour rafraîchir les données
    public void rafraichir() {
        // Recharger les catégories dans le panneau des produits
        panelProduit.rechargerCategories();
        
        // Recharger le panneau des catégories (optionnel)
        // Note: si vous voulez vraiment recréer complètement le panneau, 
        // décommentez les lignes ci-dessous
        /*
        panelCategorie = new GestionCategoriePanel();
        onglets.setComponentAt(0, panelCategorie);
        */
    }
}