/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package formulaires;

/**
 *
 * @author sevtify
 */
import dao.ProduitDAO;
import dao.CategorieDAO;
import entity.Produit;
import entity.Categorie;
import javax.swing.*;
import java.awt.*;
import utilitaires.AuthentificationManager;

public class GestionProduitPanel extends JPanel {
    private JTextField txtNom = new JTextField(15);
    private JTextField txtPrix = new JTextField(10);
    private JTextField txtStock = new JTextField(10);
    private JTextField txtSeuil = new JTextField(10);
    private JComboBox<Categorie> comboCat = new JComboBox<>();

    public GestionProduitPanel() {
        setLayout(new GridLayout(6, 2));
        
        // Chargement catégories
        var cats = CategorieDAO.getInstance().recupererTout();
        if(cats.estUnSucces()) {
            for(Categorie c : cats.getDonnes()) comboCat.addItem(c);
        }

        add(new JLabel("Nom:")); add(txtNom);
        add(new JLabel("Catégorie:")); add(comboCat);
        add(new JLabel("Prix:")); add(txtPrix);
        add(new JLabel("Stock:")); add(txtStock);
        add(new JLabel("Seuil Alerte:")); add(txtSeuil);
        
        JButton btnSave = new JButton("Enregistrer");
        btnSave.addActionListener(e -> enregistrer());
        add(btnSave);
    }

    private void enregistrer() {
        try {
            Produit p = new Produit();
            p.setNom(txtNom.getText());
            p.setCategorie((Categorie) comboCat.getSelectedItem());
            p.setPrixDeVente(Double.parseDouble(txtPrix.getText()));
            p.setStockActuel(Integer.parseInt(txtStock.getText()));
            p.setSeuilAlerte(Integer.parseInt(txtSeuil.getText()));
            p.setUser(AuthentificationManager.getInstance().recupererUtilisateurConnecte());
            
            var result = ProduitDAO.getInstance().enregistrer(p);
            if(result.estUnSucces()) {
                JOptionPane.showMessageDialog(this, "Produit ajouté !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur: " + result.getErreur());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des chiffres valides.");
        }
    }
}