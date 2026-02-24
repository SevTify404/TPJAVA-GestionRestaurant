/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilitaires;

/**
 *
 * @author sevtify
 */
public class StatsDto {
    public static class ProduitCA {

        public ProduitCA(int idProduit, String nomProduit, double chiffreAffaire) {
            this.idProduit = idProduit;
            this.nomProduit = nomProduit;
            this.chiffreAffaire = chiffreAffaire;
        }

        public ProduitCA() {
        }
        
        
        

    private int idProduit;
    private String nomProduit;
    private double chiffreAffaire;

        public int getIdProduit() {
            return idProduit;
        }

        public String getNomProduit() {
            return nomProduit;
        }

        public void setChiffreAffaire(double chiffreAffaire) {
            this.chiffreAffaire = chiffreAffaire;
        }

        public void setIdProduit(int idProduit) {
            this.idProduit = idProduit;
        }

        public void setNomProduit(String nomProduit) {
            this.nomProduit = nomProduit;
        }

        public double getChiffreAffaire() {
            return chiffreAffaire;
        }
    }
    public static enum PeriodeStatistique {
        SEMAINE_EN_COURS,
        MOIS_EN_COURS,
        ANNEE_EN_COURS
        
    }
    public static class ProduitQuantite {

    private int idProduit;
    private String nomProduit;
    private int totalVendu;

        public String getNomProduit() {
            return nomProduit;
        }

        public int getTotalVendu() {
            return totalVendu;
        }

        public void setIdProduit(int idProduit) {
            this.idProduit = idProduit;
        }

        public void setNomProduit(String nomProduit) {
            this.nomProduit = nomProduit;
        }

        public void setTotalVendu(int totalVendu) {
            this.totalVendu = totalVendu;
        }

        public ProduitQuantite() {
        }

        public ProduitQuantite(int idProduit, String nomProduit, int totalVendu) {
            this.idProduit = idProduit;
            this.nomProduit = nomProduit;
            this.totalVendu = totalVendu;
        }


        public int getIdProduit() {
            return idProduit;
        }
    }
    
}
