/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilitaires;

import java.awt.Color;

/**
 *
 * @author sevtify
 */
public class ApplicationColors {
    
    // J'ai défini les couleurs qu'on va réutilisé pour que les interfaces soient pareils
    // --- COULEURS PRIMAIRES & MARQUE ---
    public static final Color PRIMARY        = new Color(33, 150, 243);      
    public static final Color PRIMARY_DARK   = new Color(25, 118, 210);
    public static final Color ACCENT         = new Color(0, 188, 212); // Pour les éléments secondaires

    // --- ARRIÈRE-PLANS & ZONES ---
    public static final Color BACKGROUND     = new Color(245, 247, 250); // Fond général
    public static final Color PANEL_BG       = Color.WHITE;             // Fond des cartes/panneaux
    public static final Color BORDER         = new Color(224, 224, 224); // Pour séparations/lignes

    // --- SIDEBAR (Navigation) ---
    public static final Color SIDEBAR_BG        = Color.decode("#0369a1");
    public static final Color SIDEBAR_HOVER     = new Color(2, 105, 161, 50); // Alpha pour transparence
    public static final Color SIDEBAR_SELECTION = new Color(52, 152, 219);

    public static final Color SIDEBAR_TEXT      = new Color(236, 240, 241);

    // --- TYPOGRAPHIE ---
    public static final Color TEXT_PRIMARY   = new Color(33, 33, 33);
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117);
    public static final Color TEXT_LIGHT     = new Color(255, 255, 255);

    // --- ÉTATS & ALERTE (Feedback Utilisateur) ---
    public static final Color SUCCESS        = new Color(76, 175, 80);    // Vert succès
    public static final Color INFO           = new Color(33, 150, 243);   // Bleu info
    public static final Color WARNING        = new Color(255, 152, 0);    // Orange alerte
    public static final Color ERROR          = new Color(211, 47, 47);    // Rouge danger

    // --- COULEURS COMPLÉMENTAIRES (Utiles pour les Graphiques/Stats) ---
    public static final Color CHART_1 = new Color(103, 58, 183);
    public static final Color CHART_2 = new Color(233, 30, 99);
    public static final Color CHART_3 = new Color(255, 193, 7);
}
