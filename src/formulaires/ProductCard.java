/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package formulaires;

import entity.Produit;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;


import javax.swing.border.EmptyBorder;

import java.awt.geom.RoundRectangle2D;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import utilitaires.ApplicationColors;
import utilitaires.RoundedButton;
/**
 *
 * @author sevtify
 */
public class ProductCard extends JPanel {

    private float elevation = 4f;
    private boolean hovered = false;
    private static final int RADIUS = 16;

    public ProductCard(Produit leProduit, ActionListener onAddToCart) {
        setLayout(new BorderLayout(0, 12));
        setOpaque(false); // On peint tout manuellement
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // ── Badge catégorie ──────────────────────────────────────────
        JLabel lblCategorie = new JLabel(leProduit.getCategorie().getLIBELLE().toUpperCase());
        lblCategorie.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblCategorie.setForeground(ApplicationColors.PRIMARY);
        lblCategorie.setOpaque(true);
        lblCategorie.setBackground(new Color(33, 150, 243, 20));
        lblCategorie.setBorder(new EmptyBorder(3, 8, 3, 8));

        // ── Badge stock ──────────────────────────────────────────────
        boolean alerteStock = leProduit.getStockActuel() < leProduit.getSeuilAlerte();
        Color stockColor  = alerteStock ? ApplicationColors.ERROR   : ApplicationColors.SUCCESS;
        Color stockBg     = alerteStock ? new Color(211, 47, 47, 15) : new Color(76, 175, 80, 15);
        String stockIcon  = alerteStock ? "⚠ " : "✓ ";

        JLabel lblStock = new JLabel(stockIcon + "Stock : " + leProduit.getStockActuel());
        lblStock.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblStock.setForeground(stockColor);
        lblStock.setOpaque(true);
        lblStock.setBackground(stockBg);
        lblStock.setBorder(new EmptyBorder(3, 8, 3, 8));

        // ── Ligne catégorie + stock ──────────────────────────────────
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(lblCategorie, BorderLayout.WEST);
        topRow.add(lblStock, BorderLayout.EAST);

        // ── Nom du produit ───────────────────────────────────────────
        JLabel lblNom = new JLabel(leProduit.getNom());
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblNom.setForeground(ApplicationColors.TEXT_PRIMARY);
        lblNom.setVerticalAlignment(SwingConstants.CENTER);
        lblNom.setHorizontalAlignment(SwingConstants.CENTER);

        // ── Séparateur fin ───────────────────────────────────────────
        JSeparator sep = new JSeparator();
        sep.setForeground(ApplicationColors.BORDER);

        // ── Prix ─────────────────────────────────────────────────────
        JLabel lblPrice = new JLabel(String.valueOf(leProduit.getPrixDeVente()));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblPrice.setForeground(ApplicationColors.SUCCESS);

        JLabel lblPriceCaption = new JLabel("Prix unitaire");
        lblPriceCaption.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPriceCaption.setForeground(ApplicationColors.TEXT_SECONDARY);

        JPanel pricePanel = new JPanel(new BorderLayout(0, 2));
        pricePanel.setOpaque(false);
        pricePanel.add(lblPrice, BorderLayout.CENTER);
        pricePanel.add(lblPriceCaption, BorderLayout.SOUTH);

        // ── Bloc central ─────────────────────────────────────────────
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(lblNom);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(sep);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(pricePanel);

        // ── Bouton Ajouter ───────────────────────────────────────────
        RoundedButton btnAjouter = new RoundedButton("  Ajouter", RADIUS - 4);
        btnAjouter.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.setBackground(ApplicationColors.PRIMARY);
        btnAjouter.setHoverBackground(ApplicationColors.PRIMARY_DARK);
        try {
            ImageIcon icon = new ImageIcon(ProductCard.class.getResource("/images/plus-sm.png"));
            btnAjouter.setIcon(icon);
        } catch (Exception ignored) {}
        btnAjouter.setIconTextGap(8);
        btnAjouter.setCursor(Cursor.getDefaultCursor().getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAjouter.setPreferredSize(new Dimension(0, 40));
        
        btnAjouter.addActionListener(e -> {
            onAddToCart.actionPerformed(e);
        });

        // ── Assemblage ───────────────────────────────────────────────
        add(topRow,      BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(btnAjouter,  BorderLayout.SOUTH);
        
        

//        
    }

    // ── Peinture personnalisée : ombre + fond blanc arrondi ──────────
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int shadow = (int) elevation;
        int w = getWidth()  - shadow * 2;
        int h = getHeight() - shadow * 2;

        // Fond blanc de la carte
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(shadow, shadow, w, h, RADIUS, RADIUS));

        // Bordure subtile
        g2.setColor(hovered ? new Color(33, 150, 243, 60) : new Color(224, 224, 224));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(new RoundRectangle2D.Float(shadow + 0.6f, shadow + 0.6f, w - 1.2f, h - 1.2f, RADIUS, RADIUS));

        g2.dispose();
        super.paintComponent(g);
    }


}

// Fallback
//public class ProductCard extends JPanel {
//    public ProductCard(String name, String category, String price, int stock, int seuilAlerte) {
//        setLayout(new BorderLayout(10, 10));
//        setBackground(Color.WHITE);
//        setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
//                new EmptyBorder(15, 15, 15, 15)
//        ));
//
//        // Panneau texte
//        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 5));
//        textPanel.setOpaque(false);
//        
//        JLabel lblCategorie = new JLabel(category);
//        lblCategorie.setForeground(ApplicationColors.TEXT_SECONDARY);
//        textPanel.add(lblCategorie); // Catégorie
//        
//        JLabel lblNom = new JLabel(name);
//        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        lblNom.setForeground(ApplicationColors.TEXT_PRIMARY);
//        textPanel.add(lblNom);                   // Nom
//        
//        JLabel lblPrice = new JLabel(price);               // Prix
//        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        lblPrice.setForeground(ApplicationColors.SUCCESS);
//        textPanel.add(lblPrice);
//
//        // Affichage du stock (Emplacement en haut à droite)
//        JLabel lblStock = new JLabel("Stock: " + stock);
//        lblStock.setForeground(stock < seuilAlerte ? ApplicationColors.ERROR : ApplicationColors.TEXT_SECONDARY);
//        
//        add(textPanel, BorderLayout.CENTER);
//        add(lblStock, BorderLayout.NORTH);
//        JButton btnAjouter = new JButton("Ajouter");
//        btnAjouter.setForeground(ApplicationColors.TEXT_LIGHT);
//        btnAjouter.setFont(new Font("Segoe UI", Font.BOLD, 16));
//        btnAjouter.setBackground(ApplicationColors.INFO);
//        btnAjouter.setBorderPainted(false);
//        btnAjouter.setIcon(new ImageIcon(ProductCard.class.getResource("/images/plus-sm.png")));
//        btnAjouter.setIconTextGap(10);
//        
//        add(btnAjouter, BorderLayout.SOUTH);
//
//        // Effet surélévation au survol
//        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                setBorder(BorderFactory.createCompoundBorder(
//                        BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
//                        new EmptyBorder(15, 15, 15, 15)
//                ));
//            }
//            @Override
//            public void mouseExited(MouseEvent e) {
//                setBorder(BorderFactory.createCompoundBorder(
//                        BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
//                        new EmptyBorder(15, 15, 15, 15)
//                ));
//            }
//        });
//    }
//}
