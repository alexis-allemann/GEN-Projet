/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     CardJPanel.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une carte
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.frontend;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class CardJPanel extends JPanel {
    // Attributs
    private String fileName;

    /**
     * Instanciation d'une carte
     */
    public CardJPanel(String fileName) {
        setPreferredSize(new Dimension(200, 200));
        this.fileName = fileName;
    }


    /**
     * Afficher la carte
     *
     * @param g interface graphique
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        g.drawImage(image, 0, 0, this);
    }
}