package formulaires;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import utilitaires.ApplicationColors;

/**
 * Menu Principal - Architecture dynamique avec CardLayout
 *
 * @author Gemini
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

    public MenuPrincipalFrame() {
        initComponents();
        configureFrame();
    }

    private void configureFrame() {
        Utils.configurationDeBaseDeFenetre(this, FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void initComponents() {
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
        menuContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

        addNavigationButton(menuContainer, "Tableau de Bord", "DASHBOARD");
        addNavigationButton(menuContainer, "Produits", "INVENTORY");
        addNavigationButton(menuContainer, "Stocks", "STOCKS");
        addNavigationButton(menuContainer, "Commandes", "ORDERS");
        addNavigationButton(menuContainer, "Utilisateurs", "USERS");
        addNavigationButton(menuContainer, "Statistiques", "STATS");
        addNavigationButton(menuContainer, "Audit & Logs", "LOGS");

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
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.setBackground(ApplicationColors.BACKGROUND);
        topBar.setPreferredSize(new Dimension(FRAME_WIDTH - SIDEBAR_WIDTH, 50));

        JButton btnClose = new JButton("✕");
        btnClose.setFont(new Font("Arial", Font.BOLD, 20));
        btnClose.setForeground(ApplicationColors.TEXT_SECONDARY);
        btnClose.setBorder(null);
        btnClose.setContentAreaFilled(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> System.exit(0));

        topBar.add(btnClose);

        // --- ASSEMBLAGE ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(topBar, BorderLayout.NORTH);
        rightPanel.add(mainContent, BorderLayout.CENTER);

        add(sideBar, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebarHeader() {
        JPanel header = new JPanel();
        header.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 180));
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(30, 20, 20, 20));

        JLabel lblAvatar = new JLabel("👤"); // Remplacer par une icône image plus tard
        lblAvatar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/user.png")));
        lblAvatar.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        lblAvatar.setForeground(Color.WHITE);
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUser = new JLabel("Admin User");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblUser.setForeground(Color.WHITE);
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblRole = new JLabel("  Administrator  ");
        lblRole.setOpaque(true);
        lblRole.setBackground(ApplicationColors.SUCCESS);
        lblRole.setForeground(Color.WHITE);
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 12));
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
        btn.setPreferredSize(new Dimension(SIDEBAR_WIDTH - 20, 50));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btn.setForeground(new Color(200, 200, 200));
        btn.setBackground(new Color(44, 62, 80));
        btn.setBorder(new EmptyBorder(0, 20, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 73, 94));
                btn.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(44, 62, 80));
                btn.setForeground(new Color(200, 200, 200));
            }
        });

        btn.addActionListener(e -> cardLayout.show(mainContent, cardName));
        parent.add(btn);
    }

    private JPanel createSidebarFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setPreferredSize(new Dimension(120, 40));
        btnLogout.setBackground(ApplicationColors.ERROR);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogout.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this, "Quitter la session ?", "Logout", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        footer.add(btnLogout, BorderLayout.WEST);
        return footer;
    }

    private JPanel createViewPlaceholder(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(ApplicationColors.BACKGROUND);
        JLabel l = new JLabel(title, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 24));
        l.setForeground(ApplicationColors.TEXT_SECONDARY);
        p.add(l, BorderLayout.CENTER);
        return p;
    }
}
