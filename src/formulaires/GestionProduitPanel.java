/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formulaires;

import dao.CategorieDAO;
import dao.ProduitDAO;
import dao.CrudResult;
import entity.Categorie;
import entity.Produit;
import entity.Users;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VotreNom
 */
public class GestionProduitPanel extends JPanel {
    
    private JTable tableProduits;
    private DefaultTableModel tableModel;
    private JTextField txtNom, txtPrix, txtStock, txtSeuil;
    private JComboBox<CategorieItem> cmbCategorie;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser;
    private ProduitDAO produitDAO;
    private CategorieDAO categorieDAO;
    private int idProduitSelectionne = -1;
    private Users utilisateurConnecte;
    public void rechargerCategories() {
        chargerCategories();
    }
    
    // Classe interne pour afficher les catégories dans la comboBox
    private class CategorieItem {
        private Categorie categorie;
        
        public CategorieItem(Categorie categorie) {
            this.categorie = categorie;
        }
        
        public Categorie getCategorie() {
            return categorie;
        }
        
        @Override
        public String toString() {
            return categorie.getLIBELLE();
        }
    }
    
    public GestionProduitPanel(Users utilisateur) {
        this.utilisateurConnecte = utilisateur;
        produitDAO = ProduitDAO.getInstance();
        categorieDAO = CategorieDAO.getInstance();
        initComponents();
        chargerCategories(); // Charger d'abord les catégories
        chargerProduits();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panneau de formulaire
        JPanel panelFormulaire = new JPanel(new GridLayout(6, 2, 5, 5));
        panelFormulaire.setBorder(BorderFactory.createTitledBorder("Ajouter / Modifier un produit"));
        
        panelFormulaire.add(new JLabel("Nom:"));
        txtNom = new JTextField();
        panelFormulaire.add(txtNom);
        
        panelFormulaire.add(new JLabel("Catégorie:"));
        cmbCategorie = new JComboBox<>();
        panelFormulaire.add(cmbCategorie);
        
        panelFormulaire.add(new JLabel("Prix de vente:"));
        txtPrix = new JTextField();
        panelFormulaire.add(txtPrix);
        
        panelFormulaire.add(new JLabel("Stock actuel:"));
        txtStock = new JTextField();
        panelFormulaire.add(txtStock);
        
        panelFormulaire.add(new JLabel("Seuil d'alerte:"));
        txtSeuil = new JTextField();
        panelFormulaire.add(txtSeuil);
        
        // Panneau des boutons
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAjouter = new JButton("Ajouter");
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");
        btnActualiser = new JButton("Actualiser");
        
        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);
        
        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnActualiser);
        
        panelFormulaire.add(panelBoutons);
        
        add(panelFormulaire, BorderLayout.NORTH);
        
        // Tableau des produits
        String[] colonnes = {"ID", "Nom", "Catégorie", "Prix", "Stock", "Seuil d'alerte"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableProduits = new JTable(tableModel);
        tableProduits.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableProduits.getSelectedRow();
                if (selectedRow != -1) {
                    idProduitSelectionne = (int) tableModel.getValueAt(selectedRow, 0);
                    remplirFormulaire(selectedRow);
                    btnModifier.setEnabled(true);
                    btnSupprimer.setEnabled(true);
                    btnAjouter.setEnabled(false);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableProduits);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des produits"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Actions des boutons
        btnAjouter.addActionListener(e -> ajouterProduit());
        btnModifier.addActionListener(e -> modifierProduit());
        btnSupprimer.addActionListener(e -> supprimerProduit());
        btnActualiser.addActionListener(e -> {
            viderFormulaire();
            chargerCategories(); // Recharger les catégories d'abord
            chargerProduits();
        });
    }
    
    /**
     * Charge les catégories depuis la base de données et met à jour la comboBox
     */
    public void chargerCategories() {
        // Sauvegarder la sélection actuelle si possible
        String ancienneSelection = null;
        CategorieItem selectedItem = (CategorieItem) cmbCategorie.getSelectedItem();
        if (selectedItem != null) {
            ancienneSelection = selectedItem.getCategorie().getLIBELLE();
        }
        
        cmbCategorie.removeAllItems();
        CrudResult<List<Categorie>> result = categorieDAO.recupererTout();
        
        if (!result.estUneErreur()) {
            List<Categorie> categories = result.getDonnes();
            
            if (categories.isEmpty()) {
                // Ajouter un élément par défaut si aucune catégorie
                cmbCategorie.addItem(new CategorieItem(new Categorie(0, "Aucune catégorie")));
                cmbCategorie.setEnabled(false);
                JOptionPane.showMessageDialog(this, 
                    "Veuillez d'abord créer des catégories", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                cmbCategorie.setEnabled(true);
                for (Categorie cat : categories) {
                    cmbCategorie.addItem(new CategorieItem(cat));
                }
                
                // Restaurer la sélection précédente si possible
                if (ancienneSelection != null) {
                    for (int i = 0; i < cmbCategorie.getItemCount(); i++) {
                        CategorieItem item = cmbCategorie.getItemAt(i);
                        if (item.getCategorie().getLIBELLE().equals(ancienneSelection)) {
                            cmbCategorie.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement des catégories: " + result.getErreur(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void chargerProduits() {
        tableModel.setRowCount(0);
        CrudResult<List<Produit>> result = produitDAO.recupererTout();
        
        if (!result.estUneErreur()) {
            for (Produit p : result.getDonnes()) {
                tableModel.addRow(new Object[]{
                    p.getIdProduit(),
                    p.getNom(),
                    p.getCategorie().getLIBELLE(),
                    String.format("%.2f", p.getPrixDeVente()),
                    p.getStockActuel(),
                    p.getSeuilAlerte()
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement: " + result.getErreur(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void remplirFormulaire(int row) {
        try {
            txtNom.setText((String) tableModel.getValueAt(row, 1));
            
            // Trouver la catégorie dans la comboBox
            String categorieNom = (String) tableModel.getValueAt(row, 2);
            boolean categorieTrouvee = false;
            
            for (int i = 0; i < cmbCategorie.getItemCount(); i++) {
                CategorieItem item = cmbCategorie.getItemAt(i);
                if (item.getCategorie().getLIBELLE().equals(categorieNom)) {
                    cmbCategorie.setSelectedIndex(i);
                    categorieTrouvee = true;
                    break;
                }
            }
            
            // Si la catégorie n'est pas trouvée (peut-être supprimée), sélectionner la première
            if (!categorieTrouvee && cmbCategorie.getItemCount() > 0) {
                cmbCategorie.setSelectedIndex(0);
            }
            
            txtPrix.setText(tableModel.getValueAt(row, 3).toString().replace(",", "."));
            txtStock.setText(tableModel.getValueAt(row, 4).toString());
            txtSeuil.setText(tableModel.getValueAt(row, 5).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void ajouterProduit() {
        if (!validerFormulaire()) {
            return;
        }
        
        try {
            CategorieItem selectedItem = (CategorieItem) cmbCategorie.getSelectedItem();
            if (selectedItem == null || selectedItem.getCategorie().getIDCAT() == 0) {
                JOptionPane.showMessageDialog(this, 
                    "Veuillez sélectionner une catégorie valide", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Produit nouveauProduit = new Produit();
            nouveauProduit.setNom(txtNom.getText().trim());
            nouveauProduit.setCategorie(selectedItem.getCategorie());
            nouveauProduit.setUser(utilisateurConnecte);
            nouveauProduit.setPrixDeVente(Double.parseDouble(txtPrix.getText().trim().replace(",", ".")));
            nouveauProduit.setStockActuel(Integer.parseInt(txtStock.getText().trim()));
            nouveauProduit.setSeuilAlerte(Integer.parseInt(txtSeuil.getText().trim()));
            
            CrudResult<Boolean> saveResult = produitDAO.enregistrer(nouveauProduit);
            
            if (!saveResult.estUneErreur() && saveResult.getDonnes()) {
                JOptionPane.showMessageDialog(this, 
                    "Produit ajouté avec succès", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                viderFormulaire();
                chargerProduits();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de l'ajout: " + saveResult.getErreur(), 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez saisir des nombres valides", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void modifierProduit() {
        if (idProduitSelectionne == -1) {
            return;
        }
        
        if (!validerFormulaire()) {
            return;
        }
        
        try {
            CategorieItem selectedItem = (CategorieItem) cmbCategorie.getSelectedItem();
            if (selectedItem == null || selectedItem.getCategorie().getIDCAT() == 0) {
                JOptionPane.showMessageDialog(this, 
                    "Veuillez sélectionner une catégorie valide", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Produit produitModifie = new Produit();
            produitModifie.setIdProduit(idProduitSelectionne);
            produitModifie.setNom(txtNom.getText().trim());
            produitModifie.setCategorie(selectedItem.getCategorie());
            produitModifie.setUser(utilisateurConnecte);
            produitModifie.setPrixDeVente(Double.parseDouble(txtPrix.getText().trim().replace(",", ".")));
            produitModifie.setStockActuel(Integer.parseInt(txtStock.getText().trim()));
            produitModifie.setSeuilAlerte(Integer.parseInt(txtSeuil.getText().trim()));
            
            CrudResult<Produit> updateResult = produitDAO.mettreAJour(produitModifie);
            
            if (!updateResult.estUneErreur()) {
                JOptionPane.showMessageDialog(this, 
                    "Produit modifié avec succès", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                viderFormulaire();
                chargerProduits();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de la modification: " + updateResult.getErreur(), 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez saisir des nombres valides", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void supprimerProduit() {
        if (idProduitSelectionne == -1) {
            return;
        }
        
        int confirmation = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir supprimer ce produit ?\n" +
            "Attention : Cette action peut affecter les commandes existantes.", 
            "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            Produit produitSupprime = new Produit();
            produitSupprime.setIdProduit(idProduitSelectionne);
            
            CrudResult<Boolean> deleteResult = produitDAO.suppressionLogique(produitSupprime);
            
            if (!deleteResult.estUneErreur() && deleteResult.getDonnes()) {
                JOptionPane.showMessageDialog(this, 
                    "Produit supprimé avec succès", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                viderFormulaire();
                chargerProduits();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de la suppression: " + deleteResult.getErreur(), 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean validerFormulaire() {
        if (txtNom.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez saisir un nom", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            double prix = Double.parseDouble(txtPrix.getText().trim().replace(",", "."));
            if (prix <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Le prix doit être strictement positif", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Prix invalide", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            int stock = Integer.parseInt(txtStock.getText().trim());
            if (stock < 0) {
                JOptionPane.showMessageDialog(this, 
                    "Le stock ne peut pas être négatif", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Stock invalide", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            int seuil = Integer.parseInt(txtSeuil.getText().trim());
            if (seuil < 0) {
                JOptionPane.showMessageDialog(this, 
                    "Le seuil d'alerte ne peut pas être négatif", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Seuil d'alerte invalide", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void viderFormulaire() {
        txtNom.setText("");
        txtPrix.setText("");
        txtStock.setText("");
        txtSeuil.setText("");
        idProduitSelectionne = -1;
        tableProduits.clearSelection();
        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);
        btnAjouter.setEnabled(true);
    }
    
    /**
     * Méthode publique pour forcer le rechargement des catégories
     * Utile quand on revient à cet onglet après avoir modifié les catégories
     */
    
}