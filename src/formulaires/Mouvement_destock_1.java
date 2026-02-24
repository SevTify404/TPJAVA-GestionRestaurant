/*
 * Mouvement_destock.java — RestoManager
 * ─────────────────────────────────────
 * 100 % pur Java (sans GUI Builder NetBeans) pour un layout parfaitement contrôlé.
 *
 * Classes utilisées (celles du VRAI projet) :
 *   • MouvementDeStockDAO.enregistrer()   / .recupererTout()  → CrudResult<...>
 *   • ProduitDAO.recupererTout()           / .mettreAJour()   → CrudResult<...>
 *   • MouvementDeStock : getID/getTYPE/getQUANTITE/getDATEMOUVEMENT/getMOTIF
 *   • MouvementDeStock.TypeMouvement.ENTREE / SORTIE
 *   • Produit : getIdProduit/getNom/getStockActuel/getSeuilAlerte/setStockActuel
 *   • AuditDAO.log(...)
 *   • AuthentificationManager.getInstance().recupererUtilisateurConnecte()
 */
package formulaires;

import dao.AuditDAO;
import dao.CrudResult;
import dao.MouvementDeStockDAO;
import dao.ProduitDAO;
import entity.MouvementDeStock;
import entity.Produit;
import utilitaires.ApplicationColors;
import utilitaires.AuthentificationManager;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Mouvement_destock_1 extends JPanel {

    // ── DAOs ──────────────────────────────────────────────────────────────────
    private final ProduitDAO          produitDAO = new ProduitDAO();
    private final MouvementDeStockDAO mvtDAO     = new MouvementDeStockDAO();
    private final AuditDAO            auditDAO   = new AuditDAO();

    // ── Formulaire ────────────────────────────────────────────────────────────
    private JToggleButton      btnEntree, btnSortie;
    private JComboBox<Produit> cmbProduit;
    private JSpinner           spnQuantite;
    private JTextArea          txtMotif;
    private JLabel             lblStockDispo;
    private JButton            btnEnregistrer;

    // ── Tables ────────────────────────────────────────────────────────────────
    private DefaultTableModel modelHistorique;
    private DefaultTableModel modelAlertes;

    // =========================================================================
    //  CONSTRUCTEUR
    // =========================================================================
    public Mouvement_destock_1() {
        setLayout(new BorderLayout(16, 0));
        setBackground(ApplicationColors.BACKGROUND);
        setBorder(new EmptyBorder(18, 18, 18, 18));

        add(buildFormulaire(), BorderLayout.WEST);
        add(buildDroite(),     BorderLayout.CENTER);

        setupToggles();
        chargerDonnees();
    }

    // =========================================================================
    //  PANNEAU GAUCHE — formulaire d'enregistrement
    // =========================================================================
    private JPanel buildFormulaire() {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(280, 0));
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(22, 20, 22, 20)
        ));

        // ── Titre ─────────────────────────────────────────────────────────────
        JLabel titre = lbl("Enregistrer un Mouvement", 15, Font.BOLD, ApplicationColors.TEXT_PRIMARY);
        card.add(titre);
        card.add(gap(18));

        // ── Séparateur ────────────────────────────────────────────────────────
        card.add(separateur());
        card.add(gap(14));

        // ── Type de mouvement ─────────────────────────────────────────────────
        card.add(lbl("Type de Mouvement", 12, Font.BOLD, ApplicationColors.TEXT_SECONDARY));
        card.add(gap(8));

        btnEntree = new JToggleButton("↓  Entrée");
        btnSortie = new JToggleButton("↑  Sortie");

        JPanel rowToggle = new JPanel(new GridLayout(1, 2, 8, 0));
        rowToggle.setOpaque(false);
        rowToggle.setAlignmentX(LEFT_ALIGNMENT);
        rowToggle.setMaximumSize(new Dimension(Short.MAX_VALUE, 38));
        rowToggle.add(btnEntree);
        rowToggle.add(btnSortie);
        card.add(rowToggle);
        card.add(gap(16));

        // ── Produit ───────────────────────────────────────────────────────────
        card.add(lbl("Produit", 12, Font.BOLD, ApplicationColors.TEXT_SECONDARY));
        card.add(gap(6));

        cmbProduit = new JComboBox<>();
        cmbProduit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbProduit.setBackground(Color.WHITE);
        cmbProduit.setMaximumSize(new Dimension(Short.MAX_VALUE, 36));
        cmbProduit.setAlignmentX(LEFT_ALIGNMENT);
        cmbProduit.setRenderer(new ProduitRenderer());
        cmbProduit.addActionListener(e -> rafraichirStockDispo());
        card.add(cmbProduit);
        card.add(gap(5));

        // Feedback stock disponible
        lblStockDispo = new JLabel(" ");
        lblStockDispo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStockDispo.setForeground(new Color(100, 116, 139));
        lblStockDispo.setAlignmentX(LEFT_ALIGNMENT);
        card.add(lblStockDispo);
        card.add(gap(14));

        // ── Quantité ──────────────────────────────────────────────────────────
        card.add(lbl("Quantité", 12, Font.BOLD, ApplicationColors.TEXT_SECONDARY));
        card.add(gap(6));

        spnQuantite = new JSpinner(new SpinnerNumberModel(1, 1, 99999, 1));
        spnQuantite.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spnQuantite.setMaximumSize(new Dimension(Short.MAX_VALUE, 36));
        spnQuantite.setAlignmentX(LEFT_ALIGNMENT);
        card.add(spnQuantite);
        card.add(gap(16));

        // ── Motif ─────────────────────────────────────────────────────────────
        card.add(lbl("Motif (optionnel)", 12, Font.BOLD, ApplicationColors.TEXT_SECONDARY));
        card.add(gap(6));

        txtMotif = new JTextArea(3, 20);
        txtMotif.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMotif.setLineWrap(true);
        txtMotif.setWrapStyleWord(true);
        txtMotif.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane spMotif = new JScrollPane(txtMotif);
        spMotif.setBorder(new LineBorder(new Color(209, 213, 219), 1, true));
        spMotif.setAlignmentX(LEFT_ALIGNMENT);
        spMotif.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));
        card.add(spMotif);
        card.add(gap(22));

        // ── Bouton Enregistrer ────────────────────────────────────────────────
        btnEnregistrer = new JButton("  Enregistrer le Mouvement");
        btnEnregistrer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEnregistrer.setForeground(Color.WHITE);
        btnEnregistrer.setBackground(ApplicationColors.PRIMARY);
        btnEnregistrer.setFocusPainted(false);
        btnEnregistrer.setBorderPainted(false);
        btnEnregistrer.setOpaque(true);
        btnEnregistrer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEnregistrer.setAlignmentX(LEFT_ALIGNMENT);
        btnEnregistrer.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));
        btnEnregistrer.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnEnregistrer.setBackground(ApplicationColors.PRIMARY_DARK); }
            public void mouseExited(MouseEvent e)  { btnEnregistrer.setBackground(ApplicationColors.PRIMARY); }
        });
        btnEnregistrer.addActionListener(e -> enregistrerMouvement());
        card.add(btnEnregistrer);

        return card;
    }

    // =========================================================================
    //  PANNEAU DROIT — historique + alertes
    // =========================================================================
    private JPanel buildDroite() {
        JPanel col = new JPanel(new BorderLayout(0, 14));
        col.setOpaque(false);

        // ── Card Historique ───────────────────────────────────────────────────
        JPanel cardH = new JPanel(new BorderLayout());
        cardH.setBackground(Color.WHITE);
        cardH.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));

        // En-tête historique
        JPanel headH = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 12));
        headH.setBackground(Color.WHITE);
        headH.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));
        JLabel titreH = new JLabel("Historique des Mouvements");
        titreH.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titreH.setForeground(ApplicationColors.TEXT_PRIMARY);
        headH.add(titreH);
        cardH.add(headH, BorderLayout.NORTH);

        // Table
        String[] colsH = {"ID", "Date & Heure", "Produit", "Type", "Qté", "Motif"};
        modelHistorique = new DefaultTableModel(colsH, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblH = new JTable(modelHistorique);
        styleTable(tblH, Color.WHITE);
        tblH.getColumnModel().getColumn(3).setCellRenderer(new BadgeTypeRenderer());
        int[] wH = {40, 135, 160, 85, 45, 0};
        for (int i = 0; i < wH.length; i++)
            if (wH[i] > 0) tblH.getColumnModel().getColumn(i).setPreferredWidth(wH[i]);

        JScrollPane spH = new JScrollPane(tblH);
        spH.setBorder(null);
        cardH.add(spH, BorderLayout.CENTER);

        // ── Card Alertes ──────────────────────────────────────────────────────
        JPanel cardA = new JPanel(new BorderLayout());
        cardA.setBackground(new Color(255, 248, 248));
        cardA.setBorder(new LineBorder(new Color(254, 202, 202), 1, true));
        cardA.setPreferredSize(new Dimension(0, 210));

        // En-tête alertes
        JPanel headA = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        headA.setBackground(new Color(255, 237, 237));
        headA.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(254, 202, 202)));
        JLabel icoA = new JLabel("⚠");
        icoA.setFont(new Font("Segoe UI", Font.BOLD, 14));
        icoA.setForeground(ApplicationColors.ERROR);
        JLabel titreA = new JLabel("Produits en dessous du seuil d'alerte");
        titreA.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titreA.setForeground(ApplicationColors.ERROR);
        headA.add(icoA);
        headA.add(titreA);
        cardA.add(headA, BorderLayout.NORTH);

        // Table alertes
        String[] colsA = {"Produit", "Stock Actuel", "Seuil d'alerte", "Statut"};
        modelAlertes = new DefaultTableModel(colsA, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblA = new JTable(modelAlertes);
        styleTable(tblA, new Color(255, 248, 248));

        // Stock actuel en rouge gras
        tblA.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            { setHorizontalAlignment(CENTER); }
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                super.getTableCellRendererComponent(t, v, s, f, r, c);
                setForeground(ApplicationColors.ERROR);
                setFont(getFont().deriveFont(Font.BOLD));
                setBackground(s ? t.getSelectionBackground() : new Color(255, 248, 248));
                return this;
            }
        });
        tblA.getColumnModel().getColumn(3).setCellRenderer(new BadgeBasRenderer());

        JScrollPane spA = new JScrollPane(tblA);
        spA.setBorder(null);
        spA.getViewport().setBackground(new Color(255, 248, 248));
        cardA.add(spA, BorderLayout.CENTER);

        col.add(cardH, BorderLayout.CENTER);
        col.add(cardA, BorderLayout.SOUTH);

        return col;
    }

    // =========================================================================
    //  setupToggles()
    // =========================================================================
    private void setupToggles() {
        ButtonGroup g = new ButtonGroup();
        g.add(btnEntree);
        g.add(btnSortie);
        btnEntree.setSelected(true);
        styleToggle(btnEntree, true);
        styleToggle(btnSortie, false);

        btnEntree.addActionListener(e -> {
            styleToggle(btnEntree, true);
            styleToggle(btnSortie, false);
            rafraichirStockDispo();
        });
        btnSortie.addActionListener(e -> {
            styleToggle(btnEntree, false);
            styleToggle(btnSortie, true);
            rafraichirStockDispo();
        });
    }

    // =========================================================================
    //  chargerDonnees()
    // =========================================================================
    private void chargerDonnees() {
        // ── Produits ──────────────────────────────────────────────────────────
        Produit selection = (Produit) cmbProduit.getSelectedItem();
        cmbProduit.removeAllItems();
        cmbProduit.addItem(null);

        CrudResult<List<Produit>> resProd = produitDAO.recupererTout();
        if (resProd.estUneErreur()) {
            erreur("Impossible de charger les produits :\n" + resProd.getErreur());
        } else {
            for (Produit p : resProd.getDonnes()) {
                cmbProduit.addItem(p);
                // Restaurer la sélection précédente si elle existe encore
                if (selection != null && p.getIdProduit() == selection.getIdProduit())
                    cmbProduit.setSelectedItem(p);
            }
        }

        // ── Historique ────────────────────────────────────────────────────────
        modelHistorique.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        CrudResult<List<MouvementDeStock>> resMvt = mvtDAO.recupererTout();
        if (resMvt.estUnSucces()) {
            for (MouvementDeStock m : resMvt.getDonnes()) {
                modelHistorique.addRow(new Object[]{
                    m.getID(),
                    m.getDATEMOUVEMENT() != null ? m.getDATEMOUVEMENT().format(fmt) : "—",
                    m.getProduit() != null ? m.getProduit().getNom() : "—",
                    m.getTYPE() == MouvementDeStock.TypeMouvement.ENTREE ? "Entrée" : "Sortie",
                    m.getQUANTITE(),
                    m.getMOTIF() != null ? m.getMOTIF() : ""
                });
            }
        }

        // ── Alertes (filtrées côté client) ────────────────────────────────────
        modelAlertes.setRowCount(0);
        if (resProd.estUnSucces()) {
            for (Produit p : resProd.getDonnes()) {
                if (p.getStockActuel() <= p.getSeuilAlerte()) {
                    modelAlertes.addRow(new Object[]{
                        p.getNom(), p.getStockActuel(), p.getSeuilAlerte(), "Bas"
                    });
                }
            }
        }

        rafraichirStockDispo();
    }

    // =========================================================================
    //  rafraichirStockDispo() — feedback sous la ComboBox
    // =========================================================================
    private void rafraichirStockDispo() {
        Object sel = cmbProduit.getSelectedItem();
        if (sel instanceof Produit) {
            Produit p = (Produit) sel;
            int stock = p.getStockActuel();
            boolean sortie = btnSortie != null && btnSortie.isSelected();
            String txt = "Stock disponible : " + stock + " unité(s)";
            lblStockDispo.setText(txt);
            lblStockDispo.setForeground(
                (sortie && stock == 0) ? ApplicationColors.ERROR
                : (stock <= p.getSeuilAlerte()) ? new Color(234, 88, 12)   // orange si alerte
                : new Color(22, 163, 74)                                    // vert si OK
            );
        } else {
            lblStockDispo.setText("Sélectionnez un produit");
            lblStockDispo.setForeground(new Color(148, 163, 184));
        }
    }

    // =========================================================================
    //  enregistrerMouvement()
    // =========================================================================
    private void enregistrerMouvement() {
        // Validation produit
        Object sel = cmbProduit.getSelectedItem();
        if (!(sel instanceof Produit)) {
            warning("Veuillez sélectionner un produit.");
            return;
        }
        Produit produit = (Produit) sel;
        int qte = (Integer) spnQuantite.getValue();

        MouvementDeStock.TypeMouvement type = btnEntree.isSelected()
            ? MouvementDeStock.TypeMouvement.ENTREE
            : MouvementDeStock.TypeMouvement.SORTIE;

        // Validation stock pour sortie
        if (type == MouvementDeStock.TypeMouvement.SORTIE && qte > produit.getStockActuel()) {
            erreur("Stock insuffisant !\n"
                + "Quantité demandée : " + qte + "\n"
                + "Stock disponible  : " + produit.getStockActuel());
            return;
        }

        // Créer le mouvement
        MouvementDeStock m = new MouvementDeStock();
        m.setProduit(produit);
        m.setTYPE(type);
        m.setQUANTITE(qte);
        m.setDATEMOUVEMENT(LocalDateTime.now());
        m.setMOTIF(txtMotif.getText().trim());

        // Enregistrer en BD
        CrudResult<Boolean> res = mvtDAO.enregistrer(m);
        if (res.estUneErreur()) {
            erreur("Erreur lors de l'enregistrement :\n" + res.getErreur());
            return;
        }

        // Mettre à jour le stock du produit
        int nvStock = type == MouvementDeStock.TypeMouvement.ENTREE
            ? produit.getStockActuel() + qte
            : produit.getStockActuel() - qte;
        produit.setStockActuel(nvStock);
        CrudResult<?> resMaj = produitDAO.mettreAJour(produit);
        if (resMaj.estUneErreur()) {
            erreur("Mouvement enregistré mais erreur mise à jour stock :\n" + resMaj.getErreur());
        }

        // Log audit (non bloquant)
        try {
            String login = AuthentificationManager.getInstance()
                .recupererUtilisateurConnecte().getLogin();
            auditDAO.log(login, "MOUVEMENT", "Stock",
                (type == MouvementDeStock.TypeMouvement.ENTREE ? "Entrée" : "Sortie")
                + " ×" + qte + " — " + produit.getNom()
                + " → stock : " + nvStock);
        } catch (Exception ignored) {}

        // Reset + rechargement
        spnQuantite.setValue(1);
        txtMotif.setText("");
        chargerDonnees();

        JOptionPane.showMessageDialog(this,
            "✓  Mouvement enregistré avec succès !\n"
            + "Produit : " + produit.getNom() + "\n"
            + "Nouveau stock : " + nvStock + " unité(s)",
            "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================================================================
    //  UTILITAIRES UI
    // =========================================================================

    /** Crée un JLabel stylé aligné à gauche */
    private JLabel lbl(String texte, int size, int style, Color couleur) {
        JLabel l = new JLabel(texte);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(couleur);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    /** Espace vertical */
    private Component gap(int h) { return Box.createVerticalStrut(h); }

    /** Séparateur horizontal */
    private JSeparator separateur() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(226, 232, 240));
        sep.setAlignmentX(LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(Short.MAX_VALUE, 1));
        return sep;
    }

    /** Style visuel d'un toggle */
    private void styleToggle(JToggleButton btn, boolean actif) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorderPainted(true);
        if (actif) {
            btn.setBackground(ApplicationColors.PRIMARY);
            btn.setForeground(Color.WHITE);
            btn.setBorder(new LineBorder(ApplicationColors.PRIMARY_DARK, 1));
        } else {
            btn.setBackground(new Color(241, 245, 249));
            btn.setForeground(new Color(100, 116, 139));
            btn.setBorder(new LineBorder(new Color(203, 213, 225), 1));
        }
    }

    /** Style commun pour les JTable */
    private void styleTable(JTable t, Color bg) {
        t.setRowHeight(38);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setBackground(bg);
        t.setSelectionBackground(new Color(224, 242, 254));
        t.setSelectionForeground(ApplicationColors.TEXT_PRIMARY);
        t.setFillsViewportHeight(true);

        JTableHeader h = t.getTableHeader();
        h.setBackground(new Color(241, 245, 249));
        h.setForeground(new Color(100, 116, 139));
        h.setFont(new Font("Segoe UI", Font.BOLD, 12));
        h.setReorderingAllowed(false);
        h.setPreferredSize(new Dimension(0, 38));
        ((DefaultTableCellRenderer) h.getDefaultRenderer())
            .setHorizontalAlignment(SwingConstants.LEFT);
    }

    private void erreur(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void warning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Attention", JOptionPane.WARNING_MESSAGE);
    }

    // =========================================================================
    //  RENDERER — Affiche le nom du produit dans la ComboBox
    // =========================================================================
    private static class ProduitRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Produit) {
                Produit p = (Produit) value;
                setText(p.getNom() + "  (stock : " + p.getStockActuel() + ")");
            } else {
                setText("— Sélectionner un produit —");
            }
            return this;
        }
    }

    // =========================================================================
    //  RENDERER — Badge Entrée (bleu) / Sortie (rouge)
    // =========================================================================
    private static class BadgeTypeRenderer extends DefaultTableCellRenderer {
        { setHorizontalAlignment(CENTER); }
        public Component getTableCellRendererComponent(
                JTable t, Object v, boolean s, boolean f, int r, int c) {
            String type = v != null ? v.toString() : "";
            JLabel b = new JLabel(type);
            b.setOpaque(true);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setFont(new Font("Segoe UI", Font.BOLD, 11));
            b.setBorder(new EmptyBorder(3, 12, 3, 12));
            if ("Entrée".equals(type)) {
                b.setBackground(new Color(219, 234, 254));
                b.setForeground(new Color(29, 78, 216));
            } else {
                b.setBackground(new Color(254, 226, 226));
                b.setForeground(new Color(185, 28, 28));
            }
            JPanel w = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 7));
            w.setBackground(s ? t.getSelectionBackground() : Color.WHITE);
            w.add(b);
            return w;
        }
    }

    // =========================================================================
    //  RENDERER — Badge "Bas" rouge pour la table alertes
    // =========================================================================
    private static class BadgeBasRenderer extends DefaultTableCellRenderer {
        { setHorizontalAlignment(CENTER); }
        public Component getTableCellRendererComponent(
                JTable t, Object v, boolean s, boolean f, int r, int c) {
            JLabel b = new JLabel("⚠ Bas");
            b.setOpaque(true);
            b.setBackground(new Color(254, 226, 226));
            b.setForeground(new Color(185, 28, 28));
            b.setFont(new Font("Segoe UI", Font.BOLD, 11));
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setBorder(new EmptyBorder(3, 10, 3, 10));
            JPanel w = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 7));
            w.setBackground(s ? t.getSelectionBackground() : new Color(255, 248, 248));
            w.add(b);
            return w;
        }
    }
}
