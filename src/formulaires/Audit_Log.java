/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package formulaires;

/**
 *
 * @author loyale
 */
public class Audit_Log extends javax.swing.JPanel {
    
    /**
     * Creates new form Audit_Log
     */
    public Audit_Log() {
    initComponents();
    styleTable();
    chargerAudits();
}
    private void styleTable() {
    //EN-TÊTE
    jTable1.getTableHeader().setBackground(utilitaires.ApplicationColors.PRIMARY);
    jTable1.getTableHeader().setForeground(utilitaires.ApplicationColors.TEXT_LIGHT);
    jTable1.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    jTable1.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 40));

    //LIGNES
    jTable1.setRowHeight(45);
    jTable1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
    jTable1.setForeground(utilitaires.ApplicationColors.TEXT_PRIMARY);
    jTable1.setSelectionBackground(utilitaires.ApplicationColors.PRIMARY);
    jTable1.setSelectionForeground(utilitaires.ApplicationColors.TEXT_LIGHT);
    jTable1.setShowHorizontalLines(true);
    jTable1.setGridColor(utilitaires.ApplicationColors.BORDER);
    jTable1.setIntercellSpacing(new java.awt.Dimension(0, 1));
    jTable1.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

            if (isSelected) {
                setBackground(utilitaires.ApplicationColors.PRIMARY);
                setForeground(utilitaires.ApplicationColors.TEXT_LIGHT);
            } else if (row % 2 == 0) {
                setBackground(utilitaires.ApplicationColors.PANEL_BG);
                setForeground(utilitaires.ApplicationColors.TEXT_PRIMARY);
            } else {
                setBackground(utilitaires.ApplicationColors.BACKGROUND);
                setForeground(utilitaires.ApplicationColors.TEXT_PRIMARY);
            }
            return this;
        }
    });
    styliserColonneAction(); 
    styliserColonneStatut();
   
}
    
    private void styliserColonneAction() {
    jTable1.getColumnModel().getColumn(3).setCellRenderer(
        new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                javax.swing.JLabel label = new javax.swing.JLabel(
                    value != null ? value.toString() : "");
                label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                label.setOpaque(true);
                label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

                String action = value != null ? value.toString() : "";

                switch (action) {
                    case "SUPPRESSION" -> {
                        label.setBackground(new java.awt.Color(255, 235, 238));
                        label.setForeground(utilitaires.ApplicationColors.ERROR);
                    }
                    case "AJOUT" -> {
                        label.setBackground(new java.awt.Color(232, 245, 233)); 
                        label.setForeground(utilitaires.ApplicationColors.SUCCESS);
                    }
                    case "MODIFICATION" -> {
                        label.setBackground(new java.awt.Color(255, 243, 224)); 
                        label.setForeground(utilitaires.ApplicationColors.WARNING);
                    }
                    case "CONNEXION" -> {
                        label.setBackground(new java.awt.Color(227, 242, 253)); 
                        label.setForeground(utilitaires.ApplicationColors.PRIMARY);
                    }
                    case "VENTE" -> {
                        label.setBackground(new java.awt.Color(243, 229, 245)); 
                        label.setForeground(utilitaires.ApplicationColors.CHART_1);
                    }
                    default -> {
                        label.setBackground(utilitaires.ApplicationColors.BACKGROUND);
                        label.setForeground(utilitaires.ApplicationColors.TEXT_SECONDARY);
                    }
                }
                return label;
            }
        }
    );
}
    
    private void styliserColonneStatut() {
    jTable1.getColumnModel().getColumn(4).setCellRenderer(
        new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                javax.swing.JLabel label = new javax.swing.JLabel(
                    value != null ? value.toString() : "");
                label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                label.setOpaque(true);
                label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
                return label;
            }
        }
    );
}
    
    private void chargerAudits() {
    javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
        new Object[][]{},
        new String[]{"ID", "Date & Heure", "Utilisateur", "Action", "Message"} // ← Statut retiré
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    jTable1.setModel(model);

    styliserColonneAction();

    dao.CrudResult<java.util.List<entity.Audit>> result =
        dao.AuditDAO.getInstance().recupererTout();

    if (result.estUneErreur()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Erreur : " + result.getErreur());
        return;
    }

    for (entity.Audit a : result.getDonnes()) {
        String date = "";
        if (a.getTempsAction() != null) {
            java.time.ZonedDateTime zdt = a.getTempsAction()
                .atZone(java.time.ZoneId.systemDefault());
            date = java.time.format.DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss").format(zdt);
        }

        String utilisateur = "";
        if (a.getUser() != null && a.getUser().getLogin() != null) {
            String role = a.getUser().getIsAdmin() ? "Admin" : "Caissier";
            utilisateur = a.getUser().getLogin() + " (" + role + ")";
        }

        model.addRow(new Object[]{
            a.getIdAudit(),
            date,
            utilisateur,
            a.getAction().name(),
            a.getMessage()
        });
    }

    jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
    jTable1.getColumnModel().getColumn(1).setPreferredWidth(150);
    jTable1.getColumnModel().getColumn(2).setPreferredWidth(180);
    jTable1.getColumnModel().getColumn(3).setPreferredWidth(120);
    jTable1.getColumnModel().getColumn(4).setPreferredWidth(300);
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Zone = new javax.swing.JPanel();
        Titre = new javax.swing.JLabel();
        Sous_titre = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        Zone.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        Titre.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        Titre.setText("AUDIT & LOGS");

        Sous_titre.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        Sous_titre.setText("Historiques complet des actions système\n");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "idAudit", "Utilisateur", "Action", "Message", "Temps Action"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout ZoneLayout = new javax.swing.GroupLayout(Zone);
        Zone.setLayout(ZoneLayout);
        ZoneLayout.setHorizontalGroup(
            ZoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ZoneLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(ZoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ZoneLayout.createSequentialGroup()
                        .addComponent(Sous_titre)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(ZoneLayout.createSequentialGroup()
                        .addComponent(Titre)
                        .addGap(34, 1032, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ZoneLayout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1033, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92))
        );
        ZoneLayout.setVerticalGroup(
            ZoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ZoneLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(Titre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Sous_titre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(Zone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(Zone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(102, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Sous_titre;
    private javax.swing.JLabel Titre;
    private javax.swing.JPanel Zone;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
