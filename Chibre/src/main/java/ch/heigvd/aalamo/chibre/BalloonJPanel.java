/* ---------------------------
Laboratoire : 04
Fichier :     BalloonJPanel.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        26.03.2020 - 01.04.2020
But : Classe représentant le panel d'un ballon
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class BalloonJPanel extends JPanel {
    // Attributs
    private int rayon = 50;

    /**
     * Instanciation d'un ballon
     */
    public BalloonJPanel() {
        setPreferredSize(new Dimension(200,200));
    }

    /**
     * Changer la taille d'un ballon
     * @param change variation de la taille
     */
    public void changeSize(int change) {
        rayon = rayon + change;
        repaint();
    }

    /**
     * Afficher le ballon
     * @param g interface graphique
     */
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.drawOval(getWidth()/2-rayon, getHeight()/2-rayon, 2*rayon, 2*rayon);
    }
}