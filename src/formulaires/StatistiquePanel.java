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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;
import utilitaires.StatsDto.ProduitCA;
import utilitaires.StatsDto.ProduitQuantite;
import utilitaires.StatsDto.PeriodeStatistique;

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
        remplirComboJours(jcbStatJournaliers);
        remplirComboPeriodes(jcbStatPeriode);
        chargementDemarrage();
    }
    
    public void chargementDemarrage(){
        chargerStatsJour(convertirDate(jcbStatJournaliers.getSelectedItem().toString()));
        chargerStatsPeriode(convertirPeriode(jcbStatPeriode.getSelectedItem().toString()));
    }
    
    private void showCustomErreurJOptionPane(String message){
        JOptionPane.showMessageDialog(this, message, "Erreur Commandes", JOptionPane.ERROR_MESSAGE, new ImageIcon(ProductCard.class.getResource("/images/fast-food.png")));
    }
    
    public void chargerStatsCaJour(LocalDate date) {
        // Nettoyage du conteneur avant toute chose
        jpContenuDiagramCaJour.removeAll();

        // Récupération des données (Simulation de ton appel DAO)
        CrudResult<List<ProduitCA>> statsRequest = CommandeDAO.getInstance().chiffreAffaireJourParProduit(date);
        
        if (statsRequest.estUneErreur()) {
            showCustomErreurJOptionPane(statsRequest.getErreur());
            return;
        }

        List<ProduitCA> stats = statsRequest.getDonnes();

        jlDetailsCarte1_1.setText(String.valueOf(bouclepourRecupererTotalCA(stats)));
        if (stats == null || stats.isEmpty()) {
            // Fallback : Message d'information
            JLabel lblEmpty = new JLabel("Aucune vente enregistrée pour ce jour.", JLabel.CENTER);
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblEmpty.setForeground(Color.GRAY);
            jpContenuDiagramCaJour.add(lblEmpty);
        } else {

            afficherGraphiqueCA(stats, jpContenuDiagramCaJour, "CA Par Jour");
        }
    }
    
    public void chargerStatsVentesJour(LocalDate date) {
        jpContenuDiagramVenteJour.removeAll();

     
        CrudResult<List<ProduitQuantite>> statsRequest = CommandeDAO.getInstance().top5ProduitsVendusJour(date);
        
                
        if (statsRequest.estUneErreur()) {
            showCustomErreurJOptionPane(statsRequest.getErreur());
            return;
        }

        List<ProduitQuantite> stats = statsRequest.getDonnes();

        if (stats == null || stats.isEmpty()) {
            JLabel lblEmpty = new JLabel("Aucune vente enregistrée pour ce jour", JLabel.CENTER);
            jpContenuDiagramVenteJour.add(lblEmpty);
        } else {
            afficherGraphiqueQuantites(stats, jpContenuDiagramVenteJour, "Ventes Jour");
        }

        jpContenuDiagramVenteJour.revalidate();
        jpContenuDiagramVenteJour.repaint();
    }
    
    public void chargerStatsVentesPeriode(PeriodeStatistique laPeriode) {
        jpContenuDiagramVentePeriode.removeAll();

     
        CrudResult<List<ProduitQuantite>> statsRequest = CommandeDAO.getInstance().top5ProduitsVendusPeriode(laPeriode);
        
         if (statsRequest.estUneErreur()) {
                showCustomErreurJOptionPane(statsRequest.getErreur());
                return;
            }

        List<ProduitQuantite> stats = statsRequest.getDonnes();

        if (stats == null || stats.isEmpty()) {
            JLabel lblEmpty = new JLabel("Aucune vente enregistrée pour cette période", JLabel.CENTER);
            jpContenuDiagramVentePeriode.add(lblEmpty);
        } else {
            afficherGraphiqueQuantites(stats, jpContenuDiagramVentePeriode, "Ventes Périodes");
        }

        jpContenuDiagramVenteJour.revalidate();
        jpContenuDiagramVenteJour.repaint();
    }
    
    public void chargerStatsCaPeriode(PeriodeStatistique laPeriode) {
        // Nettoyage du conteneur avant toute chose
        jpContenuDiagramCaPeriode.removeAll();

        // Récupération des données (Simulation de ton appel DAO)
        CrudResult<List<ProduitCA>> statsRequest = CommandeDAO.getInstance().chiffreAffaireParProduitPeriode(laPeriode);
        
            if (statsRequest.estUneErreur()) {
                showCustomErreurJOptionPane(statsRequest.getErreur());
                return;
            }

        List<ProduitCA> stats = statsRequest.getDonnes();
        
        jlDetailsCarte1_7.setText(String.valueOf(bouclepourRecupererTotalCA(stats)));

        if (stats == null || stats.isEmpty()) {
            // Fallback : Message d'information
            JLabel lblEmpty = new JLabel("Aucune vente enregistrée pour cette période.", JLabel.CENTER);
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblEmpty.setForeground(Color.GRAY);
            jpContenuDiagramCaPeriode.add(lblEmpty);
        } else {

            afficherGraphiqueCA(stats, jpContenuDiagramCaPeriode, "CA Par Période");
        }
    }
    
    public void chargerStatsPeriode(PeriodeStatistique laPeriode) {
        jlDetailsCarte1_11.setText(String.valueOf(ProduitDAO.getInstance().recupererNombreDeProduits().getDonnes()));
        jlDetailsCarte1_9.setText(String.valueOf(CommandeDAO.getInstance().nombreCommandesPeriode(laPeriode).getDonnes()));
        chargerStatsCaPeriode(laPeriode);
        chargerStatsVentesPeriode(laPeriode);
    }
    
    public void chargerStatsJour(LocalDate dateVoulue) {
        jlDetailsCarte1_5.setText(String.valueOf(ProduitDAO.getInstance().recupererNombreDeProduits().getDonnes()));
        jlDetailsCarte1_3.setText(String.valueOf(CommandeDAO.getInstance().nombreCommandesJour(dateVoulue).getDonnes()));
        chargerStatsCaJour(dateVoulue);
        chargerStatsVentesJour(dateVoulue);
    }
    
    public Double bouclepourRecupererTotalCA(List<ProduitCA> ppppp){
        double total = 0;
        
        for (ProduitCA produitCA : ppppp) {
            total += produitCA.getChiffreAffaire();
        }
        
        return total;
    
    }
    
    
    
    private void afficherGraphiqueCA(List<ProduitCA> data, JPanel destinatiooon, String tectAAffiche) {
      
        List<String> noms = data.stream().map(ProduitCA::getNomProduit).collect(Collectors.toList());
        List<Double> cas = data.stream().map(ProduitCA::getChiffreAffaire).collect(Collectors.toList());

        
        CategoryChart chart = new CategoryChartBuilder()
                .width(600).height(400)
                .title(tectAAffiche)
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

        
        destinatiooon.removeAll();
        destinatiooon.setLayout(new java.awt.BorderLayout());
        XChartPanel<CategoryChart> chartPanel = new XChartPanel<>(chart);
//        chartPanel.setPreferredSize(new java.awt.Dimension(400, 300)); // Force une taille raisonnable
        destinatiooon.add(chartPanel, java.awt.BorderLayout.CENTER);
        destinatiooon.revalidate();
        destinatiooon.repaint();
    }
    
    private void afficherGraphiqueQuantites(List<ProduitQuantite> data, JPanel destinatiooon, String texteAAfficher) {
        // 1. Configuration du graphique
        PieChart chart = new PieChartBuilder()
                .width(400).height(300)
                .title(texteAAfficher)
                .build();

        // 2. Personnalisation du style
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setCircular(true);
//        chart.getStyler().setAnnotation;
//        chart.getStyler(setAnnotationLineColor(Color.orange):Distance(1.15);
        chart.getStyler().setPlotContentSize(0.7);

        // Injection des données
        for (ProduitQuantite pq : data) {
            if (pq.getTotalVendu() > 0) {
                chart.addSeries(pq.getNomProduit(), pq.getTotalVendu());
            }
        }


        destinatiooon.removeAll();
        destinatiooon.setLayout(new java.awt.BorderLayout());
        destinatiooon.add(new XChartPanel<>(chart), java.awt.BorderLayout.CENTER);

        destinatiooon.revalidate();
        destinatiooon.repaint();
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
    
    private void remplirComboJours(javax.swing.JComboBox<String> combo) {
        List<String> jours = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Génère les 30 derniers jours en partant d'aujourd'hui
        for (int i = 0; i < 30; i++) {
            jours.add(LocalDate.now().minusDays(i).format(formatter));
        }

        combo.setModel(new DefaultComboBoxModel<>(jours.toArray(new String[0])));
    }
    
    private void remplirComboPeriodes(javax.swing.JComboBox<String> combo) {
        String[] periodes = {
            "Semaine en cours", 
            "Mois en cours", 
            "Année en cours"
        };
        combo.setModel(new DefaultComboBoxModel<>(periodes));
    }
    
    private PeriodeStatistique convertirPeriode(String valeur) {

        switch (valeur) {

            case "Semaine en cours":
                return PeriodeStatistique.SEMAINE_EN_COURS;

            case "Mois en cours":
                return PeriodeStatistique.MOIS_EN_COURS;

            case "Année en cours":
                return PeriodeStatistique.ANNEE_EN_COURS;

            default:
                throw new IllegalArgumentException("Période invalide");
        }

    }
    private LocalDate convertirDate(String dateString) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return LocalDate.parse(dateString, formatter);
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
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        jpStattsPeriodes = new javax.swing.JPanel();
        jpDetailsStatsPeriode = new javax.swing.JPanel();
        jpTitreStatsPeriode = new javax.swing.JPanel();
        jlTitrePrincipalPanel2 = new javax.swing.JLabel();
        jcbStatPeriode = new javax.swing.JComboBox<>();
        jbRefreshPeriode = new javax.swing.JButton();
        jpContenuStatsPeriode = new javax.swing.JPanel();
        jpCardPeriode = new javax.swing.JPanel();
        jpCarte7 = new javax.swing.JPanel();
        jpImageCarte4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jpDetailsCarte4 = new javax.swing.JPanel();
        jlDetailsCarte1_7 = new javax.swing.JLabel();
        jlDetailsCarte1_8 = new javax.swing.JLabel();
        jpCarte8 = new javax.swing.JPanel();
        jpImageCarte5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jpDetailsCarte5 = new javax.swing.JPanel();
        jlDetailsCarte1_9 = new javax.swing.JLabel();
        jlDetailsCarte1_10 = new javax.swing.JLabel();
        jpCarte9 = new javax.swing.JPanel();
        jpImageCarte6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jpDetailsCarte6 = new javax.swing.JPanel();
        jlDetailsCarte1_11 = new javax.swing.JLabel();
        jlDetailsCarte1_12 = new javax.swing.JLabel();
        jpDiagramsJournaliers1 = new javax.swing.JPanel();
        jpDiagramCAJour1 = new javax.swing.JPanel();
        jlDescriptionDiagramCAJour1 = new javax.swing.JLabel();
        jpContenuDiagramCaPeriode = new javax.swing.JPanel();
        jpDiagramTop5ProdJour1 = new javax.swing.JPanel();
        jlDescriptionDiagramTop5ProdJour1 = new javax.swing.JLabel();
        jpContenuDiagramVentePeriode = new javax.swing.JPanel();

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

        jspGlobal.setPreferredSize(new java.awt.Dimension(500, 500));

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
        jbRefreshJournalier.addActionListener(this::jbRefreshJournalierActionPerformed);
        jpTitreStatsJournaliers.add(jbRefreshJournalier);

        jpDetailsStatsJournaliers.add(jpTitreStatsJournaliers, java.awt.BorderLayout.NORTH);

        jpContenuStatsJournalier.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jpContenuStatsJournalier.setLayout(new javax.swing.BoxLayout(jpContenuStatsJournalier, javax.swing.BoxLayout.Y_AXIS));

        jpCardJours.setBackground(ApplicationColors.BACKGROUND);
        jpCardJours.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jpCardJours.setPreferredSize(new java.awt.Dimension(1089, 100));
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
        jpContenuDiagramVenteJour.setLayout(new java.awt.BorderLayout());
        jpDiagramTop5ProdJour.add(jpContenuDiagramVenteJour, java.awt.BorderLayout.CENTER);

        jpDiagramsJournaliers.add(jpDiagramTop5ProdJour);

        jpContenuStatsJournalier.add(jpDiagramsJournaliers);

        jpDetailsStatsJournaliers.add(jpContenuStatsJournalier, java.awt.BorderLayout.CENTER);

        jpStatsJournalirs.add(jpDetailsStatsJournaliers, java.awt.BorderLayout.CENTER);

        jpConteneurGlobal.add(jpStatsJournalirs);
        jpConteneurGlobal.add(filler2);

        jpStattsPeriodes.setLayout(new java.awt.BorderLayout());

        jpDetailsStatsPeriode.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jpDetailsStatsPeriode.setLayout(new java.awt.BorderLayout());

        jpTitreStatsPeriode.setLayout(new java.awt.GridLayout(1, 3, 20, 0));

        jlTitrePrincipalPanel2.setBackground(ApplicationColors.BACKGROUND);
        jlTitrePrincipalPanel2.setFont(new Font("Segoe UI", Font.BOLD, 22)
        );
        jlTitrePrincipalPanel2.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlTitrePrincipalPanel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlTitrePrincipalPanel2.setText("Statistiques sur une période");
        jlTitrePrincipalPanel2.setToolTipText("");
        jlTitrePrincipalPanel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jpTitreStatsPeriode.add(jlTitrePrincipalPanel2);

        jcbStatPeriode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jpTitreStatsPeriode.add(jcbStatPeriode);

        jbRefreshPeriode.setBackground(ApplicationColors.PRIMARY);
        jbRefreshPeriode.setFont(new Font("Segoe UI", Font.BOLD, 22));
        jbRefreshPeriode.setForeground(ApplicationColors.TEXT_LIGHT);
        jbRefreshPeriode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh-l-light.png"))); // NOI18N
        jbRefreshPeriode.setText("Rafraichir");
        jbRefreshPeriode.addActionListener(this::jbRefreshPeriodeActionPerformed);
        jpTitreStatsPeriode.add(jbRefreshPeriode);

        jpDetailsStatsPeriode.add(jpTitreStatsPeriode, java.awt.BorderLayout.NORTH);

        jpContenuStatsPeriode.setLayout(new javax.swing.BoxLayout(jpContenuStatsPeriode, javax.swing.BoxLayout.Y_AXIS));

        jpCardPeriode.setBackground(ApplicationColors.BACKGROUND);
        jpCardPeriode.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        jpCardPeriode.setPreferredSize(new java.awt.Dimension(1089, 100));
        jpCardPeriode.setLayout(new java.awt.GridLayout(1, 3, 30, 0));

        jpCarte7.setBackground(ApplicationColors.BACKGROUND);
        jpCarte7.setBorder(new javax.swing.border.MatteBorder(null));
        jpCarte7.setLayout(new java.awt.BorderLayout(10, 0));

        jpImageCarte4.setBackground(ApplicationColors.PANEL_BG);
        jpImageCarte4.setLayout(new javax.swing.BoxLayout(jpImageCarte4, javax.swing.BoxLayout.LINE_AXIS));

        jPanel6.setLayout(new java.awt.BorderLayout());

        jLabel7.setBackground(ApplicationColors.BACKGROUND);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/circle-dollar-sign.png"))); // NOI18N
        jPanel6.add(jLabel7, java.awt.BorderLayout.CENTER);

        jpImageCarte4.add(jPanel6);

        jpCarte7.add(jpImageCarte4, java.awt.BorderLayout.WEST);

        jpDetailsCarte4.setBackground(ApplicationColors.BACKGROUND        );
        jpDetailsCarte4.setLayout(new javax.swing.BoxLayout(jpDetailsCarte4, javax.swing.BoxLayout.Y_AXIS));

        jlDetailsCarte1_7.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jlDetailsCarte1_7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte1_7.setText("48");
        jpDetailsCarte4.add(jlDetailsCarte1_7);

        jlDetailsCarte1_8.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jlDetailsCarte1_8.setForeground(ApplicationColors.TEXT_SECONDARY);
        jlDetailsCarte1_8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte1_8.setText("Chiffre d'Affaire");
        jpDetailsCarte4.add(jlDetailsCarte1_8);

        jpCarte7.add(jpDetailsCarte4, java.awt.BorderLayout.CENTER);

        jpCardPeriode.add(jpCarte7);

        jpCarte8.setBackground(ApplicationColors.BACKGROUND);
        jpCarte8.setBorder(new javax.swing.border.MatteBorder(null));
        jpCarte8.setLayout(new java.awt.BorderLayout(10, 0));

        jpImageCarte5.setBackground(ApplicationColors.PANEL_BG);
        jpImageCarte5.setLayout(new javax.swing.BoxLayout(jpImageCarte5, javax.swing.BoxLayout.LINE_AXIS));

        jPanel7.setLayout(new java.awt.BorderLayout());

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/purchase-order.png"))); // NOI18N
        jPanel7.add(jLabel8, java.awt.BorderLayout.CENTER);

        jpImageCarte5.add(jPanel7);

        jpCarte8.add(jpImageCarte5, java.awt.BorderLayout.WEST);

        jpDetailsCarte5.setBackground(ApplicationColors.BACKGROUND        );
        jpDetailsCarte5.setLayout(new javax.swing.BoxLayout(jpDetailsCarte5, javax.swing.BoxLayout.Y_AXIS));

        jlDetailsCarte1_9.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jlDetailsCarte1_9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte1_9.setText("48");
        jpDetailsCarte5.add(jlDetailsCarte1_9);

        jlDetailsCarte1_10.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jlDetailsCarte1_10.setForeground(ApplicationColors.TEXT_SECONDARY);
        jlDetailsCarte1_10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte1_10.setText("Commandes");
        jpDetailsCarte5.add(jlDetailsCarte1_10);

        jpCarte8.add(jpDetailsCarte5, java.awt.BorderLayout.CENTER);

        jpCardPeriode.add(jpCarte8);

        jpCarte9.setBackground(ApplicationColors.BACKGROUND);
        jpCarte9.setBorder(new javax.swing.border.MatteBorder(null));
        jpCarte9.setLayout(new java.awt.BorderLayout(10, 0));

        jpImageCarte6.setBackground(ApplicationColors.PANEL_BG);
        jpImageCarte6.setLayout(new javax.swing.BoxLayout(jpImageCarte6, javax.swing.BoxLayout.LINE_AXIS));

        jPanel8.setLayout(new java.awt.BorderLayout());

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/in-stock (1).png"))); // NOI18N
        jPanel8.add(jLabel9, java.awt.BorderLayout.CENTER);

        jpImageCarte6.add(jPanel8);

        jpCarte9.add(jpImageCarte6, java.awt.BorderLayout.WEST);

        jpDetailsCarte6.setBackground(ApplicationColors.BACKGROUND        );
        jpDetailsCarte6.setLayout(new javax.swing.BoxLayout(jpDetailsCarte6, javax.swing.BoxLayout.Y_AXIS));

        jlDetailsCarte1_11.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jlDetailsCarte1_11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte1_11.setText("48");
        jpDetailsCarte6.add(jlDetailsCarte1_11);

        jlDetailsCarte1_12.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jlDetailsCarte1_12.setForeground(ApplicationColors.TEXT_SECONDARY);
        jlDetailsCarte1_12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlDetailsCarte1_12.setText("Total Produits");
        jpDetailsCarte6.add(jlDetailsCarte1_12);

        jpCarte9.add(jpDetailsCarte6, java.awt.BorderLayout.CENTER);

        jpCardPeriode.add(jpCarte9);

        jpContenuStatsPeriode.add(jpCardPeriode);

        jpDiagramsJournaliers1.setBackground(ApplicationColors.BACKGROUND);
        jpDiagramsJournaliers1.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        jpDiagramCAJour1.setBorder(javax.swing.BorderFactory.createEmptyBorder(7, 7, 7, 7));
        jpDiagramCAJour1.setLayout(new java.awt.BorderLayout());

        jlDescriptionDiagramCAJour1.setFont(new Font("Segoe UI", Font.BOLD, 15));
        jlDescriptionDiagramCAJour1.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlDescriptionDiagramCAJour1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlDescriptionDiagramCAJour1.setText("Chiffre d'Affaire de La Période Par Produit");
        jpDiagramCAJour1.add(jlDescriptionDiagramCAJour1, java.awt.BorderLayout.NORTH);

        jpContenuDiagramCaPeriode.setBackground(ApplicationColors.BACKGROUND);
        jpContenuDiagramCaPeriode.setLayout(new java.awt.BorderLayout());
        jpDiagramCAJour1.add(jpContenuDiagramCaPeriode, java.awt.BorderLayout.CENTER);

        jpDiagramsJournaliers1.add(jpDiagramCAJour1);

        jpDiagramTop5ProdJour1.setBorder(javax.swing.BorderFactory.createEmptyBorder(7, 7, 7, 7));
        jpDiagramTop5ProdJour1.setLayout(new java.awt.BorderLayout());

        jlDescriptionDiagramTop5ProdJour1.setFont(new Font("Segoe UI", Font.BOLD, 15));
        jlDescriptionDiagramTop5ProdJour1.setForeground(ApplicationColors.TEXT_PRIMARY);
        jlDescriptionDiagramTop5ProdJour1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlDescriptionDiagramTop5ProdJour1.setText("Top 5 Produits Vendus De La Période");
        jpDiagramTop5ProdJour1.add(jlDescriptionDiagramTop5ProdJour1, java.awt.BorderLayout.NORTH);

        jpContenuDiagramVentePeriode.setBackground(ApplicationColors.BACKGROUND);
        jpContenuDiagramVentePeriode.setLayout(new java.awt.BorderLayout());
        jpDiagramTop5ProdJour1.add(jpContenuDiagramVentePeriode, java.awt.BorderLayout.CENTER);

        jpDiagramsJournaliers1.add(jpDiagramTop5ProdJour1);

        jpContenuStatsPeriode.add(jpDiagramsJournaliers1);

        jpDetailsStatsPeriode.add(jpContenuStatsPeriode, java.awt.BorderLayout.CENTER);

        jpStattsPeriodes.add(jpDetailsStatsPeriode, java.awt.BorderLayout.CENTER);

        jpConteneurGlobal.add(jpStattsPeriodes);

        jspGlobal.setViewportView(jpConteneurGlobal);

        add(jspGlobal, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jbRefreshPeriodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRefreshPeriodeActionPerformed
     
        chargerStatsPeriode(convertirPeriode(jcbStatPeriode.getSelectedItem().toString()));
    }//GEN-LAST:event_jbRefreshPeriodeActionPerformed

    private void jbRefreshJournalierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRefreshJournalierActionPerformed
        chargerStatsJour(convertirDate(jcbStatJournaliers.getSelectedItem().toString()));
    }//GEN-LAST:event_jbRefreshJournalierActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbRefreshJournalier;
    private javax.swing.JButton jbRefreshPeriode;
    private javax.swing.JComboBox<String> jcbStatJournaliers;
    private javax.swing.JComboBox<String> jcbStatPeriode;
    private javax.swing.JLabel jlDescription;
    private javax.swing.JLabel jlDescriptionDiagramCAJour;
    private javax.swing.JLabel jlDescriptionDiagramCAJour1;
    private javax.swing.JLabel jlDescriptionDiagramTop5ProdJour;
    private javax.swing.JLabel jlDescriptionDiagramTop5ProdJour1;
    private javax.swing.JLabel jlDetailsCarte1_1;
    private javax.swing.JLabel jlDetailsCarte1_10;
    private javax.swing.JLabel jlDetailsCarte1_11;
    private javax.swing.JLabel jlDetailsCarte1_12;
    private javax.swing.JLabel jlDetailsCarte1_2;
    private javax.swing.JLabel jlDetailsCarte1_3;
    private javax.swing.JLabel jlDetailsCarte1_4;
    private javax.swing.JLabel jlDetailsCarte1_5;
    private javax.swing.JLabel jlDetailsCarte1_6;
    private javax.swing.JLabel jlDetailsCarte1_7;
    private javax.swing.JLabel jlDetailsCarte1_8;
    private javax.swing.JLabel jlDetailsCarte1_9;
    private javax.swing.JLabel jlTitrePrincipalPanel;
    private javax.swing.JLabel jlTitrePrincipalPanel1;
    private javax.swing.JLabel jlTitrePrincipalPanel2;
    private javax.swing.JPanel jpCardJours;
    private javax.swing.JPanel jpCardPeriode;
    private javax.swing.JPanel jpCarte4;
    private javax.swing.JPanel jpCarte5;
    private javax.swing.JPanel jpCarte6;
    private javax.swing.JPanel jpCarte7;
    private javax.swing.JPanel jpCarte8;
    private javax.swing.JPanel jpCarte9;
    private javax.swing.JPanel jpConteneurGlobal;
    private javax.swing.JPanel jpConteneurTitre;
    private javax.swing.JPanel jpContenuDiagramCaJour;
    private javax.swing.JPanel jpContenuDiagramCaPeriode;
    private javax.swing.JPanel jpContenuDiagramVenteJour;
    private javax.swing.JPanel jpContenuDiagramVentePeriode;
    private javax.swing.JPanel jpContenuStatsJournalier;
    private javax.swing.JPanel jpContenuStatsPeriode;
    private javax.swing.JPanel jpDetailsCarte1;
    private javax.swing.JPanel jpDetailsCarte2;
    private javax.swing.JPanel jpDetailsCarte3;
    private javax.swing.JPanel jpDetailsCarte4;
    private javax.swing.JPanel jpDetailsCarte5;
    private javax.swing.JPanel jpDetailsCarte6;
    private javax.swing.JPanel jpDetailsStatsJournaliers;
    private javax.swing.JPanel jpDetailsStatsPeriode;
    private javax.swing.JPanel jpDiagramCAJour;
    private javax.swing.JPanel jpDiagramCAJour1;
    private javax.swing.JPanel jpDiagramTop5ProdJour;
    private javax.swing.JPanel jpDiagramTop5ProdJour1;
    private javax.swing.JPanel jpDiagramsJournaliers;
    private javax.swing.JPanel jpDiagramsJournaliers1;
    private javax.swing.JPanel jpImageCarte1;
    private javax.swing.JPanel jpImageCarte2;
    private javax.swing.JPanel jpImageCarte3;
    private javax.swing.JPanel jpImageCarte4;
    private javax.swing.JPanel jpImageCarte5;
    private javax.swing.JPanel jpImageCarte6;
    private javax.swing.JPanel jpStatsJournalirs;
    private javax.swing.JPanel jpStattsPeriodes;
    private javax.swing.JPanel jpTitre;
    private javax.swing.JPanel jpTitreStatsJournaliers;
    private javax.swing.JPanel jpTitreStatsPeriode;
    private javax.swing.JScrollPane jspGlobal;
    // End of variables declaration//GEN-END:variables
}
