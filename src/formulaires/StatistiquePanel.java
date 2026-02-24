/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formulaires;
import dao.CommandeDAO;
import dao.CrudResult;
import dao.ProduitDAO;
import java.awt.Color;
import utilitaires.ApplicationColors;
import java.awt.Font;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;
import utilitaires.StatsDto.ProduitCA;
import utilitaires.StatsDto.ProduitQuantite;

/**
 *
 * @author sevtify
 */
public class StatistiquePanel extends javax.swing.JPanel {

    /**
     * Creates new form StatistiquePanel
     */
    public StatistiquePanel() {
        initComponents();
        initCustomComponents();
    }
    
    
    private void initCustomComponents(){

            jspGlobal.getVerticalScrollBar().setUnitIncrement(16);
        chargerStats();
        chargerStatsVentes();
          jspGlobal.setVerticalScrollBarPolicy(
    javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
);
jspGlobal.setHorizontalScrollBarPolicy(
    javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
);
    }
    
    
    public void chargerStats() {
        // Nettoyage du conteneur avant toute chose
        jpContenuDiagramCaJour.removeAll();

        // Récupération des données (Simulation de ton appel DAO)
        CrudResult<List<ProduitCA>> statsRequest = CommandeDAO.getInstance().chiffreAffaireJourParProduit(LocalDate.now());
        
            if (statsRequest.estUneErreur()) {
                System.out.println(statsRequest.getErreur());
                return;
            }

        List<ProduitCA> stats = getMockData();

        if (stats == null || stats.isEmpty()) {
            // Fallback : Message d'information
            JLabel lblEmpty = new JLabel("Aucune vente enregistrée pour ce jour.", JLabel.CENTER);
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblEmpty.setForeground(Color.GRAY);
            jpContenuDiagramCaJour.add(lblEmpty);
        } else {

            afficherGraphiqueCA(stats);
        }
    }
    
    private void afficherGraphiqueCA(List<ProduitCA> data) {
      
        List<String> noms = data.stream().map(ProduitCA::getNomProduit).collect(Collectors.toList());
        List<Double> cas = data.stream().map(ProduitCA::getChiffreAffaire).collect(Collectors.toList());

        
        CategoryChart chart = new CategoryChartBuilder()
                .width(600).height(400)
                .title("Chiffre d'Affaire du Jour par Produit")
                .xAxisTitle("Produits")
                .yAxisTitle("FCFA")
                .build();


        chart.getStyler().setAvailableSpaceFill(.96);
        chart.getStyler().setOverlapped(true);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
//        chart.getStyler().setHasAnnotations(true);

        chart.getStyler().setLabelsVisible(true);
        chart.getStyler().setXAxisLabelRotation(45); // Rotation des Noms en Abcisse

        
        chart.addSeries("CA", noms, cas);

        
        jpContenuDiagramCaJour.removeAll();
        jpContenuDiagramCaJour.setLayout(new java.awt.BorderLayout());
        XChartPanel<CategoryChart> chartPanel = new XChartPanel<>(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(400, 300)); // Force une taille raisonnable
        jpContenuDiagramCaJour.add(chartPanel, java.awt.BorderLayout.CENTER);
        jpContenuDiagramCaJour.revalidate();
        jpContenuDiagramCaJour.repaint();
    }
    
    private void afficherGraphiqueQuantites(List<ProduitQuantite> data) {
        // 1. Configuration du graphique
        PieChart chart = new PieChartBuilder()
                .width(400).height(300)
                .title("Répartition des ventes par produit")
                .build();

        // 2. Personnalisation du style
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setCircular(true);
//        chart.getStyler().setAnnotation;
//        chart.getStyler(setAnnotationLineColor(Color.orange):Distance(1.15);
        chart.getStyler().setPlotContentSize(0.7);

        // 3. Injection des données
        for (ProduitQuantite pq : data) {
            if (pq.getTotalVendu() > 0) {
                chart.addSeries(pq.getNomProduit(), pq.getTotalVendu());
            }
        }

        // 4. Intégration dans le panel (nettoyage préalable)
        jpContenuDiagramVenteJour.removeAll();
        jpContenuDiagramVenteJour.setLayout(new java.awt.BorderLayout());
        jpContenuDiagramVenteJour.add(new XChartPanel<>(chart), java.awt.BorderLayout.CENTER);

        jpContenuDiagramVenteJour.revalidate();
        jpContenuDiagramVenteJour.repaint();
    }
    
    public void chargerStatsVentes() {
        jpContenuDiagramVenteJour.removeAll();

     
        List<ProduitQuantite> stats = getMockVentes();

        if (stats == null || stats.isEmpty()) {
            JLabel lblEmpty = new JLabel("Aucune vente enregistrée pour ce jour", JLabel.CENTER);
            jpContenuDiagramVenteJour.add(lblEmpty);
        } else {
            afficherGraphiqueQuantites(stats);
        }

        jpContenuDiagramVenteJour.revalidate();
        jpContenuDiagramVenteJour.repaint();
    }
    
    public List<ProduitQuantite> getMockVentes() {
        List<ProduitQuantite> mock = new java.util.ArrayList<>();
        mock.add(new ProduitQuantite(1, "Burger", 25));
        mock.add(new ProduitQuantite(2, "Pizza", 12));
        mock.add(new ProduitQuantite(3, "Soda", 40));
        mock.add(new ProduitQuantite(4, "Frites", 30));
        return mock;
    }
    
    
    public List<ProduitCA> getMockData() {
        List<ProduitCA> mockList = new ArrayList<>();
        mockList.add(new ProduitCA(1, "Burger Classique", 15000));
        mockList.add(new ProduitCA(2, "Pizza Royale", 28000));
        mockList.add(new ProduitCA(3, "Coca-Cola 50cl", 4500));
        mockList.add(new ProduitCA(4, "Frites Maison", 7200));
        mockList.add(new ProduitCA(5, "Salade César", 9500));

        return mockList;
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpConteneurTitre = new javax.swing.JPanel();
        jpTitre = new javax.swing.JPanel();
        jlTitrePrincipalPanel = new javax.swing.JLabel();
        jlDescription = new javax.swing.JLabel();
        jspGlobal = new javax.swing.JScrollPane();
        jpConteneurGlobal = new javax.swing.JPanel();
        jpStatsJournalirs = new javax.swing.JPanel();
        jpDetailsStatsJournaliers = new javax.swing.JPanel();
        jpTitreStatsJournaliers = new javax.swing.JPanel();
        jlTitrePrincipalPanel1 = new javax.swing.JLabel();
        jcbStatJournaliers = new javax.swing.JComboBox<>();
        jbRefreshJournalier = new javax.swing.JButton();
        jpContenuStatsJournalier = new javax.swing.JPanel();
        jpCardJours = new javax.swing.JPanel();
        jpCarte4 = new javax.swing.JPanel();
        jpImageCarte1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jpDetailsCarte1 = new javax.swing.JPanel();
        jlDetailsCarte1_1 = new javax.swing.JLabel();
        jlDetailsCarte1_2 = new javax.swing.JLabel();
        jpCarte5 = new javax.swing.JPanel();
        jpImageCarte2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jpDetailsCarte2 = new javax.swing.JPanel();
        jlDetailsCarte1_3 = new javax.swing.JLabel();
        jlDetailsCarte1_4 = new javax.swing.JLabel();
        jpCarte6 = new javax.swing.JPanel();
        jpImageCarte3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jpDetailsCarte3 = new javax.swing.JPanel();
        jlDetailsCarte1_5 = new javax.swing.JLabel();
        jlDetailsCarte1_6 = new javax.swing.JLabel();
        jpDiagramsJournaliers = new javax.swing.JPanel();
        jpDiagramCAJour = new javax.swing.JPanel();
        jlDescriptionDiagramCAJour = new javax.swing.JLabel();
        jpContenuDiagramCaJour = new javax.swing.JPanel();
        jpDiagramTop5ProdJour = new javax.swing.JPanel();
        jlDescriptionDiagramTop5ProdJour = new javax.swing.JLabel();
        jpContenuDiagramVenteJour = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(30, 0), new java.awt.Dimension(30, 0), new java.awt.Dimension(30, 0));

        setLayout(new java.awt.BorderLayout(0, 5));

        jpConteneurTitre.setPreferredSize(new java.awt.Dimension(1151, 100));
        jpConteneurTitre.setLayout(new java.awt.BorderLayout());

        jpTitre.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jpTitre.setLayout(new java.awt.BorderLayout());

        jlTitrePrincipalPanel.setBackground(ApplicationColors.BACKGROUND);
        jlTitrePrincipalPanel.setFont(new Font("Segoe UI", Font.BOLD, 30)
        );
        jlTitrePrincipalPanel.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlTitrePrincipalPanel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlTitrePrincipalPanel.setText("Statisiques de l'Appllication");
        jlTitrePrincipalPanel.setToolTipText("");
        jlTitrePrincipalPanel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jpTitre.add(jlTitrePrincipalPanel, java.awt.BorderLayout.NORTH);

        jlDescription.setBackground(ApplicationColors.BACKGROUND);
        jlDescription.setFont(new Font("Segoe UI", Font.PLAIN, 20)
        );
        jlDescription.setForeground(ApplicationColors.TEXT_SECONDARY);
        jlDescription.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlDescription.setText("Informations sur les Ventes et Produits selon Jours et Périodes");
        jpTitre.add(jlDescription, java.awt.BorderLayout.LINE_END);

        jpConteneurTitre.add(jpTitre, java.awt.BorderLayout.WEST);

        add(jpConteneurTitre, java.awt.BorderLayout.NORTH);

        jpConteneurGlobal.setMinimumSize(new java.awt.Dimension(100, 100));
        jpConteneurGlobal.setPreferredSize(new java.awt.Dimension(1000, 1000));
        jpConteneurGlobal.setLayout(new javax.swing.BoxLayout(jpConteneurGlobal, javax.swing.BoxLayout.Y_AXIS));

        jpStatsJournalirs.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jpStatsJournalirs.setPreferredSize(new java.awt.Dimension(1000, 600));
        jpStatsJournalirs.setLayout(new java.awt.BorderLayout());

        jpDetailsStatsJournaliers.setMinimumSize(new java.awt.Dimension(649, 100));
        jpDetailsStatsJournaliers.setPreferredSize(new java.awt.Dimension(1119, 80));
        jpDetailsStatsJournaliers.setLayout(new java.awt.BorderLayout());

        jpTitreStatsJournaliers.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jpTitreStatsJournaliers.setMinimumSize(new java.awt.Dimension(649, 100));
        jpTitreStatsJournaliers.setPreferredSize(new java.awt.Dimension(223, 70));
        jpTitreStatsJournaliers.setLayout(new java.awt.GridLayout(1, 3, 20, 0));

        jlTitrePrincipalPanel1.setBackground(ApplicationColors.BACKGROUND);
        jlTitrePrincipalPanel1.setFont(new Font("Segoe UI", Font.BOLD, 22)
        );
        jlTitrePrincipalPanel1.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlTitrePrincipalPanel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlTitrePrincipalPanel1.setText("Statistiques sur un jour précis");
        jlTitrePrincipalPanel1.setToolTipText("");
        jlTitrePrincipalPanel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jpTitreStatsJournaliers.add(jlTitrePrincipalPanel1);

        jcbStatJournaliers.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jpTitreStatsJournaliers.add(jcbStatJournaliers);

        jbRefreshJournalier.setBackground(ApplicationColors.PRIMARY);
        jbRefreshJournalier.setFont(new Font("Segoe UI", Font.BOLD, 22));
        jbRefreshJournalier.setForeground(ApplicationColors.TEXT_LIGHT);
        jbRefreshJournalier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh-l-light.png"))); // NOI18N
        jbRefreshJournalier.setText("Rafraichir");
        jpTitreStatsJournaliers.add(jbRefreshJournalier);

        jpDetailsStatsJournaliers.add(jpTitreStatsJournaliers, java.awt.BorderLayout.NORTH);

        jpContenuStatsJournalier.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jpContenuStatsJournalier.setLayout(new javax.swing.BoxLayout(jpContenuStatsJournalier, javax.swing.BoxLayout.Y_AXIS));

        jpCardJours.setBackground(ApplicationColors.BACKGROUND);
        jpCardJours.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jpCardJours.setPreferredSize(new java.awt.Dimension(1089, 111));
        jpCardJours.setLayout(new java.awt.GridLayout(1, 3, 30, 0));

        jpCarte4.setBackground(ApplicationColors.BACKGROUND);
        jpCarte4.setBorder(new javax.swing.border.MatteBorder(null));
        jpCarte4.setLayout(new java.awt.BorderLayout(10, 0));

        jpImageCarte1.setBackground(ApplicationColors.PANEL_BG);
        jpImageCarte1.setLayout(new javax.swing.BoxLayout(jpImageCarte1, javax.swing.BoxLayout.LINE_AXIS));

        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel4.setBackground(ApplicationColors.BACKGROUND);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/circle-dollar-sign.png"))); // NOI18N
        jPanel3.add(jLabel4, java.awt.BorderLayout.CENTER);

        jpImageCarte1.add(jPanel3);

        jpCarte4.add(jpImageCarte1, java.awt.BorderLayout.WEST);

        jpDetailsCarte1.setBackground(ApplicationColors.BACKGROUND        );
        jpDetailsCarte1.setLayout(new javax.swing.BoxLayout(jpDetailsCarte1, javax.swing.BoxLayout.Y_AXIS));

        jlDetailsCarte1_1.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jlDetailsCarte1_1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte1_1.setText("48");
        jpDetailsCarte1.add(jlDetailsCarte1_1);

        jlDetailsCarte1_2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jlDetailsCarte1_2.setForeground(ApplicationColors.TEXT_SECONDARY);
        jlDetailsCarte1_2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte1_2.setText("Chiffre d'Affaire");
        jpDetailsCarte1.add(jlDetailsCarte1_2);

        jpCarte4.add(jpDetailsCarte1, java.awt.BorderLayout.CENTER);

        jpCardJours.add(jpCarte4);

        jpCarte5.setBackground(ApplicationColors.BACKGROUND);
        jpCarte5.setBorder(new javax.swing.border.MatteBorder(null));
        jpCarte5.setLayout(new java.awt.BorderLayout(10, 0));

        jpImageCarte2.setBackground(ApplicationColors.PANEL_BG);
        jpImageCarte2.setLayout(new javax.swing.BoxLayout(jpImageCarte2, javax.swing.BoxLayout.LINE_AXIS));

        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/purchase-order.png"))); // NOI18N
        jPanel4.add(jLabel5, java.awt.BorderLayout.CENTER);

        jpImageCarte2.add(jPanel4);

        jpCarte5.add(jpImageCarte2, java.awt.BorderLayout.WEST);

        jpDetailsCarte2.setBackground(ApplicationColors.BACKGROUND        );
        jpDetailsCarte2.setLayout(new javax.swing.BoxLayout(jpDetailsCarte2, javax.swing.BoxLayout.Y_AXIS));

        jlDetailsCarte1_3.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jlDetailsCarte1_3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte1_3.setText("48");
        jpDetailsCarte2.add(jlDetailsCarte1_3);

        jlDetailsCarte1_4.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jlDetailsCarte1_4.setForeground(ApplicationColors.TEXT_SECONDARY);
        jlDetailsCarte1_4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte1_4.setText("Commandes");
        jpDetailsCarte2.add(jlDetailsCarte1_4);

        jpCarte5.add(jpDetailsCarte2, java.awt.BorderLayout.CENTER);

        jpCardJours.add(jpCarte5);

        jpCarte6.setBackground(ApplicationColors.BACKGROUND);
        jpCarte6.setBorder(new javax.swing.border.MatteBorder(null));
        jpCarte6.setLayout(new java.awt.BorderLayout(10, 0));

        jpImageCarte3.setBackground(ApplicationColors.PANEL_BG);
        jpImageCarte3.setLayout(new javax.swing.BoxLayout(jpImageCarte3, javax.swing.BoxLayout.LINE_AXIS));

        jPanel5.setLayout(new java.awt.BorderLayout());

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/in-stock (1).png"))); // NOI18N
        jPanel5.add(jLabel6, java.awt.BorderLayout.CENTER);

        jpImageCarte3.add(jPanel5);

        jpCarte6.add(jpImageCarte3, java.awt.BorderLayout.WEST);

        jpDetailsCarte3.setBackground(ApplicationColors.BACKGROUND        );
        jpDetailsCarte3.setLayout(new javax.swing.BoxLayout(jpDetailsCarte3, javax.swing.BoxLayout.Y_AXIS));

        jlDetailsCarte1_5.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jlDetailsCarte1_5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte1_5.setText("48");
        jpDetailsCarte3.add(jlDetailsCarte1_5);

        jlDetailsCarte1_6.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jlDetailsCarte1_6.setForeground(ApplicationColors.TEXT_SECONDARY);
        jlDetailsCarte1_6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte1_6.setText("Total Produits");
        jpDetailsCarte3.add(jlDetailsCarte1_6);

        jpCarte6.add(jpDetailsCarte3, java.awt.BorderLayout.CENTER);

        jpCardJours.add(jpCarte6);

        jpContenuStatsJournalier.add(jpCardJours);

        jpDiagramsJournaliers.setBackground(ApplicationColors.BACKGROUND);
        jpDiagramsJournaliers.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        jpDiagramCAJour.setBorder(javax.swing.BorderFactory.createEmptyBorder(7, 7, 7, 7));
        jpDiagramCAJour.setLayout(new java.awt.BorderLayout());

        jlDescriptionDiagramCAJour.setFont(new Font("Segoe UI", Font.BOLD, 15));
        jlDescriptionDiagramCAJour.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlDescriptionDiagramCAJour.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlDescriptionDiagramCAJour.setText("Chiffre d'Affaire du Jour Par Produit");
        jpDiagramCAJour.add(jlDescriptionDiagramCAJour, java.awt.BorderLayout.NORTH);

        jpContenuDiagramCaJour.setBackground(ApplicationColors.BACKGROUND);
        jpContenuDiagramCaJour.setPreferredSize(new java.awt.Dimension(1122, 500));
        jpContenuDiagramCaJour.setLayout(new java.awt.BorderLayout());
        jpDiagramCAJour.add(jpContenuDiagramCaJour, java.awt.BorderLayout.CENTER);

        jpDiagramsJournaliers.add(jpDiagramCAJour);

        jpDiagramTop5ProdJour.setBorder(javax.swing.BorderFactory.createEmptyBorder(7, 7, 7, 7));
        jpDiagramTop5ProdJour.setLayout(new java.awt.BorderLayout());

        jlDescriptionDiagramTop5ProdJour.setFont(new Font("Segoe UI", Font.BOLD, 15));
        jlDescriptionDiagramTop5ProdJour.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlDescriptionDiagramTop5ProdJour.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlDescriptionDiagramTop5ProdJour.setText("Top 5 Produits Vendus Du Jour");
        jpDiagramTop5ProdJour.add(jlDescriptionDiagramTop5ProdJour, java.awt.BorderLayout.NORTH);

        jpContenuDiagramVenteJour.setBackground(ApplicationColors.BACKGROUND);
        jpContenuDiagramVenteJour.setMinimumSize(new java.awt.Dimension(1122, 500));
        jpContenuDiagramVenteJour.setPreferredSize(new java.awt.Dimension(1122, 500));
        jpContenuDiagramVenteJour.setLayout(new java.awt.BorderLayout());
        jpDiagramTop5ProdJour.add(jpContenuDiagramVenteJour, java.awt.BorderLayout.CENTER);

        jpDiagramsJournaliers.add(jpDiagramTop5ProdJour);

        jpContenuStatsJournalier.add(jpDiagramsJournaliers);

        jpDetailsStatsJournaliers.add(jpContenuStatsJournalier, java.awt.BorderLayout.CENTER);

        jpStatsJournalirs.add(jpDetailsStatsJournaliers, java.awt.BorderLayout.CENTER);

        jpConteneurGlobal.add(jpStatsJournalirs);
        jpConteneurGlobal.add(filler1);

        jspGlobal.setViewportView(jpConteneurGlobal);

        add(jspGlobal, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton jbRefreshJournalier;
    private javax.swing.JComboBox<String> jcbStatJournaliers;
    private javax.swing.JLabel jlDescription;
    private javax.swing.JLabel jlDescriptionDiagramCAJour;
    private javax.swing.JLabel jlDescriptionDiagramTop5ProdJour;
    private javax.swing.JLabel jlDetailsCarte1_1;
    private javax.swing.JLabel jlDetailsCarte1_2;
    private javax.swing.JLabel jlDetailsCarte1_3;
    private javax.swing.JLabel jlDetailsCarte1_4;
    private javax.swing.JLabel jlDetailsCarte1_5;
    private javax.swing.JLabel jlDetailsCarte1_6;
    private javax.swing.JLabel jlTitrePrincipalPanel;
    private javax.swing.JLabel jlTitrePrincipalPanel1;
    private javax.swing.JPanel jpCardJours;
    private javax.swing.JPanel jpCarte4;
    private javax.swing.JPanel jpCarte5;
    private javax.swing.JPanel jpCarte6;
    private javax.swing.JPanel jpConteneurGlobal;
    private javax.swing.JPanel jpConteneurTitre;
    private javax.swing.JPanel jpContenuDiagramCaJour;
    private javax.swing.JPanel jpContenuDiagramVenteJour;
    private javax.swing.JPanel jpContenuStatsJournalier;
    private javax.swing.JPanel jpDetailsCarte1;
    private javax.swing.JPanel jpDetailsCarte2;
    private javax.swing.JPanel jpDetailsCarte3;
    private javax.swing.JPanel jpDetailsStatsJournaliers;
    private javax.swing.JPanel jpDiagramCAJour;
    private javax.swing.JPanel jpDiagramTop5ProdJour;
    private javax.swing.JPanel jpDiagramsJournaliers;
    private javax.swing.JPanel jpImageCarte1;
    private javax.swing.JPanel jpImageCarte2;
    private javax.swing.JPanel jpImageCarte3;
    private javax.swing.JPanel jpStatsJournalirs;
    private javax.swing.JPanel jpTitre;
    private javax.swing.JPanel jpTitreStatsJournaliers;
    private javax.swing.JScrollPane jspGlobal;
    // End of variables declaration//GEN-END:variables
}
