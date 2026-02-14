/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package formulaires;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import utilitaires.ApplicationColors;
import utilitaires.AuthentificationManager;

/**
 *
 * @author sevtify
 */
public class MenuPrincipalFrame extends JFrame {

    // Composants principaux
    private JPanel sideBar;
    private JPanel mainContent; // Le panel dynamique (CardLayout)
    private CardLayout cardLayout;

    // Dimensions imposées
    private final int FRAME_WIDTH = 1700;
    private final int FRAME_HEIGHT = 1000;
    private final int SIDEBAR_WIDTH = 320; // Ratio équilibré pour 1700px
    
    private JButton boutonSelectionne = null;
    
    private String utislisateurConnecte = AuthentificationManager.getInstance().recupererUtilisateurConnecte();
    
    private final Map<String, ImageIcon> navigationIcons = new HashMap<>();
    
    private JLabel labelSectionCourante = new JLabel("", SwingConstants.CENTER);

    public MenuPrincipalFrame() {
        initComponents();
        configureFrame();
    }

    private void configureFrame() {
        Utils.configurationDeBaseDeFenetre(this, FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void initComponents() {
        
        navigationIcons.put(
            "DASHBOARD",
            new ImageIcon(getClass().getResource("/images/dashboard.png"))
        );
        navigationIcons.put(
            "INVENTORY",
            new ImageIcon(getClass().getResource("/images/store.png"))
        );
        navigationIcons.put(
            "STOCKS",
            new ImageIcon(getClass().getResource("/images/stock.png"))
        );
        navigationIcons.put(
            "ORDERS",
            new ImageIcon(getClass().getResource("/images/shopping.png"))
        );
        navigationIcons.put(
            "USERS",
            new ImageIcon(getClass().getResource("/images/user.png"))
        );
        navigationIcons.put(
            "STATS", 
            new ImageIcon(getClass().getResource("/images/stats.png"))
        );
        navigationIcons.put(
            "LOGS", 
            new ImageIcon(getClass().getResource("/images/logs.png"))
        );
        navigationIcons.put(
            "SETTINGS", 
            new ImageIcon(getClass().getResource("/images/settings.png"))
        );

        setLayout(new BorderLayout());

        // --- 1. SIDEBAR (GAUCHE) ---
        sideBar = new JPanel();
        sideBar.setPreferredSize(new Dimension(SIDEBAR_WIDTH, FRAME_HEIGHT));
        sideBar.setBackground(ApplicationColors.SIDEBAR_BG);
        sideBar.setLayout(new BorderLayout());

        // Header Sidebar (Profil Utilisateur)
        sideBar.add(createSidebarHeader(), BorderLayout.NORTH);

        // Menu Central
        JPanel menuContainer = new JPanel();
        menuContainer.setOpaque(false);
        menuContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 18));

        addNavigationButton(menuContainer, "Tableau de Bord", "DASHBOARD");
        addNavigationButton(menuContainer, "Produits", "INVENTORY");
        addNavigationButton(menuContainer, "Stocks", "STOCKS");
        addNavigationButton(menuContainer, "Commandes", "ORDERS");
        addNavigationButton(menuContainer, "Utilisateurs", "USERS");
        addNavigationButton(menuContainer, "Statistiques", "STATS");
        addNavigationButton(menuContainer, "Audit & Logs", "LOGS");
        addNavigationButton(menuContainer, "Paramètres Personnels", "SETTINGS");

        sideBar.add(menuContainer, BorderLayout.CENTER);

        // Footer Sidebar (Logout)
        sideBar.add(createSidebarFooter(), BorderLayout.SOUTH);

        // --- 2. CONTENU DYNAMIQUE (DROITE) ---
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setBackground(ApplicationColors.PANEL_BG);

        // Simulation des sections (À remplacer par vos JPanels définitifs)
        mainContent.add(createViewPlaceholder("DASHBOARD - Aperçu général"), "DASHBOARD");
        mainContent.add(createViewPlaceholder("INVENTORY - Gestion des stocks"), "INVENTORY");
        mainContent.add(createViewPlaceholder("ORDERS - Gestion des commandes"), "ORDERS");
        mainContent.add(createViewPlaceholder("STATS - Rapports d'activité"), "STATS");
        mainContent.add(createViewPlaceholder("SETTINGS - Configuration système"), "SETTINGS");

        // --- 3. TOP BAR (FERMETURE) ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(ApplicationColors.SIDEBAR_BG);
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(20, 20, 20, 20));
        topBar.setPreferredSize(new Dimension(FRAME_WIDTH - SIDEBAR_WIDTH, 70));

        JButton btnClose = new JButton("Fermer L'application ✕");
        btnClose.setBackground(ApplicationColors.ERROR);
        btnClose.setForeground(ApplicationColors.BACKGROUND);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this, "Etes vous sur de vous fermer cette superbe Application ?", "Fermeture", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        
        
        labelSectionCourante.setFont(new Font("Segoe UI", Font.BOLD, 22));
        labelSectionCourante.setForeground(ApplicationColors.PRIMARY);
        labelSectionCourante.setText("TABLEAU DE BORD");

        topBar.add(btnClose, BorderLayout.EAST);
        topBar.add(labelSectionCourante, BorderLayout.CENTER);

        // --- ASSEMBLAGE ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(topBar, BorderLayout.NORTH);
        rightPanel.add(mainContent, BorderLayout.CENTER);

        add(sideBar, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebarHeader() {
        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 250));
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(30, 20, 20, 20));

        JLabel lblAvatar = new JLabel(new ImageIcon(getClass().getResource("/images/woman.png"))); // Remplacer par une icône image plus tard
        
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUser = new JLabel(utislisateurConnecte);
        
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblUser.setForeground(Color.WHITE);
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRole = new JLabel("  Administrator  ");
        lblRole.setOpaque(true);
        lblRole.setBackground(ApplicationColors.SUCCESS);
        lblRole.setForeground(Color.WHITE);
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(lblAvatar);
        header.add(Box.createVerticalStrut(10));
        header.add(lblUser);
        header.add(Box.createVerticalStrut(5));
        header.add(lblRole);

        return header;
    }

    private void addNavigationButton(JPanel parent, String text, String cardName) {
        JButton btn = new JButton("   " + text);
        btn.setIcon(navigationIcons.get(cardName));
        btn.setPreferredSize(new Dimension(SIDEBAR_WIDTH - 20, 50));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btn.setForeground(ApplicationColors.TEXT_PRIMARY);
        btn.setBackground(ApplicationColors.BACKGROUND);
        btn.setBorder(new EmptyBorder(0, 20, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn != boutonSelectionne) {
                    btn.setBackground(new Color(52, 73, 94));
                    btn.setForeground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn != boutonSelectionne) {
                    btn.setForeground(ApplicationColors.TEXT_PRIMARY);
                    btn.setBackground(ApplicationColors.BACKGROUND);
                }
            }
            
        });
        // Pour voir ce qui est cliquer et mettr a jour la sidebar
        btn.addActionListener(e -> {
            if (boutonSelectionne != null) {
                boutonSelectionne.setForeground(ApplicationColors.TEXT_PRIMARY);
                boutonSelectionne.setBackground(ApplicationColors.BACKGROUND);
            }


            boutonSelectionne = btn;
            btn.setBackground(ApplicationColors.PRIMARY);
            btn.setForeground(Color.WHITE);
            labelSectionCourante.setText(btn.getText().strip().toUpperCase());

            cardLayout.show(mainContent, cardName);
        });

        btn.addActionListener(e -> cardLayout.show(mainContent, cardName));
        parent.add(btn);
    }

    private JPanel createSidebarFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton btnLogout = new JButton("Déconnexion");
        btnLogout.setPreferredSize(new Dimension(280, 40));
        btnLogout.setBackground(ApplicationColors.SUCCESS);
        btnLogout.setForeground(ApplicationColors.BACKGROUND);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogout.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this, "Etes vous sur de vous deconnecter ?", "Déconnexion", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                AuthentificationManager.getInstance().deconnecterUtilisateurActuel();
            }
        });

        footer.add(btnLogout, BorderLayout.WEST);
        return footer;
    }

    private JPanel createViewPlaceholder(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(ApplicationColors.PANEL_BG);
        JLabel l = new JLabel(title, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 24));
        l.setForeground(ApplicationColors.TEXT_SECONDARY);
        p.add(l, BorderLayout.CENTER);
        return p;
    }
}
