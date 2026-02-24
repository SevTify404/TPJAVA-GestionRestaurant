/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package formulaires;

/**
 *
 * @author sevtify
 */
import dao.CategorieDAO;
import entity.Categorie;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GestionCategoriePanel extends JPanel {
    private JTextField txtLibelle = new JTextField(20);
    private JList<Categorie> listeCategories = new JList<>();
    private DefaultListModel<Categorie> model = new DefaultListModel<>();

    public GestionCategoriePanel() {
        setLayout(new BorderLayout());
        JPanel panelForm = new JPanel();
        panelForm.add(new JLabel("Libellé :"));
        panelForm.add(txtLibelle);
        
        JButton btnAdd = new JButton("Ajouter");
        btnAdd.addActionListener(e -> ajouter());
        panelForm.add(btnAdd);

        listeCategories.setModel(model);
        chargerDonnees();
        
        add(panelForm, BorderLayout.NORTH);
        add(new JScrollPane(listeCategories), BorderLayout.CENTER);
    }

    private void chargerDonnees() {
        model.clear();
        var result = CategorieDAO.getInstance().recupererTout();
        if (result.estUnSucces()) {
            for (Categorie c : result.getDonnes()) model.addElement(c);
        }
    }

    private void ajouter() {
        String libelle = txtLibelle.getText();
        if (libelle.isEmpty()) return;
        
        // Vérification unicité simple
        for(int i=0; i<model.size(); i++) {
            if(model.get(i).getLIBELLE().equalsIgnoreCase(libelle)) {
                JOptionPane.showMessageDialog(this, "Cette catégorie existe déjà.");
                return;
            }
        }
        
        Categorie c = new Categorie();
        c.setLIBELLE(libelle);
        CategorieDAO.getInstance().enregistrer(c);
        txtLibelle.setText("");
        chargerDonnees();
    }
}
