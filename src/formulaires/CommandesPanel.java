/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formulaires;
import dao.CommandeDAO;
import dao.CrudResult;
import dao.LigneCommandeDAO;
import dao.ProduitDAO;
import entity.Commande;
import entity.LigneCommande;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import entity.Produit;
import entity.enums.ActionType;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import utilitaires.ApplicationColors;
import utilitaires.AuthentificationManager;
/**
/**
 *
 * @author sevtify
 */
public class CommandesPanel extends javax.swing.JPanel {

    /**
     * Creates new form CommandesPanel
     */
    
    // Si il y'a le temps apres je vais refactor ce gros spaghetti
    // TODO: refactor spaghetti au niveau de commande
    
    private Map<Integer, LigneCommande> lignesActuelles = new HashMap<>();
    private Map<Integer, LigneCommandePanel> pannelLignesActuelles = new HashMap<>();
    private List<Produit> tousLesProduits;
    
    private int totalPrixCommandeActuel, nombreTotalDeProduit;
    
    public CommandesPanel() {
        initComponents();
        initCustomComponents();
    }
    
    private void initCustomComponents(){
        jspCartesProduits.getVerticalScrollBar().setUnitIncrement(16);
        jspLigneCommandes.getVerticalScrollBar().setUnitIncrement(16);
        jspCommandesBrouillons.getVerticalScrollBar().setUnitIncrement(16);
        mettreListeProduitAJour();
        jdCommandesEnAttente.setSize(600, 500);
        jdCommandesEnAttente.setLocationRelativeTo(this);
        ajouterEcouteurs();
        jtfRechercheProduit.requestFocus();
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                
                mettreListeProduitAJour();
            }
        });
        
    }
    
    private void mettreAjourTotalProduit(){
        jlNombreProduit.setText("Nombre de Produits : " + nombreTotalDeProduit);
    }
    
    private void mettreAJourTotalPrixCommande(){
        jlTotal.setText("Total : " + totalPrixCommandeActuel + " FCFA");
    }
    
    private void ajouterEcouteurs(){
        jtfRechercheProduit.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrerProduits(jtfRechercheProduit.getText());
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrerProduits(jtfRechercheProduit.getText());
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrerProduits(jtfRechercheProduit.getText());
            }
        });
    }
    
    private void truncateLePanelDeCommande(){
        lignesActuelles.clear();
        pannelLignesActuelles.clear();
        jpLigneCommandes.removeAll();
        jpLigneCommandes.revalidate();
        jpLigneCommandes.repaint();
        calculerTotalGlobal();    
    }
    
    private void filtrerProduits(String texteRecherche) {
        
        if (texteRecherche.isBlank()) {
            afficherProduits(tousLesProduits);
        }
        
        List<Produit> produitsFiltres = tousLesProduits.stream()
            .filter(p -> p.getNom().toLowerCase().contains(texteRecherche.toLowerCase()))
            .toList();

        
        afficherProduits(produitsFiltres);
    }
    
    
    private void mettreListeProduitAJour(){
        CrudResult<List<Produit>> produits = ProduitDAO.getInstance().recupererToutDisponible();
        
        if (produits.estUneErreur()) {
            showCustomErreurJOptionPane("Erreur lors du chargement des produit : " + produits.getErreur());            
        }
        
        tousLesProduits = produits.getDonnes();
        
        afficherProduits(tousLesProduits);
    
    }
    
    private void showCustomErreurJOptionPane(String message){
        JOptionPane.showMessageDialog(this, message, "Erreur Commandes", JOptionPane.ERROR_MESSAGE, new ImageIcon(ProductCard.class.getResource("/images/fast-food.png")));
    }
    
    private void showCustomInfoJOptionPane(String message){
        JOptionPane.showMessageDialog(this, message, "Infos Commandes", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(ProductCard.class.getResource("/images/fast-food.png")));
    }
    
    private void afficherProduits(List<Produit> produits) {
        jpCartesProduits.removeAll();


        for (Produit p : produits) {
            ProductCard card = new ProductCard(
                p,
                e -> {
                    ajouterAuPannelCommande(p);
                }
            );  
            jpCartesProduits.add(card);
        }

        jpCartesProduits.revalidate();
        jpCartesProduits.repaint();
    }
    
    private void ajouterAuPannelCommande(Produit leProduit){
        if (leProduit.getStockActuel() == 0) {
            showCustomErreurJOptionPane("Stock Insuffisant pour un ajout");
            return;
        }
        System.out.println("On arrive ici");
        if (lignesActuelles.containsKey(leProduit.getIdProduit())) {
            incrementerChoix(leProduit.getIdProduit());
        }else{
            LigneCommande nouvelleLigne = new LigneCommande(0, 1, leProduit.getPrixDeVente(), null, leProduit, LocalDateTime.now());
            
            ajouterLigne(nouvelleLigne);
        }
    
    
    }
    
    public void incrementerChoix(int idProduit){
        if (!pannelLignesActuelles.get(idProduit).incrementeQuantite()) {
            showCustomErreurJOptionPane("Stock Insuffisant");
        }else {
            lignesActuelles.get(idProduit).setQuantite( lignesActuelles.get(idProduit).getQuantite() + 1);
        }
        calculerTotalGlobal();
        
    }
    
    public void decrementerChoix(int idProduit){
        if (!pannelLignesActuelles.get(idProduit).decrementeQuantite()) {
            supprimerChoix(idProduit);
            
        }else{
            lignesActuelles.get(idProduit).setQuantite( lignesActuelles.get(idProduit).getQuantite() - 1);
        }
        
        calculerTotalGlobal();
        
    }
    
    public void supprimerChoix(int idProduit){
    
            LigneCommandePanel panel = pannelLignesActuelles.get(idProduit);
        if (panel != null) {
            jpLigneCommandes.remove(panel);

            jpLigneCommandes.revalidate();
            jpLigneCommandes.repaint();

            lignesActuelles.remove(idProduit);
            pannelLignesActuelles.remove(idProduit);
        
            calculerTotalGlobal(); // N'oublie pas de recalculer le total !
        }
    }
    
    private void calculerTotalGlobal() {
        double total = 0;
        int nombreTotalProduits = 0;

        // Parcourt les lignes de commande stockées dans ta Map
        for (LigneCommande ligne : lignesActuelles.values()) {
            total += (ligne.getPrixUnitaire()* ligne.getQuantite());
            nombreTotalProduits += ligne.getQuantite();
        }

        // Mise à jour de tes variables d'instance
        this.totalPrixCommandeActuel = (int) total;
        this.nombreTotalDeProduit = nombreTotalProduits;


        mettreAJourTotalPrixCommande();
        mettreAjourTotalProduit();
    }
    
    
    
        private void mettreAJourLignePanelCommande(LigneCommande ligneCommande, String operation){
        int idProduit = ligneCommande.getProduit().getIdProduit();
        if (operation.equals("+")) {
            incrementerChoix(idProduit);
        } else if (operation.equals("-")){
            decrementerChoix(idProduit);
        }else{
            supprimerChoix(idProduit);
        }
    
    }
    
    public void ajouterLigne(LigneCommande ligneCommande) {
        LigneCommandePanel ligne = new LigneCommandePanel(
            ligneCommande,
            a -> {
                mettreAJourLignePanelCommande(ligneCommande, "-");
            },
            b -> {
                mettreAJourLignePanelCommande(ligneCommande, "+");
            },
            c -> {
                mettreAJourLignePanelCommande(ligneCommande, "jeSaisPlusQuoiMettreTchieeee");
            },
            () -> calculerTotalGlobal()
            
        );
        
        // Huuuuuuummmmmmmmmmmmmm
        lignesActuelles.put(ligneCommande.getProduit().getIdProduit(), ligneCommande);
        
        pannelLignesActuelles.put(ligneCommande.getProduit().getIdProduit(), ligne);
        
        calculerTotalGlobal();

        // On fixe la taille max pour éviter qu'elle s'étire en hauteur
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        jpLigneCommandes.add(ligne);
        jpLigneCommandes.add(Box.createRigidArea(new Dimension(0, 5))); // Petit espace entre lignes

        jpLigneCommandes.revalidate();
        jpLigneCommandes.repaint();
    }
    
    private void chargerCommandesEnAttente() {
        jpConteneurCommandeBrouillon.removeAll();

    // Récupération des commandes via ton DAO
    CrudResult<List<Commande>> enAttente = CommandeDAO.getInstance().recupererCommandeEnCoursUsers(AuthentificationManager.getInstance().recupererUtilisateurConnecte());
        if (enAttente.estUneErreur()) {
            showCustomErreurJOptionPane(enAttente.getErreur());
            return;
        }
        
        List<Commande> listeCommandes = enAttente.getDonnes();
        
        for (Commande c : listeCommandes) {
            List<LigneCommande> lignes = CommandeDAO.getInstance().recupererLignes(c.getIdCommande()).getDonnes();
            
            if (lignes == null || lignes.isEmpty()) {
                continue;
            } else {
                c.setLigneCommnandes(lignes);
            }
        }
        
        
        for (Commande cmd : listeCommandes) {
            CommandeEnCoursCard card = new CommandeEnCoursCard(
                cmd,
                () -> {
                    jdCommandesEnAttente.dispose(); // Ferme le dialog
                    chargerCommande(cmd);
                    CommandeDAO.getInstance().suppressionLogique(cmd);
                },
                () -> {
                    if (JOptionPane.showConfirmDialog(this, "Supprimer ce brouillon ?") == 0) {
                        CommandeDAO.getInstance().suppressionLogique(cmd);
                        chargerCommandesEnAttente(); // Rafraîchit la liste
                    }
                }
            );
            jpConteneurCommandeBrouillon.add(card);
            jpConteneurCommandeBrouillon.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        jpConteneurCommandeBrouillon.revalidate();
        jpConteneurCommandeBrouillon.repaint();
    }
    
    public void chargerCommande(Commande cmd) {
        // 1. Nettoyage complet de l'interface actuelle
        truncateLePanelDeCommande();

        // 2. Reconstruction à partir de la commande chargée
        // On suppose que ta classe Commande a une méthode getLignes() qui renvoie List<LigneCommande>
        for (LigneCommande ligne : cmd.getLigneCommnandes()) {
            ajouterLigne(ligne); 
        }

        
        calculerTotalGlobal();

        
        jpLigneCommandes.revalidate();
        jpLigneCommandes.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jdCommandesEnAttente = new javax.swing.JDialog();
        jpDialogConteneur = new javax.swing.JPanel();
        jpDialogTopbar = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jpCommandeBruillons = new javax.swing.JPanel();
        jspCommandesBrouillons = new javax.swing.JScrollPane();
        jpConteneurCommandeBrouillon = new javax.swing.JPanel();
        jpTopbarCommandes = new javax.swing.JPanel();
        jlLabelDuPanneau = new javax.swing.JLabel();
        jbAncienneCommande = new javax.swing.JButton();
        jpContenuPrincipalCommandes = new javax.swing.JPanel();
        jpConteneurCatalogueProduit = new javax.swing.JPanel();
        jpCatalogueProduit = new javax.swing.JPanel();
        jpTitreCatalogue = new javax.swing.JPanel();
        jlProduitsDispo = new javax.swing.JLabel();
        jpRechercheProduit = new javax.swing.JPanel();
        jtfRechercheProduit = new javax.swing.JTextField();
        jlLogoRecherche = new javax.swing.JLabel();
        jpConteneurCartesProduits = new javax.swing.JPanel();
        jspCartesProduits = new javax.swing.JScrollPane();
        jpCartesProduits = new javax.swing.JPanel();
        jpConteneurCommandeEnCours = new javax.swing.JPanel();
        jpCommandeEnCours = new javax.swing.JPanel();
        jpTitreCommande = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jpContenuCommande = new javax.swing.JPanel();
        jspLigneCommandes = new javax.swing.JScrollPane();
        jpLigneCommandes = new javax.swing.JPanel();
        jpResumeCommande = new javax.swing.JPanel();
        jpDetailsCommande = new javax.swing.JPanel();
        jlNombreProduit = new javax.swing.JLabel();
        jlTotal = new javax.swing.JLabel();
        jpBoutonsActions = new javax.swing.JPanel();
        jbAnnuler = new javax.swing.JButton();
        jbBrouillon = new javax.swing.JButton();
        jbValider = new javax.swing.JButton();

        jdCommandesEnAttente.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jdCommandesEnAttente.setTitle("Commandes Broouillons");
        jdCommandesEnAttente.setModal(true);

        jpDialogConteneur.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jpDialogConteneur.setLayout(new java.awt.BorderLayout(20, 0));

        jpDialogTopbar.setLayout(new java.awt.BorderLayout());

        jLabel2.setBackground(ApplicationColors.BORDER);
        jLabel2.setFont(new Font("Segoe UI", Font.BOLD, 33));
        jLabel2.setForeground(ApplicationColors.TEXT_PRIMARY);
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("COMMANDES EN BROUILLONS");
        jpDialogTopbar.add(jLabel2, java.awt.BorderLayout.CENTER);

        jpDialogConteneur.add(jpDialogTopbar, java.awt.BorderLayout.NORTH);

        jpCommandeBruillons.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jpCommandeBruillons.setLayout(new java.awt.BorderLayout());

        jpConteneurCommandeBrouillon.setLayout(new javax.swing.BoxLayout(jpConteneurCommandeBrouillon, javax.swing.BoxLayout.Y_AXIS));
        jspCommandesBrouillons.setViewportView(jpConteneurCommandeBrouillon);

        jpCommandeBruillons.add(jspCommandesBrouillons, java.awt.BorderLayout.CENTER);

        jpDialogConteneur.add(jpCommandeBruillons, java.awt.BorderLayout.CENTER);

        jdCommandesEnAttente.getContentPane().add(jpDialogConteneur, java.awt.BorderLayout.CENTER);

        setBackground(ApplicationColors.BACKGROUND);
        setLayout(new java.awt.BorderLayout());

        jpTopbarCommandes.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 10, 10, 10));
        jpTopbarCommandes.setLayout(new java.awt.BorderLayout(50, 0));

        jlLabelDuPanneau.setBackground(ApplicationColors.BACKGROUND
        );
        jlLabelDuPanneau.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jlLabelDuPanneau.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlLabelDuPanneau.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlLabelDuPanneau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/store.png"))); // NOI18N
        jlLabelDuPanneau.setText("Pannel Des Commandes");
        jpTopbarCommandes.add(jlLabelDuPanneau, java.awt.BorderLayout.CENTER);

        jbAncienneCommande.setBackground(ApplicationColors.SUCCESS);
        jbAncienneCommande.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jbAncienneCommande.setForeground(ApplicationColors.TEXT_LIGHT);
        jbAncienneCommande.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/circle-arrow-right-light-l.png"))); // NOI18N
        jbAncienneCommande.setText("Continuer Une Ancienne Commande");
        jbAncienneCommande.setFocusPainted(false);
        jbAncienneCommande.addActionListener(this::jbAncienneCommandeActionPerformed);
        jpTopbarCommandes.add(jbAncienneCommande, java.awt.BorderLayout.EAST);

        add(jpTopbarCommandes, java.awt.BorderLayout.NORTH);

        jpContenuPrincipalCommandes.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 10, 10, 10));
        jpContenuPrincipalCommandes.setLayout(new java.awt.GridLayout(1, 2, 25, 0));

        jpConteneurCatalogueProduit.setLayout(new java.awt.BorderLayout(25, 25));

        jpCatalogueProduit.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 25, 25, 25));
        jpCatalogueProduit.setLayout(new javax.swing.BoxLayout(jpCatalogueProduit, javax.swing.BoxLayout.Y_AXIS));

        jpTitreCatalogue.setMaximumSize(new java.awt.Dimension(2147483647, 100));
        jpTitreCatalogue.setPreferredSize(new java.awt.Dimension(638, 100));
        jpTitreCatalogue.setLayout(new java.awt.BorderLayout());

        jlProduitsDispo.setBackground(ApplicationColors.PANEL_BG);
        jlProduitsDispo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jlProduitsDispo.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlProduitsDispo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlProduitsDispo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/stock.png"))); // NOI18N
        jlProduitsDispo.setText("Produit Disponibles");
        jlProduitsDispo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 5, 1));
        jpTitreCatalogue.add(jlProduitsDispo, java.awt.BorderLayout.CENTER);

        jpCatalogueProduit.add(jpTitreCatalogue);

        jpRechercheProduit.setBorder(javax.swing.BorderFactory.createLineBorder(ApplicationColors.BORDER));
        jpRechercheProduit.setOpaque(false);
        jpRechercheProduit.setPreferredSize(new java.awt.Dimension(638, 100));
        jpRechercheProduit.setLayout(new java.awt.BorderLayout(20, 0));

        jtfRechercheProduit.setBackground(ApplicationColors.PANEL_BG);
        jtfRechercheProduit.setFont(new Font("Segoe UI", Font.PLAIN, 25));
        jtfRechercheProduit.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfRechercheProduit.setBorder(javax.swing.BorderFactory.createLineBorder(ApplicationColors.BORDER));
        jtfRechercheProduit.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jpRechercheProduit.add(jtfRechercheProduit, java.awt.BorderLayout.CENTER);

        jlLogoRecherche.setBackground(ApplicationColors.PANEL_BG);
        jlLogoRecherche.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlLogoRecherche.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search-xl.png"))); // NOI18N
        jpRechercheProduit.add(jlLogoRecherche, java.awt.BorderLayout.WEST);

        jpCatalogueProduit.add(jpRechercheProduit);

        jpConteneurCartesProduits.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 1, 1, 1));
        jpConteneurCartesProduits.setPreferredSize(new java.awt.Dimension(638, 1000));
        jpConteneurCartesProduits.setLayout(new java.awt.BorderLayout());

        jpCartesProduits.setLayout(new java.awt.GridLayout(0, 3));
        jspCartesProduits.setViewportView(jpCartesProduits);

        jpConteneurCartesProduits.add(jspCartesProduits, java.awt.BorderLayout.CENTER);

        jpCatalogueProduit.add(jpConteneurCartesProduits);

        jpConteneurCatalogueProduit.add(jpCatalogueProduit, java.awt.BorderLayout.CENTER);

        jpContenuPrincipalCommandes.add(jpConteneurCatalogueProduit);

        jpConteneurCommandeEnCours.setLayout(new java.awt.BorderLayout(25, 25));

        jpCommandeEnCours.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 25, 25, 25));
        jpCommandeEnCours.setLayout(new java.awt.BorderLayout());

        jpTitreCommande.setMaximumSize(new java.awt.Dimension(2147483647, 100));
        jpTitreCommande.setPreferredSize(new java.awt.Dimension(638, 65));
        jpTitreCommande.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(ApplicationColors.PANEL_BG
        );
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jLabel1.setForeground(ApplicationColors.TEXT_PRIMARY);
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/shopping.png"))); // NOI18N
        jLabel1.setText("Commande En Cours");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 5, 1));
        jLabel1.setMaximumSize(new java.awt.Dimension(156, 30));
        jLabel1.setMinimumSize(new java.awt.Dimension(156, 30));
        jLabel1.setPreferredSize(new java.awt.Dimension(156, 30));
        jpTitreCommande.add(jLabel1, java.awt.BorderLayout.CENTER);

        jpCommandeEnCours.add(jpTitreCommande, java.awt.BorderLayout.NORTH);

        jpContenuCommande.setPreferredSize(new java.awt.Dimension(638, 1000));
        jpContenuCommande.setLayout(new java.awt.BorderLayout());

        jpLigneCommandes.setLayout(new javax.swing.BoxLayout(jpLigneCommandes, javax.swing.BoxLayout.Y_AXIS));
        jspLigneCommandes.setViewportView(jpLigneCommandes);

        jpContenuCommande.add(jspLigneCommandes, java.awt.BorderLayout.CENTER);

        jpCommandeEnCours.add(jpContenuCommande, java.awt.BorderLayout.CENTER);

        jpResumeCommande.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 1, 1, 1));
        jpResumeCommande.setPreferredSize(new java.awt.Dimension(638, 100));
        jpResumeCommande.setLayout(new java.awt.GridLayout(2, 1));

        jpDetailsCommande.setLayout(new java.awt.BorderLayout());

        jlNombreProduit.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        jlNombreProduit.setForeground(ApplicationColors.TEXT_SECONDARY);
        jlNombreProduit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlNombreProduit.setText("Nombre de Produits : ");
        jlNombreProduit.setRequestFocusEnabled(false);
        jpDetailsCommande.add(jlNombreProduit, java.awt.BorderLayout.WEST);

        jlTotal.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        jlTotal.setForeground(ApplicationColors.TEXT_SECONDARY);
        jlTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTotal.setText("Total : ");
        jpDetailsCommande.add(jlTotal, java.awt.BorderLayout.EAST);

        jpResumeCommande.add(jpDetailsCommande);

        jpBoutonsActions.setPreferredSize(new java.awt.Dimension(688, 50));
        jpBoutonsActions.setLayout(new java.awt.GridLayout(1, 3, 10, 0));

        jbAnnuler.setBackground(ApplicationColors.ERROR
        );
        jbAnnuler.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jbAnnuler.setForeground(ApplicationColors.TEXT_LIGHT);
        jbAnnuler.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ban_whithe.png"))); // NOI18N
        jbAnnuler.setText("Annuler");
        jbAnnuler.addActionListener(this::jbAnnulerActionPerformed);
        jpBoutonsActions.add(jbAnnuler);

        jbBrouillon.setBackground(ApplicationColors.TEXT_SECONDARY);
        jbBrouillon.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jbBrouillon.setForeground(ApplicationColors.TEXT_LIGHT);
        jbBrouillon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/square-pen-white.png"))); // NOI18N
        jbBrouillon.setText("Brouillon");
        jbBrouillon.addActionListener(this::jbBrouillonActionPerformed);
        jpBoutonsActions.add(jbBrouillon);

        jbValider.setBackground(ApplicationColors.PRIMARY);
        jbValider.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jbValider.setForeground(ApplicationColors.TEXT_LIGHT);
        jbValider.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/check-whith.png"))); // NOI18N
        jbValider.setText("Valider");
        jbValider.addActionListener(this::jbValiderActionPerformed);
        jpBoutonsActions.add(jbValider);

        jpResumeCommande.add(jpBoutonsActions);

        jpCommandeEnCours.add(jpResumeCommande, java.awt.BorderLayout.SOUTH);

        jpConteneurCommandeEnCours.add(jpCommandeEnCours, java.awt.BorderLayout.CENTER);

        jpContenuPrincipalCommandes.add(jpConteneurCommandeEnCours);

        add(jpContenuPrincipalCommandes, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jbAnnulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAnnulerActionPerformed
        truncateLePanelDeCommande();
    }//GEN-LAST:event_jbAnnulerActionPerformed
    
    
    private void jbBrouillonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbBrouillonActionPerformed
        if (lignesActuelles.isEmpty()) {
            showCustomErreurJOptionPane("Aucune Ligne a enregistre au brouillon");
            return;
        }
        Commande laCommande = new Commande(
            0, LocalDateTime.now(), Commande.EtatCommande.EN_COURS,
            totalPrixCommandeActuel, LocalDateTime.MAX,
            AuthentificationManager.getInstance().recupererUtilisateurConnecte()
        );
        
        
        CrudResult<Commande> resultatAjoutCommande = CommandeDAO.getInstance().enregistrerAvecRetourId(laCommande);
        
        if (resultatAjoutCommande.estUneErreur()) {
            showCustomErreurJOptionPane(resultatAjoutCommande.getErreur());
            return;
        } 
        List<LigneCommande> lignesAEnregistrer = List.copyOf(lignesActuelles.values());
        
        for (LigneCommande ligneCommande : lignesAEnregistrer) {
            ligneCommande.setCommande(resultatAjoutCommande.getDonnes());
        }
        
        CrudResult<Boolean> resultatAjoutsLignes = LigneCommandeDAO.getInstance().enregistrerPlusieurs(lignesAEnregistrer);
        
        if (resultatAjoutsLignes.estUneErreur()) {
            showCustomErreurJOptionPane(resultatAjoutsLignes.getErreur());
            return;
        } 
        
        truncateLePanelDeCommande();
        showCustomInfoJOptionPane("Brouillon enregistré avec succès");
        
        AuthentificationManager.getInstance().enregistrerActionDansAudit(ActionType.AJOUT, "A Ajouté la Commande " + laCommande.getIdCommande() + " en Brouillon");
        
    }//GEN-LAST:event_jbBrouillonActionPerformed

    private void jbValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbValiderActionPerformed
        // TODO add your handling code here:
        if (lignesActuelles.isEmpty()) {
            showCustomErreurJOptionPane("Aucune Ligne a enregistré");
            return;
        }
        
        Commande laCommande = new Commande(
            0, LocalDateTime.now(), Commande.EtatCommande.VALIDEE,
            totalPrixCommandeActuel, LocalDateTime.MAX,
            AuthentificationManager.getInstance().recupererUtilisateurConnecte()
        );
        
        CrudResult<Boolean> resultat = CommandeDAO.getInstance().creerCommandeAvecLignes(laCommande, List.copyOf(lignesActuelles.values()));
        
        if (resultat.estUneErreur()) {
            showCustomErreurJOptionPane(resultat.getErreur());
            return;
            
        }
        
        
        
        truncateLePanelDeCommande();
        mettreListeProduitAJour();
        showCustomInfoJOptionPane("Commande validée avec succès");
        AuthentificationManager.getInstance().enregistrerActionDansAudit(ActionType.AJOUT, "A Validé la commande : " + laCommande.getIdCommande());

        
    }//GEN-LAST:event_jbValiderActionPerformed

    private void jbAncienneCommandeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAncienneCommandeActionPerformed
        // TODO add your handling code here:
        chargerCommandesEnAttente();
        jdCommandesEnAttente.setVisible(true);
    }//GEN-LAST:event_jbAncienneCommandeActionPerformed
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton jbAncienneCommande;
    private javax.swing.JButton jbAnnuler;
    private javax.swing.JButton jbBrouillon;
    private javax.swing.JButton jbValider;
    private javax.swing.JDialog jdCommandesEnAttente;
    private javax.swing.JLabel jlLabelDuPanneau;
    private javax.swing.JLabel jlLogoRecherche;
    private javax.swing.JLabel jlNombreProduit;
    private javax.swing.JLabel jlProduitsDispo;
    private javax.swing.JLabel jlTotal;
    private javax.swing.JPanel jpBoutonsActions;
    private javax.swing.JPanel jpCartesProduits;
    private javax.swing.JPanel jpCatalogueProduit;
    private javax.swing.JPanel jpCommandeBruillons;
    private javax.swing.JPanel jpCommandeEnCours;
    private javax.swing.JPanel jpConteneurCartesProduits;
    private javax.swing.JPanel jpConteneurCatalogueProduit;
    private javax.swing.JPanel jpConteneurCommandeBrouillon;
    private javax.swing.JPanel jpConteneurCommandeEnCours;
    private javax.swing.JPanel jpContenuCommande;
    private javax.swing.JPanel jpContenuPrincipalCommandes;
    private javax.swing.JPanel jpDetailsCommande;
    private javax.swing.JPanel jpDialogConteneur;
    private javax.swing.JPanel jpDialogTopbar;
    private javax.swing.JPanel jpLigneCommandes;
    private javax.swing.JPanel jpRechercheProduit;
    private javax.swing.JPanel jpResumeCommande;
    private javax.swing.JPanel jpTitreCatalogue;
    private javax.swing.JPanel jpTitreCommande;
    private javax.swing.JPanel jpTopbarCommandes;
    private javax.swing.JScrollPane jspCartesProduits;
    private javax.swing.JScrollPane jspCommandesBrouillons;
    private javax.swing.JScrollPane jspLigneCommandes;
    private javax.swing.JTextField jtfRechercheProduit;
    // End of variables declaration//GEN-END:variables
}
