/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilitaires;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author sevtify
 */
public class Security {
    
    public static String crypterMotdePasse(String leMotDePasseClair){
        String mdpHash = null;
        
        try {
            // Selection de l'algorithme de cryptage
            MessageDigest algoDeCrypto = MessageDigest.getInstance("SHA-256");
            
            // Transformation du mdp clair en tableau de byte
            byte[] mdpBytes = leMotDePasseClair.getBytes(StandardCharsets.UTF_8);
            
            // Passage du tableau de byte à l'algo pour le transdormer en hash
            
            byte[] hash = algoDeCrypto.digest(mdpBytes);
            
            // Conversion de byte en hexadécimal
            StringBuilder s = new StringBuilder();
            for (byte b : hash) {
                s.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            
            // Conversion de base 16 vers vrai string et retour
            
            mdpHash = s.toString();
            
            
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Algo de crypto non trouvé");
        }
        
        return mdpHash;
    
        
    }
}
