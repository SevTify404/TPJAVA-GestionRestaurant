/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package formulaires;



import dao.CategorieDAO;
import dao.CrudResult;
import entity.Categorie;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author rose
 */
public class GestionCategoriePanel extends JPanel {
    
    private JTable tableCategories;
    private DefaultTableModel tableModel;
    private JTextField txtLibelle;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser;
    private CategorieDAO categorieDAO;
    private int idCategorieSelectionne = -1;
    
    public GestionCategoriePanel() {
        categorieDAO = CategorieDAO.getInstance();
        initComponents();
        chargerCategories();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panneau de formulaire
        JPanel panelFormulaire = new JPanel(new GridLayout(2, 2, 5, 5));
        panelFormulaire.setBorder(BorderFactory.createTitledBorder("Ajouter / Modifier une catégorie"));
        
        panelFormulaire.add(new JLabel("Libellé:"));
        txtLibelle = new JTextField();
        panelFormulaire.add(txtLibelle);
        
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
        
        // Tableau des catégories
        String[] colonnes = {"ID", "Libellé"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableCategories = new JTable(tableModel);
        tableCategories.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableCategories.getSelectedRow();
                if (selectedRow != -1) {
                    idCategorieSelectionne = (int) tableModel.getValueAt(selectedRow, 0);
                    txtLibelle.setText((String) tableModel.getValueAt(selectedRow, 1));
                    btnModifier.setEnabled(true);
                    btnSupprimer.setEnabled(true);
                    btnAjouter.setEnabled(false);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableCategories);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Liste des catégories"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Actions des boutons
        btnAjouter.addActionListener(e -> ajouterCategorie());
        btnModifier.addActionListener(e -> modifierCategorie());
        btnSupprimer.addActionListener(e -> supprimerCategorie());
        btnActualiser.addActionListener(e -> {
            viderFormulaire();
            chargerCategories();
        });
    }
    
    private void chargerCategories() {
        tableModel.setRowCount(0);
        CrudResult<List<Categorie>> result = categorieDAO.recupererTout();
        
        if (!result.estUneErreur()) {
            for (Categorie cat : result.getDonnes()) {
                tableModel.addRow(new Object[]{
                    cat.getIDCAT(),
                    cat.getLIBELLE()
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du chargement: " + result.getErreur(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ajouterCategorie() {
        String libelle = txtLibelle.getText().trim();
        
        if (libelle.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez saisir un libellé", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Vérifier si la catégorie existe déjà
        CrudResult<List<Categorie>> result = categorieDAO.recupererTout();
        if (!result.estUneErreur()) {
            for (Categorie cat : result.getDonnes()) {
                if (cat.getLIBELLE().equalsIgnoreCase(libelle)) {
                    JOptionPane.showMessageDialog(this, 
                        "Cette catégorie existe déjà", 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        
        Categorie nouvelleCategorie = new Categorie();
        nouvelleCategorie.setLIBELLE(libelle);
        
        CrudResult<Boolean> saveResult = categorieDAO.enregistrer(nouvelleCategorie);
        
        if (!saveResult.estUneErreur() && saveResult.getDonnes()) {
            JOptionPane.showMessageDialog(this, 
                "Catégorie ajoutée avec succès", 
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            viderFormulaire();
            chargerCategories();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de l'ajout: " + saveResult.getErreur(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void modifierCategorie() {
        if (idCategorieSelectionne == -1) {
            return;
        }
        
        String nouveauLibelle = txtLibelle.getText().trim();
        
        if (nouveauLibelle.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez saisir un libellé", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Vérifier si la catégorie existe déjà
        CrudResult<List<Categorie>> result = categorieDAO.recupererTout();
        if (!result.estUneErreur()) {
            for (Categorie cat : result.getDonnes()) {
                if (cat.getIDCAT() != idCategorieSelectionne && 
                    cat.getLIBELLE().equalsIgnoreCase(nouveauLibelle)) {
                    JOptionPane.showMessageDialog(this, 
                        "Une autre catégorie avec ce libellé existe déjà", 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        
        Categorie categorieModifiee = new Categorie();
        categorieModifiee.setIDCAT(idCategorieSelectionne);
        categorieModifiee.setLIBELLE(nouveauLibelle);
        
        CrudResult<Categorie> updateResult = categorieDAO.mettreAJour(categorieModifiee);
        
        if (!updateResult.estUneErreur()) {
            JOptionPane.showMessageDialog(this, 
                "Catégorie modifiée avec succès", 
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            viderFormulaire();
            chargerCategories();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de la modification: " + updateResult.getErreur(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void supprimerCategorie() {
        if (idCategorieSelectionne == -1) {
            return;
        }
        
        int confirmation = JOptionPane.showConfirmDialog(this, 
            "Êtes-vous sûr de vouloir supprimer cette catégorie ?\n" +
            "Attention : Les produits rattachés ne seront plus disponibles.", 
            "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            Categorie categorieSupprimee = new Categorie();
            categorieSupprimee.setIDCAT(idCategorieSelectionne);
            
            CrudResult<Boolean> deleteResult = categorieDAO.suppressionLogique(categorieSupprimee);
            
            if (!deleteResult.estUneErreur() && deleteResult.getDonnes()) {
                JOptionPane.showMessageDialog(this, 
                    "Catégorie supprimée avec succès", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                viderFormulaire();
                chargerCategories();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de la suppression: " + deleteResult.getErreur(), 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viderFormulaire() {
        txtLibelle.setText("");
        idCategorieSelectionne = -1;
        tableCategories.clearSelection();
        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);
        btnAjouter.setEnabled(true);
    }
}