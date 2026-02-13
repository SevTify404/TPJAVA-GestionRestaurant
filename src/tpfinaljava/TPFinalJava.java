/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tpfinaljava;

import entity.Produit;


import dao.AuditDAO;
import dao.CrudResult;
import entity.Audit;

import java.util.List;

/**
 *
 * @author sevtify
 */
public class TPFinalJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CrudResult<List<Audit>> ee = AuditDAO.getInstance().recupererTout();
        System.out.println(ee);
        if (ee.estUnSucces()) {
            System.out.println(ee.getDonnes());
        }
    }
    
}
