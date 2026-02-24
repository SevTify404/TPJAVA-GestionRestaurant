/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package formulaires;

import entity.Users;
import entity.enums.ActionType;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;


import javax.swing.border.EmptyBorder;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import utilitaires.ApplicationColors;
import utilitaires.AuthentificationManager;

/**
 *
 * @author sevtify
 */
public class MenuPrincipalFrame extends JFrame {

    // Composants principaux
    private JPanel sideBar;
    private JPanel mainContent; // Le panel dynamique CardLayout
    private CardLayout cardLayout;

    // Dimensions imposées
    private final int FRAME_WIDTH = FormsUtils.MENU_PRINCIPAL_WIDTH;
    private final int FRAME_HEIGHT = FormsUtils.MENU_PRINCIPAL_HEIGHT;
    private final int SIDEBAR_WIDTH = FormsUtils.MENU_PRINCIPAL_SIDEBAR_WIDTH; 
    
    private JButton boutonSelectionne = null;
    
    private final Users utislisateurConnecte = AuthentificationManager.getInstance().recupererUtilisateurConnecte();
    
    private final Map<String, ImageIcon> iconesDeTousLesBoutons = new HashMap<>();
    
    private final JLabel labelSectionCourante = new JLabel("", SwingConstants.CENTER);

    public MenuPrincipalFrame() {
        initComponents();
        configureFrame();
    }

    private void configureFrame() {
        FormsUtils.configurationDeBaseDeFenetre(this, FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle("Restaurant Maman Rose");
    }

    private void initComponents() {
        
        iconesDeTousLesBoutons.put(
            "DASHBOARD",
            new ImageIcon(getClass().getResource("/images/dashboard.png"))
        );
        
        iconesDeTousLesBoutons.put(
            "INVENTORY",
            new ImageIcon(getClass().getResource("/images/store.png"))
        );
        
        iconesDeTousLesBoutons.put(
            "STOCKS",
            new ImageIcon(getClass().getResource("/images/stock.png"))
        );
        
        iconesDeTousLesBoutons.put(
            "ORDERS",
            new ImageIcon(getClass().getResource("/images/shopping.png"))
        );
        
        iconesDeTousLesBoutons.put(
            "USERS",
            new ImageIcon(getClass().getResource("/images/user.png"))
        );
        
        iconesDeTousLesBoutons.put(
            "STATS", 
            new ImageIcon(getClass().getResource("/images/stats.png"))
        );
        
        iconesDeTousLesBoutons.put(
            "LOGS", 
            new ImageIcon(getClass().getResource("/images/logs.png"))
        );
        
        iconesDeTousLesBoutons.put(
            "SETTINGS", 
            new ImageIcon(getClass().getResource("/images/settings.png"))
        );

        setLayout(new BorderLayout());

        // Sidebar Gauche 
        sideBar = new JPanel();
        sideBar.setPreferredSize(new Dimension(SIDEBAR_WIDTH, FRAME_HEIGHT));
        sideBar.setBackground(ApplicationColors.SIDEBAR_BG);
        sideBar.setLayout(new BorderLayout());

        // Header Sidebar, là où y'aura les bails de Profil Utilisateur
        sideBar.add(createSidebarHeader(), BorderLayout.NORTH);

        // Menu Central
        JPanel menuContainer = new JPanel();
        menuContainer.setOpaque(false);
        menuContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, (int) (FRAME_HEIGHT * 0.01)));

        addNavigationButton(menuContainer, "Tableau de Bord", "DASHBOARD");
        addNavigationButton(menuContainer, "Produits", "INVENTORY");
        addNavigationButton(menuContainer, "Stocks", "STOCKS");
        addNavigationButton(menuContainer, "Commandes", "ORDERS");
        addNavigationButton(menuContainer, "Statistiques", "STATS");
        
        if (utislisateurConnecte.isAdmin()) {
            addNavigationButton(menuContainer, "Utilisateurs", "USERS");
            addNavigationButton(menuContainer, "Audit & Logs", "LOGS");
        }

        sideBar.add(menuContainer, BorderLayout.CENTER);

        // Footer Sidebar là où y'aur le bouton de déconnexion
        sideBar.add(createSidebarFooter(), BorderLayout.SOUTH);

        // Contenu dynamique selon le bouton selectionné dans la sidebar
        // grace à CardLayout
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);
        mainContent.setBackground(ApplicationColors.PANEL_BG);

        // Simulation des sections
        mainContent.add(new DashBoardPanel(), "DASHBOARD");
        mainContent.add(createViewPlaceholder("INVENTORY - Gestion des stocks"), "INVENTORY");
        mainContent.add(new CommandesPanel(), "ORDERS");
        mainContent.add(new StatistiquePanel(), "STATS");
        mainContent.add(new Utilisateurs(), "USERS");
        mainContent.add(new Audit_Log(), "LOGS"); 

        // TOp Bar là où y'aura un bouton de fermeture de l'application et le 
        // titre de la section courante
        JPanel topBar = new JPanel(new BorderLayout());
//        topBar.setBackground(ApplicationColors.SIDEBAR_BG);
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(10, 10, 10, 10));      // un peu comme Margin marge extérieur css
        topBar.setPreferredSize(new Dimension(FRAME_WIDTH - SIDEBAR_WIDTH, FormsUtils.MENU_PRINCIPAL_TOPBAR_HEIGHT));

        JButton btnClose = new JButton(" Fermer L'application ");
        btnClose.setBackground(ApplicationColors.ERROR);
        btnClose.setForeground(ApplicationColors.BACKGROUND);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // reference vers la func callback du bouton
        btnClose.addActionListener(this::btnCloseActionPerformed);
        
        
        
        labelSectionCourante.setFont(new Font("Segoe UI", Font.BOLD, 22));
        labelSectionCourante.setForeground(ApplicationColors.PRIMARY);
        labelSectionCourante.setText("TABLEAU DE BORD");
        
        topBar.add(btnClose, BorderLayout.EAST);
        
        topBar.add(labelSectionCourante, BorderLayout.CENTER);

        // Assemblage final des deux Panel dans celui de droite
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(topBar, BorderLayout.NORTH);
        rightPanel.add(mainContent, BorderLayout.CENTER);

        // Assemblage de la sidebar et le truc central dans la JFrame meme
        add(sideBar, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {
        int res = JOptionPane.showConfirmDialog(
            this,
            "Etes vous sur de vous fermer cette superbe Application ?",
            "Fermeture",
            JOptionPane.YES_NO_OPTION
        );
        
        if (res == JOptionPane.YES_OPTION) {
            AuthentificationManager.getInstance().enregistrerActionDansAudit(ActionType.MODIFICATION, utislisateurConnecte.getLogin() + " a fermé l'application");
            App.getInstance().fermerApp();
        }
    } 

    private JPanel createSidebarHeader() {
        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(SIDEBAR_WIDTH, (int) (FRAME_HEIGHT * 0.3)));
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(30, 20, 20, 20));

        JLabel lblAvatar = new JLabel(
            utislisateurConnecte.getSexe().equals("F") ?
                new ImageIcon(getClass().getResource("/images/woman.png"))
                : 
                new ImageIcon(getClass().getResource("/images/man.png"))
        );
        
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUser = new JLabel(utislisateurConnecte.getLogin());
        
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblUser.setForeground(Color.WHITE);
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblRole = new JLabel(
            utislisateurConnecte.isAdmin() ? "  Administrateur  " : "  Employé  "
        );
        
        lblRole.setOpaque(true);
        lblRole.setBackground(ApplicationColors.SUCCESS);
        lblRole.setForeground(Color.WHITE);
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRole.setAlignmentY(Component.CENTER_ALIGNMENT);

        header.add(lblAvatar);
        header.add(Box.createVerticalStrut(5));
        header.add(lblUser);
        header.add(Box.createVerticalStrut(5));
        header.add(lblRole);

        return header;
    }

    private void addNavigationButton(JPanel parent, String text, String cardName) {
        JButton btn = new JButton("   " + text);
        btn.setIcon(iconesDeTousLesBoutons.get(cardName));
        btn.setPreferredSize(new Dimension((int) (SIDEBAR_WIDTH * 0.92), (int) (FRAME_HEIGHT * 0.055)));
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
        btnLogout.setPreferredSize(new Dimension((int) (SIDEBAR_WIDTH * 0.92), (int) (FRAME_HEIGHT * 0.055)));
        btnLogout.setBackground(ApplicationColors.SUCCESS);
        btnLogout.setForeground(ApplicationColors.BACKGROUND);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogout.addActionListener(this::boutonDeconnexioonActionPerformed);

        footer.add(btnLogout, BorderLayout.CENTER);
        return footer;
    }
    
    private void boutonDeconnexioonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        int res = JOptionPane.showConfirmDialog(
            this,
            "Etes vous sur de vous deconnecter ?",
            "Déconnexion",
            JOptionPane.YES_NO_OPTION
        );
        
        if (res == JOptionPane.YES_OPTION) {
            App.getInstance().fermerSessionUtilisateur();
        }
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
