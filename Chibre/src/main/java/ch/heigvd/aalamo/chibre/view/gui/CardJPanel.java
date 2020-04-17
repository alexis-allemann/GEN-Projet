/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     CardJPanel.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une carte
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.view.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class CardJPanel extends JButton {
    // Attributs
    final int position;

    /**
     * Instanciation d'une carte
     *
     * @param position position dans le deck de l'utilisateur
     */
    CardJPanel(int position) {
        assert (position < 9 && position >= 0);
        this.position = position;
        this.setBackground(Color.WHITE);
        this.setOpaque(true);
    }

    /**
     * Action lors de la sélection d'une carte
     */
    void select() {
        this.setBorder(new LineBorder(Color.GREEN, 5));
    }

    /**
     * Action lorsque la carte n'est plus sélectionnée
     */
    void deselect() {
        this.setBorder(null);
    }
}