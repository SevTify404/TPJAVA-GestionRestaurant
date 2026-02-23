/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formulaires;
import dao.CommandeDAO;
import dao.CrudResult;
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
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import utilitaires.ApplicationColors;
/**
 *
 * @author sevtify
 */
public class DashBoardPanel extends javax.swing.JPanel {

    /**
     * Creates new form DashBoardPanel
     */
    public DashBoardPanel() {
        initComponents();
        initCustomCOmponents();
    }
    
    private void initCustomCOmponents(){
        
        // Pour accelerer le scroll
        jspAlerteProduit.getVerticalScrollBar().setUnitIncrement(16);
        jspCommandes.getVerticalScrollBar().setUnitIncrement(16);
        chargerDonneesDashBoard();
        
        
    }
    
    private <T> T extraireDonneesOuErreur(CrudResult<T> result, T valeurParDefaut, List<String> erreurs) {
        if (result.estUnSucces()) {
            return result.getDonnes();
        }
        
        erreurs.add(result.getErreur());
        return valeurParDefaut;
    }
    private void chargerDonneesDashBoard(){
        List<String> erreur  = new ArrayList<>();
        
        
        List<Produit> produitsEnDessousDeSeuil  = new ArrayList<>();
        List<Commande> dernieresCommandes  = null;
        int nbreProduit = 0, nbreProduitEnDessousDeStock, nbreCommandesDuJour;
        double caDuJour;
        
        ProduitDAO produitDAO = ProduitDAO.getInstance();
        CommandeDAO commandeDAO = CommandeDAO.getInstance();
        
        CrudResult<Integer> requeteNbreProduit = produitDAO.recupererNombreDeProduits();
        
        nbreProduit = extraireDonneesOuErreur(requeteNbreProduit, nbreProduit, erreur);
      
        CrudResult<List<Produit>> requeteProduitEnDessousDeStock = produitDAO.recupererProduitsEnDessousDeSeuil();
        
        produitsEnDessousDeSeuil = extraireDonneesOuErreur(requeteProduitEnDessousDeStock, produitsEnDessousDeSeuil, erreur);
        nbreProduitEnDessousDeStock = produitsEnDessousDeSeuil.size();
        
        
        CrudResult<Map<String, Integer>> requeteCommandeValideDuJour = commandeDAO.recupererInfosJourPourDashboard();
        
        Map<String, Integer> donnneesRequeteCommandeValideDuJour = new HashMap<>();
        
        donnneesRequeteCommandeValideDuJour = extraireDonneesOuErreur(requeteCommandeValideDuJour, donnneesRequeteCommandeValideDuJour, erreur);
        nbreCommandesDuJour = donnneesRequeteCommandeValideDuJour.get("count");
        caDuJour = donnneesRequeteCommandeValideDuJour.get("chiffreAffaireJour");
        
        CrudResult<List<Commande>> dernieresCommandesRequet = commandeDAO.recuperer10DerniersCommandesAvecLignePourDashBoard(nbreCommandesDuJour);
        dernieresCommandes = extraireDonneesOuErreur(dernieresCommandesRequet, dernieresCommandes, erreur);
        
        System.out.println(erreur);
        jlDetailsCarte1_1.setText(String.valueOf(nbreProduit));
        jlDetailsCarte2_1.setText(String.valueOf(nbreCommandesDuJour));
        jlDetailsCarte3_1.setText(String.valueOf(caDuJour));
        jlDetailsCarte4_1.setText(String.valueOf(nbreProduitEnDessousDeStock));
        rafraichirAlertesStock(produitsEnDessousDeSeuil);
        rafraichirListeCommandes(dernieresCommandes);
    }
    

    
    private void rafraichirListeCommandes(List<Commande> lesCommandes) {
        jpContenuCommandes.removeAll(); // Vide le contenu actuel
        if (lesCommandes == null || lesCommandes.isEmpty()) {
            JLabel labelPasCommandes = new JLabel("Auucune Commandes Récentes");
            
            labelPasCommandes.setForeground(ApplicationColors.TEXT_PRIMARY);
            
            labelPasCommandes.setFont(new Font("Segoe UI", Font.BOLD, 22));

            labelPasCommandes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            labelPasCommandes.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
            labelPasCommandes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            labelPasCommandes.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
            
            jpContenuCommandes.add(labelPasCommandes);
            
        } else {
            for (Commande laCommande : lesCommandes) {
                String prduits = "";

                for (LigneCommande ligneC : laCommande.getLigneCommnandes()) {
                    prduits = prduits.isEmpty() ?
                        ligneC.getQuantite() + "x " + ligneC.getProduit().getNom() :
                        prduits + ", " + ligneC.getQuantite() + "x "  + ligneC.getProduit().getNom();
                }
                JPanel card = createCard("Commande #" + String.valueOf(laCommande.getIdCommande()), 
                                        prduits, 
                                         String.valueOf(laCommande.getTotal()) + " FCFA");

                // Fixe une taille préférée pour la carte sinon le layout peut la compresser
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

                jpContenuCommandes.add(card);
                jpContenuCommandes.add(Box.createRigidArea(new Dimension(0, 10))); // Espacement entre cartes

            }
        }
    
        jpContenuCommandes.revalidate(); // Indique à Swing de recalculer l'affichage
        jpContenuCommandes.repaint();    // Force le redessin
    }
    
    private void rafraichirAlertesStock(List<Produit> lesProduits) {
        jpContenuProduitAlert.removeAll(); // Nettoie les anciennes alertes
        
        if (lesProduits == null || lesProduits.isEmpty()) {
            JLabel labelPasAlerte = new JLabel("Auucun Produit en Dessous de Stock");
            
            labelPasAlerte.setForeground(ApplicationColors.TEXT_PRIMARY);
            
            labelPasAlerte.setFont(new Font("Segoe UI", Font.BOLD, 22));

            labelPasAlerte.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            labelPasAlerte.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
            labelPasAlerte.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            labelPasAlerte.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
            
            jpContenuProduitAlert.add(labelPasAlerte);
        }else{
        
            for (Produit leProduit : lesProduits) {
                JPanel alerte = createAlerteStockCard(leProduit);

                jpContenuProduitAlert.add(alerte);
                // Ajout d'un petit espacement entre les cartes
                jpContenuProduitAlert.add(Box.createRigidArea(new Dimension(0, 8))); 
            }
        }


        jpContenuProduitAlert.revalidate(); // Indique à Swing de recalculer le layout
        jpContenuProduitAlert.repaint();    // Rafraîchit l'affichage
}
    
    public JPanel createAlerteStockCard(Produit leProduit) {
        // 1. Création du panneau principal de la carte
        JPanel card = new JPanel(new BorderLayout());
        // Fond rose pâle pour signaler l'alerte
        card.setBackground(new Color(255, 235, 238)); 
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 205, 210), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));

        // 2. Panneau gauche : Nom et Catégorie
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setOpaque(false);

        JLabel lblNom = new JLabel(leProduit.getNom());
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblCat = new JLabel(leProduit.getCategorie().getLIBELLE());
        lblCat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCat.setForeground(ApplicationColors.TEXT_SECONDARY);

        leftPanel.add(lblNom);
        leftPanel.add(lblCat);

        // 3. Panneau droit : Stock et Seuil
        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        rightPanel.setOpaque(false);

        JLabel lblStock = new JLabel("Stock: " + leProduit.getStockActuel());
        lblStock.setForeground(ApplicationColors.ERROR); // Rouge pour le stock
        lblStock.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel lblSeuil = new JLabel("Seuil: " + leProduit.getSeuilAlerte());
        lblSeuil.setHorizontalAlignment(SwingConstants.RIGHT);

        rightPanel.add(lblStock);
        rightPanel.add(lblSeuil);

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        // Fixer une hauteur maximale pour éviter que la carte ne s'étire trop
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        return card;
}
    public JPanel createCard(String title, String subtitle, String amountOrValue) {
        // 1. Panel conteneur avec un layout propre
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        // Bordure fine pour l'effet carte
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));

        // 2. Panel pour le texte (gauche)
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblSubtitle = new JLabel(subtitle);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(Color.GRAY);

        textPanel.add(lblTitle);
        textPanel.add(lblSubtitle);

        // 3. Label pour le montant/valeur (droite)
        JLabel lblAmount = new JLabel(amountOrValue);
        lblAmount.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblAmount.setForeground(new Color(33, 150, 243)); // Couleur primaire

        card.add(textPanel, BorderLayout.CENTER);
        card.add(lblAmount, BorderLayout.EAST);

    return card;
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpCartes = new javax.swing.JPanel();
        jpCarte1 = new javax.swing.JPanel();
        jpImageCarte1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jpDetailsCarte1 = new javax.swing.JPanel();
        jlDetailsCarte1_1 = new javax.swing.JLabel();
        jlDetailsCarte1_2 = new javax.swing.JLabel();
        jpCarte2 = new javax.swing.JPanel();
        jpImageCarte2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jpDetailsCarte2 = new javax.swing.JPanel();
        jlDetailsCarte2_1 = new javax.swing.JLabel();
        jlDetailsCarte2_2 = new javax.swing.JLabel();
        jpCarte3 = new javax.swing.JPanel();
        jpImageCarte3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jpDetailsCarte3 = new javax.swing.JPanel();
        jlDetailsCarte3_1 = new javax.swing.JLabel();
        jlDetailsCarte3_2 = new javax.swing.JLabel();
        jpCarte4 = new javax.swing.JPanel();
        jpImageCarte4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jpDetailsCarte4 = new javax.swing.JPanel();
        jlDetailsCarte4_1 = new javax.swing.JLabel();
        jlDetailsCarte4_2 = new javax.swing.JLabel();
        jpNavigation = new javax.swing.JPanel();
        jpTexteNavigation = new javax.swing.JPanel();
        jlNavigatiopRapide = new javax.swing.JLabel();
        jbRafraichirPage = new javax.swing.JButton();
        jpCartesNavigationRapide = new javax.swing.JPanel();
        jpCarteNaviguation1 = new javax.swing.JPanel();
        jlImageCarteNaviguation1 = new javax.swing.JLabel();
        jlDetailsCarteNaviguation1 = new javax.swing.JLabel();
        jpCarteNaviguation2 = new javax.swing.JPanel();
        jlImageCarteNaviguation2 = new javax.swing.JLabel();
        jlDetailsCarteNaviguation2 = new javax.swing.JLabel();
        jpCarteNaviguation3 = new javax.swing.JPanel();
        jlImageCarteNaviguation3 = new javax.swing.JLabel();
        jlDetailsCarteNaviguation3 = new javax.swing.JLabel();
        jpCarteNaviguation4 = new javax.swing.JPanel();
        jlImageCarteNaviguation4 = new javax.swing.JLabel();
        jlDetailsCarteNaviguation4 = new javax.swing.JLabel();
        jpDetailsDashorad = new javax.swing.JPanel();
        jpProduitsAlertes = new javax.swing.JPanel();
        jpDetailAlerteProduit = new javax.swing.JPanel();
        jlDetailAlerteProduit = new javax.swing.JLabel();
        jspAlerteProduit = new javax.swing.JScrollPane();
        jpContenuProduitAlert = new javax.swing.JPanel();
        jpCommandes = new javax.swing.JPanel();
        jpDetailsCommandes = new javax.swing.JPanel();
        jlDetailsCommandes = new javax.swing.JLabel();
        jspCommandes = new javax.swing.JScrollPane();
        jpContenuCommandes = new javax.swing.JPanel();

        setBackground(ApplicationColors.BACKGROUND);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        setLayout(new java.awt.BorderLayout(0, 15));

        jpCartes.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 0, 15));
        jpCartes.setLayout(new java.awt.GridLayout(1, 4, 20, 0));

        jpCarte1.setBackground(ApplicationColors.BACKGROUND);
        jpCarte1.setBorder(new javax.swing.border.MatteBorder(null));
        jpCarte1.setLayout(new java.awt.BorderLayout(10, 0));

        jpImageCarte1.setBackground(ApplicationColors.PANEL_BG);
        jpImageCarte1.setLayout(new javax.swing.BoxLayout(jpImageCarte1, javax.swing.BoxLayout.LINE_AXIS));

        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/in-stock (1).png"))); // NOI18N
        jPanel3.add(jLabel4, java.awt.BorderLayout.CENTER);

        jpImageCarte1.add(jPanel3);

        jpCarte1.add(jpImageCarte1, java.awt.BorderLayout.WEST);

        jpDetailsCarte1.setBackground(ApplicationColors.BACKGROUND        );
        jpDetailsCarte1.setLayout(new javax.swing.BoxLayout(jpDetailsCarte1, javax.swing.BoxLayout.Y_AXIS));

        jlDetailsCarte1_1.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jlDetailsCarte1_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte1_1.setText("48");
        jpDetailsCarte1.add(jlDetailsCarte1_1);

        jlDetailsCarte1_2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jlDetailsCarte1_2.setForeground(ApplicationColors.TEXT_SECONDARY);
        jlDetailsCarte1_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte1_2.setText("Total Produits");
        jpDetailsCarte1.add(jlDetailsCarte1_2);

        jpCarte1.add(jpDetailsCarte1, java.awt.BorderLayout.CENTER);

        jpCartes.add(jpCarte1);

        jpCarte2.setBorder(new javax.swing.border.MatteBorder(null));
        jpCarte2.setLayout(new java.awt.BorderLayout(10, 0));

        jpImageCarte2.setBackground(ApplicationColors.PANEL_BG);
        jpImageCarte2.setLayout(new javax.swing.BoxLayout(jpImageCarte2, javax.swing.BoxLayout.LINE_AXIS));

        jPanel5.setLayout(new java.awt.BorderLayout());

        jLabel5.setBackground(ApplicationColors.BACKGROUND);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/purchase-order.png"))); // NOI18N
        jPanel5.add(jLabel5, java.awt.BorderLayout.CENTER);

        jpImageCarte2.add(jPanel5);

        jpCarte2.add(jpImageCarte2, java.awt.BorderLayout.WEST);

        jpDetailsCarte2.setBackground(ApplicationColors.BACKGROUND);
        jpDetailsCarte2.setLayout(new javax.swing.BoxLayout(jpDetailsCarte2, javax.swing.BoxLayout.Y_AXIS));

        jlDetailsCarte2_1.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jlDetailsCarte2_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte2_1.setText("48");
        jpDetailsCarte2.add(jlDetailsCarte2_1);

        jlDetailsCarte2_2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jlDetailsCarte2_2.setForeground(ApplicationColors.TEXT_SECONDARY);
        jlDetailsCarte2_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte2_2.setText("Commandes Du Jour");
        jpDetailsCarte2.add(jlDetailsCarte2_2);

        jpCarte2.add(jpDetailsCarte2, java.awt.BorderLayout.CENTER);

        jpCartes.add(jpCarte2);

        jpCarte3.setBorder(new javax.swing.border.MatteBorder(null));
        jpCarte3.setLayout(new java.awt.BorderLayout(10, 0));

        jpImageCarte3.setBackground(ApplicationColors.PANEL_BG);
        jpImageCarte3.setLayout(new javax.swing.BoxLayout(jpImageCarte3, javax.swing.BoxLayout.LINE_AXIS));

        jPanel8.setLayout(new java.awt.BorderLayout());

        jLabel7.setBackground(ApplicationColors.BACKGROUND);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/money-bag.png"))); // NOI18N
        jPanel8.add(jLabel7, java.awt.BorderLayout.CENTER);

        jpImageCarte3.add(jPanel8);

        jpCarte3.add(jpImageCarte3, java.awt.BorderLayout.WEST);

        jpDetailsCarte3.setBackground(ApplicationColors.BACKGROUND);
        jpDetailsCarte3.setLayout(new javax.swing.BoxLayout(jpDetailsCarte3, javax.swing.BoxLayout.Y_AXIS));

        jlDetailsCarte3_1.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jlDetailsCarte3_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte3_1.setText("48");
        jpDetailsCarte3.add(jlDetailsCarte3_1);

        jlDetailsCarte3_2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jlDetailsCarte3_2.setForeground(ApplicationColors.TEXT_SECONDARY);
        jlDetailsCarte3_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte3_2.setText("Revenues Du Jour");
        jpDetailsCarte3.add(jlDetailsCarte3_2);

        jpCarte3.add(jpDetailsCarte3, java.awt.BorderLayout.CENTER);

        jpCartes.add(jpCarte3);

        jpCarte4.setBorder(new javax.swing.border.MatteBorder(null));
        jpCarte4.setLayout(new java.awt.BorderLayout(10, 0));

        jpImageCarte4.setBackground(ApplicationColors.PANEL_BG);
        jpImageCarte4.setLayout(new javax.swing.BoxLayout(jpImageCarte4, javax.swing.BoxLayout.LINE_AXIS));

        jPanel11.setLayout(new java.awt.BorderLayout());

        jLabel10.setBackground(ApplicationColors.BACKGROUND);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/danger.png"))); // NOI18N
        jPanel11.add(jLabel10, java.awt.BorderLayout.CENTER);

        jpImageCarte4.add(jPanel11);

        jpCarte4.add(jpImageCarte4, java.awt.BorderLayout.WEST);

        jpDetailsCarte4.setBackground(ApplicationColors.BACKGROUND);
        jpDetailsCarte4.setLayout(new javax.swing.BoxLayout(jpDetailsCarte4, javax.swing.BoxLayout.Y_AXIS));

        jlDetailsCarte4_1.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jlDetailsCarte4_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte4_1.setText("48");
        jpDetailsCarte4.add(jlDetailsCarte4_1);

        jlDetailsCarte4_2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jlDetailsCarte4_2.setForeground(ApplicationColors.TEXT_SECONDARY);
        jlDetailsCarte4_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte4_2.setText("Produits Dessous Seuil");
        jpDetailsCarte4.add(jlDetailsCarte4_2);

        jpCarte4.add(jpDetailsCarte4, java.awt.BorderLayout.CENTER);

        jpCartes.add(jpCarte4);

        add(jpCartes, java.awt.BorderLayout.NORTH);

        jpNavigation.setBackground(ApplicationColors.BACKGROUND);
        jpNavigation.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 15, 15));
        jpNavigation.setLayout(new java.awt.BorderLayout(0, 10));

        jpTexteNavigation.setBackground(ApplicationColors.BACKGROUND);
        jpTexteNavigation.setLayout(new java.awt.BorderLayout());

        jlNavigatiopRapide.setFont(new Font("Segoe UI", Font.BOLD, 25)
        );
        jlNavigatiopRapide.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlNavigatiopRapide.setText("Navigation Rapide");
        jpTexteNavigation.add(jlNavigatiopRapide, java.awt.BorderLayout.WEST);

        jbRafraichirPage.setBackground(ApplicationColors.SIDEBAR_SELECTION);
        jbRafraichirPage.setFont(new Font("Segoe UI", Font.BOLD, 19));
        jbRafraichirPage.setForeground(ApplicationColors.TEXT_LIGHT);
        jbRafraichirPage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh-l.png"))); // NOI18N
        jbRafraichirPage.setText("Rafraichir DashBoard");
        jbRafraichirPage.setBorderPainted(false);
        jbRafraichirPage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jbRafraichirPage.setFocusPainted(false);
        jbRafraichirPage.addActionListener(this::jbRafraichirPageActionPerformed);
        jpTexteNavigation.add(jbRafraichirPage, java.awt.BorderLayout.EAST);

        jpNavigation.add(jpTexteNavigation, java.awt.BorderLayout.NORTH);

        jpCartesNavigationRapide.setBackground(ApplicationColors.BACKGROUND);
        jpCartesNavigationRapide.setLayout(new java.awt.GridLayout(1, 4, 25, 0));

        jpCarteNaviguation1.setBackground(ApplicationColors.PANEL_BG);
        jpCarteNaviguation1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jpCarteNaviguation1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jpCarteNaviguation1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jpCarteNaviguation1MouseExited(evt);
            }
        });
        jpCarteNaviguation1.setLayout(new java.awt.BorderLayout());

        jlImageCarteNaviguation1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlImageCarteNaviguation1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/store-xxl.png"))); // NOI18N
        jpCarteNaviguation1.add(jlImageCarteNaviguation1, java.awt.BorderLayout.CENTER);

        jlDetailsCarteNaviguation1.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        jlDetailsCarteNaviguation1.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlDetailsCarteNaviguation1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarteNaviguation1.setText("Produits");
        jlDetailsCarteNaviguation1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jpCarteNaviguation1.add(jlDetailsCarteNaviguation1, java.awt.BorderLayout.PAGE_END);

        jpCartesNavigationRapide.add(jpCarteNaviguation1);

        jpCarteNaviguation2.setBackground(ApplicationColors.PANEL_BG);
        jpCarteNaviguation2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jpCarteNaviguation2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jpCarteNaviguation2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jpCarteNaviguation2MouseExited(evt);
            }
        });
        jpCarteNaviguation2.setLayout(new java.awt.BorderLayout(0, 24));

        jlImageCarteNaviguation2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlImageCarteNaviguation2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/stock-xxl.png"))); // NOI18N
        jpCarteNaviguation2.add(jlImageCarteNaviguation2, java.awt.BorderLayout.CENTER);

        jlDetailsCarteNaviguation2.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        jlDetailsCarteNaviguation2.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlDetailsCarteNaviguation2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarteNaviguation2.setText("Stock");
        jpCarteNaviguation2.add(jlDetailsCarteNaviguation2, java.awt.BorderLayout.PAGE_END);

        jpCartesNavigationRapide.add(jpCarteNaviguation2);

        jpCarteNaviguation3.setBackground(ApplicationColors.PANEL_BG);
        jpCarteNaviguation3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jpCarteNaviguation3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jpCarteNaviguation3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jpCarteNaviguation3MouseExited(evt);
            }
        });
        jpCarteNaviguation3.setLayout(new java.awt.BorderLayout(0, 24));

        jlImageCarteNaviguation3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlImageCarteNaviguation3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/shopping-xxl.png"))); // NOI18N
        jlImageCarteNaviguation3.setToolTipText("");
        jpCarteNaviguation3.add(jlImageCarteNaviguation3, java.awt.BorderLayout.CENTER);

        jlDetailsCarteNaviguation3.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        jlDetailsCarteNaviguation3.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlDetailsCarteNaviguation3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarteNaviguation3.setText("Commandes");
        jpCarteNaviguation3.add(jlDetailsCarteNaviguation3, java.awt.BorderLayout.SOUTH);

        jpCartesNavigationRapide.add(jpCarteNaviguation3);

        jpCarteNaviguation4.setBackground(ApplicationColors.PANEL_BG);
        jpCarteNaviguation4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jpCarteNaviguation4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jpCarteNaviguation4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jpCarteNaviguation4MouseExited(evt);
            }
        });
        jpCarteNaviguation4.setLayout(new java.awt.BorderLayout(0, 24));

        jlImageCarteNaviguation4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlImageCarteNaviguation4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/stats-xxl.png"))); // NOI18N
        jpCarteNaviguation4.add(jlImageCarteNaviguation4, java.awt.BorderLayout.CENTER);

        jlDetailsCarteNaviguation4.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        jlDetailsCarteNaviguation4.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlDetailsCarteNaviguation4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarteNaviguation4.setText("Statistiques");
        jpCarteNaviguation4.add(jlDetailsCarteNaviguation4, java.awt.BorderLayout.PAGE_END);

        jpCartesNavigationRapide.add(jpCarteNaviguation4);

        jpNavigation.add(jpCartesNavigationRapide, java.awt.BorderLayout.CENTER);

        add(jpNavigation, java.awt.BorderLayout.CENTER);

        jpDetailsDashorad.setBackground(ApplicationColors.BACKGROUND);
        jpDetailsDashorad.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 10, 15));
        jpDetailsDashorad.setPreferredSize(new java.awt.Dimension(660, 440));
        jpDetailsDashorad.setLayout(new java.awt.GridLayout(1, 2, 30, 0));

        jpProduitsAlertes.setBackground(ApplicationColors.BACKGROUND);
        jpProduitsAlertes.setLayout(new java.awt.BorderLayout(0, 15));

        jpDetailAlerteProduit.setBackground(ApplicationColors.BACKGROUND);
        jpDetailAlerteProduit.setLayout(new java.awt.BorderLayout());

        jlDetailAlerteProduit.setBackground(ApplicationColors.BACKGROUND        );
        jlDetailAlerteProduit.setFont(new Font("Segoe UI", Font.BOLD, 22));
        jlDetailAlerteProduit.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlDetailAlerteProduit.setText("Produit en alertes de stock");
        jpDetailAlerteProduit.add(jlDetailAlerteProduit, java.awt.BorderLayout.CENTER);

        jpProduitsAlertes.add(jpDetailAlerteProduit, java.awt.BorderLayout.NORTH);

        jspAlerteProduit.setBorder(null);
        jspAlerteProduit.setToolTipText("");
        jspAlerteProduit.setMinimumSize(new java.awt.Dimension(100, 100));
        jspAlerteProduit.setOpaque(false);
        jspAlerteProduit.setPreferredSize(new java.awt.Dimension(300, 400));
        jspAlerteProduit.setViewportView(null);

        jpContenuProduitAlert.setBackground(ApplicationColors.BACKGROUND);
        jpContenuProduitAlert.setLayout(new javax.swing.BoxLayout(jpContenuProduitAlert, javax.swing.BoxLayout.Y_AXIS));
        jspAlerteProduit.setViewportView(jpContenuProduitAlert);

        jpProduitsAlertes.add(jspAlerteProduit, java.awt.BorderLayout.CENTER);

        jpDetailsDashorad.add(jpProduitsAlertes);

        jpCommandes.setBackground(ApplicationColors.BACKGROUND);
        jpCommandes.setLayout(new java.awt.BorderLayout(0, 15));

        jpDetailsCommandes.setBackground(ApplicationColors.BACKGROUND);
        jpDetailsCommandes.setLayout(new java.awt.BorderLayout());

        jlDetailsCommandes.setBackground(ApplicationColors.BACKGROUND        );
        jlDetailsCommandes.setFont(new Font("Segoe UI", Font.BOLD, 22));
        jlDetailsCommandes.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlDetailsCommandes.setText("10 Dernières Commandes");
        jpDetailsCommandes.add(jlDetailsCommandes, java.awt.BorderLayout.CENTER);

        jpCommandes.add(jpDetailsCommandes, java.awt.BorderLayout.NORTH);

        jspCommandes.setBorder(null);
        jspCommandes.setToolTipText("");
        jspCommandes.setMinimumSize(new java.awt.Dimension(100, 100));
        jspCommandes.setOpaque(false);
        jspCommandes.setPreferredSize(new java.awt.Dimension(300, 400));
        jspCommandes.setViewportView(null);

        jpContenuCommandes.setBackground(ApplicationColors.BACKGROUND);
        jpContenuCommandes.setLayout(new javax.swing.BoxLayout(jpContenuCommandes, javax.swing.BoxLayout.Y_AXIS));
        jspCommandes.setViewportView(jpContenuCommandes);

        jpCommandes.add(jspCommandes, java.awt.BorderLayout.CENTER);

        jpDetailsDashorad.add(jpCommandes);

        add(jpDetailsDashorad, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_formKeyPressed

    private void jpCarteNaviguation1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpCarteNaviguation1MouseEntered
        jpCarteNaviguation1.setBackground(ApplicationColors.SIDEBAR_HOVER);
    }//GEN-LAST:event_jpCarteNaviguation1MouseEntered

    private void jpCarteNaviguation1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpCarteNaviguation1MouseExited
        jpCarteNaviguation1.setBackground(ApplicationColors.PANEL_BG);
        // TODO add your handling code here:
    }//GEN-LAST:event_jpCarteNaviguation1MouseExited

    private void jpCarteNaviguation2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpCarteNaviguation2MouseEntered
        // TODO add your handling code here:
        jpCarteNaviguation2.setBackground(ApplicationColors.SIDEBAR_HOVER);

    }//GEN-LAST:event_jpCarteNaviguation2MouseEntered

    private void jpCarteNaviguation2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpCarteNaviguation2MouseExited
        // TODO add your handling code here:
        jpCarteNaviguation2.setBackground(ApplicationColors.PANEL_BG);

    }//GEN-LAST:event_jpCarteNaviguation2MouseExited

    private void jpCarteNaviguation3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpCarteNaviguation3MouseEntered
        // TODO add your handling code here:
        jpCarteNaviguation3.setBackground(ApplicationColors.SIDEBAR_HOVER);

    }//GEN-LAST:event_jpCarteNaviguation3MouseEntered

    private void jpCarteNaviguation3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpCarteNaviguation3MouseExited
        // TODO add your handling code here:
        jpCarteNaviguation3.setBackground(ApplicationColors.PANEL_BG);
    }//GEN-LAST:event_jpCarteNaviguation3MouseExited

    private void jpCarteNaviguation4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpCarteNaviguation4MouseEntered
        // TODO add your handling code here:
        jpCarteNaviguation4.setBackground(ApplicationColors.SIDEBAR_HOVER);
    }//GEN-LAST:event_jpCarteNaviguation4MouseEntered

    private void jpCarteNaviguation4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpCarteNaviguation4MouseExited
        jpCarteNaviguation4.setBackground(ApplicationColors.PANEL_BG);
    }//GEN-LAST:event_jpCarteNaviguation4MouseExited

    private void jbRafraichirPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRafraichirPageActionPerformed
        // TODO add your handling code here:
        chargerDonneesDashBoard();
    }//GEN-LAST:event_jbRafraichirPageActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbRafraichirPage;
    private javax.swing.JLabel jlDetailAlerteProduit;
    private javax.swing.JLabel jlDetailsCarte1_1;
    private javax.swing.JLabel jlDetailsCarte1_2;
    private javax.swing.JLabel jlDetailsCarte2_1;
    private javax.swing.JLabel jlDetailsCarte2_2;
    private javax.swing.JLabel jlDetailsCarte3_1;
    private javax.swing.JLabel jlDetailsCarte3_2;
    private javax.swing.JLabel jlDetailsCarte4_1;
    private javax.swing.JLabel jlDetailsCarte4_2;
    private javax.swing.JLabel jlDetailsCarteNaviguation1;
    private javax.swing.JLabel jlDetailsCarteNaviguation2;
    private javax.swing.JLabel jlDetailsCarteNaviguation3;
    private javax.swing.JLabel jlDetailsCarteNaviguation4;
    private javax.swing.JLabel jlDetailsCommandes;
    private javax.swing.JLabel jlImageCarteNaviguation1;
    private javax.swing.JLabel jlImageCarteNaviguation2;
    private javax.swing.JLabel jlImageCarteNaviguation3;
    private javax.swing.JLabel jlImageCarteNaviguation4;
    private javax.swing.JLabel jlNavigatiopRapide;
    private javax.swing.JPanel jpCarte1;
    private javax.swing.JPanel jpCarte2;
    private javax.swing.JPanel jpCarte3;
    private javax.swing.JPanel jpCarte4;
    private javax.swing.JPanel jpCarteNaviguation1;
    private javax.swing.JPanel jpCarteNaviguation2;
    private javax.swing.JPanel jpCarteNaviguation3;
    private javax.swing.JPanel jpCarteNaviguation4;
    private javax.swing.JPanel jpCartes;
    private javax.swing.JPanel jpCartesNavigationRapide;
    private javax.swing.JPanel jpCommandes;
    private javax.swing.JPanel jpContenuCommandes;
    private javax.swing.JPanel jpContenuProduitAlert;
    private javax.swing.JPanel jpDetailAlerteProduit;
    private javax.swing.JPanel jpDetailsCarte1;
    private javax.swing.JPanel jpDetailsCarte2;
    private javax.swing.JPanel jpDetailsCarte3;
    private javax.swing.JPanel jpDetailsCarte4;
    private javax.swing.JPanel jpDetailsCommandes;
    private javax.swing.JPanel jpDetailsDashorad;
    private javax.swing.JPanel jpImageCarte1;
    private javax.swing.JPanel jpImageCarte2;
    private javax.swing.JPanel jpImageCarte3;
    private javax.swing.JPanel jpImageCarte4;
    private javax.swing.JPanel jpNavigation;
    private javax.swing.JPanel jpProduitsAlertes;
    private javax.swing.JPanel jpTexteNavigation;
    private javax.swing.JScrollPane jspAlerteProduit;
    private javax.swing.JScrollPane jspCommandes;
    // End of variables declaration//GEN-END:variables
}
