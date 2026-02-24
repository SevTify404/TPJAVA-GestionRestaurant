/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formulaires;

/**
 *
 * @author loyale
 */
public class Utilisateurs extends javax.swing.JPanel {
    
    public Utilisateurs() {
    initComponents();
    styleTable();
    styliserColonneRole();
    configurerColonneAction(); 
    chargerUtilisateurs();
}
    
   private void styleTable() {
    //SUPPRIMER LA BORDURE PAR DÉFAUT 
    users_table.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    users_table.getViewport().setBackground(utilitaires.ApplicationColors.BACKGROUND);
    users.setBorder(javax.swing.BorderFactory.createEmptyBorder());

    //EN-TÊTE IMPOSANT
    users.getTableHeader().setBackground(utilitaires.ApplicationColors.PRIMARY_DARK);
    users.getTableHeader().setForeground(utilitaires.ApplicationColors.TEXT_LIGHT);
    users.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    users.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 52));
    users.getTableHeader().setReorderingAllowed(false); 
    users.getTableHeader().setBorder(
        javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0,
            utilitaires.ApplicationColors.ACCENT)
    );

    //LIGNES
    users.setRowHeight(52);
    users.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
    users.setForeground(utilitaires.ApplicationColors.TEXT_PRIMARY);
    users.setSelectionBackground(new java.awt.Color(227, 242, 253));
    users.setSelectionForeground(utilitaires.ApplicationColors.TEXT_PRIMARY);
    users.setShowHorizontalLines(true);
    users.setShowVerticalLines(false);
    users.setGridColor(utilitaires.ApplicationColors.BORDER);
    users.setIntercellSpacing(new java.awt.Dimension(0, 0));
    users.setFocusable(false);

    //RENDERER AVEC COINS ARRONDIS SIMULÉS
    users.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 12, 0, 12));

            if (isSelected) {
                setBackground(new java.awt.Color(227, 242, 253));
                setForeground(utilitaires.ApplicationColors.PRIMARY_DARK);
                setFont(getFont().deriveFont(java.awt.Font.BOLD));
            } else if (row % 2 == 0) {
                setBackground(utilitaires.ApplicationColors.PANEL_BG);
                setForeground(utilitaires.ApplicationColors.TEXT_PRIMARY);
                setFont(getFont().deriveFont(java.awt.Font.PLAIN));
            } else {
                setBackground(new java.awt.Color(248, 250, 252)); 
                setForeground(utilitaires.ApplicationColors.TEXT_PRIMARY);
                setFont(getFont().deriveFont(java.awt.Font.PLAIN));
            }
            return this;
        }
    });
}
    
    private void chargerUtilisateurs() {
    
    javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) users.getModel();
    model.setRowCount(0);

    dao.CrudResult<java.util.List<entity.Users>> result =
        dao.UsersDAO.getInstance().recupererTout();

    if (result.estUneErreur()) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Erreur : " + result.getErreur());
        return;
    }

    for (entity.Users u : result.getDonnes()) {
        String role = u.getIsAdmin() ? "Admin" : "Non";
        model.addRow(new Object[]{
            u.getIdUser(),
            u.getLogin(),
            u.getSexe(),
            role,
            "Action"
        });
    }
}
    
    private void styliserColonneRole() {
        
    users.getColumnModel().getColumn(3).setCellRenderer(
        new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                // Badge centré dans un panel
                javax.swing.JPanel container = new javax.swing.JPanel(
                    new java.awt.GridBagLayout());
                container.setOpaque(true);
                container.setBackground(
                    row % 2 == 0 ?
                        utilitaires.ApplicationColors.PANEL_BG :
                        new java.awt.Color(248, 250, 252)
                );

                javax.swing.JLabel badge = new javax.swing.JLabel(
                    value != null ? value.toString() : "");
                badge.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                badge.setOpaque(true);
                badge.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
                badge.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 14, 4, 14));

                if ("Admin".equals(value)) {
                    badge.setBackground(new java.awt.Color(237, 231, 246));
                    badge.setForeground(new java.awt.Color(103, 58, 183));
                } else {
                    badge.setBackground(new java.awt.Color(232, 245, 233));
                    badge.setForeground(new java.awt.Color(46, 125, 50));
                }

                container.add(badge);
                return container;
            }
        }
    );
}
    
    private class BoutonsActionRenderer implements javax.swing.table.TableCellRenderer {
    private final javax.swing.JPanel panel = new javax.swing.JPanel();
    private final javax.swing.JButton btnModifier = new javax.swing.JButton("✏  Modifier");
    private final javax.swing.JButton btnSupprimer = new javax.swing.JButton("🗑  Supprimer");

    public BoutonsActionRenderer() {
        panel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 8));
        panel.setOpaque(true);

        //Bouton Modifier
        btnModifier.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        btnModifier.setForeground(java.awt.Color.WHITE);
        btnModifier.setBackground(utilitaires.ApplicationColors.PRIMARY);
        btnModifier.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 16, 6, 16));
        btnModifier.setFocusPainted(false);
        btnModifier.setBorderPainted(false);
        btnModifier.setOpaque(true);
        btnModifier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //Bouton Supprimer
        btnSupprimer.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        btnSupprimer.setForeground(java.awt.Color.WHITE);
        btnSupprimer.setBackground(utilitaires.ApplicationColors.ERROR);
        btnSupprimer.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 16, 6, 16));
        btnSupprimer.setFocusPainted(false);
        btnSupprimer.setBorderPainted(false);
        btnSupprimer.setOpaque(true);
        btnSupprimer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        panel.add(btnModifier);
        panel.add(btnSupprimer);
    }

    @Override
    public java.awt.Component getTableCellRendererComponent(
            javax.swing.JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        panel.setBackground(
            isSelected ? new java.awt.Color(227, 242, 253) :
            row % 2 == 0 ? utilitaires.ApplicationColors.PANEL_BG :
            new java.awt.Color(248, 250, 252)
        );
        return panel;
    }
}
    
    private class BoutonsActionEditor extends javax.swing.DefaultCellEditor {
    private final javax.swing.JPanel panel = new javax.swing.JPanel();
    private final javax.swing.JButton btnModifier = new javax.swing.JButton("✏ Modifier");
    private final javax.swing.JButton btnSupprimer = new javax.swing.JButton("🗑 Supprimer");
    //private int idUserCourant;

    public BoutonsActionEditor() {
        super(new javax.swing.JCheckBox());

        panel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 8, 6));
        panel.setOpaque(true);

        //Bouton Modifier
        btnModifier.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        btnModifier.setForeground(java.awt.Color.WHITE);
        btnModifier.setBackground(utilitaires.ApplicationColors.PRIMARY);
        btnModifier.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 16, 6, 16));
        btnModifier.setFocusPainted(false);
        btnModifier.setBorderPainted(false);
        btnModifier.setOpaque(true);
        btnModifier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        //Bouton Supprimer
        btnSupprimer.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        btnSupprimer.setForeground(java.awt.Color.WHITE);
        btnSupprimer.setBackground(utilitaires.ApplicationColors.ERROR);
        btnSupprimer.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 16, 6, 16));
        btnSupprimer.setFocusPainted(false);
        btnSupprimer.setBorderPainted(false);
        btnSupprimer.setOpaque(true);
        btnSupprimer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        panel.add(btnModifier);
        panel.add(btnSupprimer);

        // LOGIQUE DU BOUTTON MODIFIER
        btnModifier.addActionListener(e -> {
            fireEditingStopped();

            javax.swing.table.DefaultTableModel model =
                (javax.swing.table.DefaultTableModel) users.getModel();

            int row = users.getSelectedRow();
            if (row == -1) return;

            int id       = (int)    model.getValueAt(row, 0);
            String login = (String) model.getValueAt(row, 1);
            String sexe  = (String) model.getValueAt(row, 2);
            String role  = (String) model.getValueAt(row, 3);

            ModifierUser dialog = new ModifierUser(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(
                    Utilisateurs.this), true);
            dialog.preRemplir(id, login, sexe, role);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

            chargerUtilisateurs();
        });

        //LOGIQUE DU BOUTTON SUPPRIMER 
        btnSupprimer.addActionListener(e -> {
            fireEditingStopped();

            int row = users.getSelectedRow();
            if (row == -1) return;

            javax.swing.table.DefaultTableModel model =
                (javax.swing.table.DefaultTableModel) users.getModel();

            int id = (int) model.getValueAt(row, 0);
            
            int choix = javax.swing.JOptionPane.showConfirmDialog(
                Utilisateurs.this,
                "Voulez-vous vraiment supprimer cet utilisateur ?",
                "Confirmation de suppression",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);

            if (choix != javax.swing.JOptionPane.YES_OPTION) return;

            entity.Users u = new entity.Users();
            u.setIdUser(id);

            dao.CrudResult<Boolean> result =
                dao.UsersDAO.getInstance().suppressionLogique(u);

            if (result.estUneErreur()) {
                javax.swing.JOptionPane.showMessageDialog(
                    Utilisateurs.this,
                    "Erreur : " + result.getErreur());
            } else {
                javax.swing.JOptionPane.showMessageDialog(
                    Utilisateurs.this,
                    "Utilisateur supprimé avec succès !");
                chargerUtilisateurs();
            }
        });
    }

    @Override
    public java.awt.Component getTableCellEditorComponent(
            javax.swing.JTable table, Object value,
            boolean isSelected, int row, int column) {
        panel.setBackground(utilitaires.ApplicationColors.PRIMARY);
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
    
    private void configurerColonneAction() {
    users.getColumnModel().getColumn(4).setCellRenderer(new BoutonsActionRenderer());
    users.getColumnModel().getColumn(4).setCellEditor(new BoutonsActionEditor());
    users.getColumnModel().getColumn(4).setMinWidth(240);

    // Largeurs fixes pour toutes les colonnes
    users.getColumnModel().getColumn(0).setPreferredWidth(60);   
    users.getColumnModel().getColumn(1).setPreferredWidth(200);  
    users.getColumnModel().getColumn(2).setPreferredWidth(80);   
    users.getColumnModel().getColumn(3).setPreferredWidth(100);  
    users.getColumnModel().getColumn(4).setPreferredWidth(240); 
}
    
    /**
     * Creates new form Utilisateurs
     */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        User_page = new javax.swing.JPanel();
        jBAjout = new javax.swing.JButton();
        Tittre = new javax.swing.JLabel();
        sous_titre = new javax.swing.JLabel();
        users_table = new javax.swing.JScrollPane();
        users = new javax.swing.JTable();

        User_page.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jBAjout.setBackground(new java.awt.Color(102, 255, 102));
        jBAjout.setFont(new java.awt.Font("sansserif", 3, 14)); // NOI18N
        jBAjout.setForeground(new java.awt.Color(255, 255, 255));
        jBAjout.setText("+ Ajouter");
        jBAjout.addActionListener(this::jBAjoutActionPerformed);

        Tittre.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        Tittre.setText("Gestion des Utilisateurs");

        sous_titre.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        sous_titre.setText("Liste des Utilisateurs du Système");

        users.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        users.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Login", "Sexe", "Role", "Action"
            }
        ));
        users_table.setViewportView(users);

        javax.swing.GroupLayout User_pageLayout = new javax.swing.GroupLayout(User_page);
        User_page.setLayout(User_pageLayout);
        User_pageLayout.setHorizontalGroup(
            User_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, User_pageLayout.createSequentialGroup()
                .addGroup(User_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(User_pageLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(users_table, javax.swing.GroupLayout.PREFERRED_SIZE, 1091, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(User_pageLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(User_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(User_pageLayout.createSequentialGroup()
                                .addComponent(sous_titre)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(Tittre))
                        .addGap(755, 755, 755)
                        .addComponent(jBAjout)))
                .addGap(263, 263, 263))
        );
        User_pageLayout.setVerticalGroup(
            User_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(User_pageLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addGroup(User_pageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(User_pageLayout.createSequentialGroup()
                        .addComponent(Tittre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sous_titre))
                    .addComponent(jBAjout, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(users_table, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(User_page, javax.swing.GroupLayout.PREFERRED_SIZE, 1265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(135, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(User_page, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jBAjoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAjoutActionPerformed
    UserForm form = new UserForm(null, true);
    form.setLocationRelativeTo(null);
    form.setVisible(true);
        
    }//GEN-LAST:event_jBAjoutActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Tittre;
    private javax.swing.JPanel User_page;
    private javax.swing.JButton jBAjout;
    private javax.swing.JLabel sous_titre;
    private javax.swing.JTable users;
    private javax.swing.JScrollPane users_table;
    // End of variables declaration//GEN-END:variables
}
