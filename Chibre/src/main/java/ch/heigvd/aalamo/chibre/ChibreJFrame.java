/* ---------------------------
Laboratoire : 04
Fichier :     BalloonJFrame.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        26.03.2020 - 01.04.2020
But : Classe représentant la frame d'un ballon
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChibreJFrame extends JFrame implements UserInterface {
    // Attributs
    private JButton little, large, lance;
    private BalloonJPanel balloon;
    private ChibreUser chibreUser;

    /**
     * Instanciation d'une interface utilisateur
     * @param chibreUser la classe user gérant l'interface
     */
    public ChibreJFrame(ChibreUser chibreUser) {
        if (chibreUser == null)
            throw new IllegalArgumentException("Ballon user nul");

        this.chibreUser = chibreUser;
        chibreUser.setUserInterface(this);

        setTitle("Balloon");

        Container contentPane = getContentPane();
        JPanel boutons = new JPanel();
        boutons.setLayout(new GridLayout(1, 3));
        contentPane.add(boutons, BorderLayout.NORTH);

        little = new JButton("Little");
        boutons.add(little);
        little.addActionListener(e -> {
            if (balloon != null)
                balloon.changeSize(-10);
        });

        lance = new JButton("Lance");
        boutons.add(lance);
        lance.addActionListener(e -> {
            if (balloon != null) {
                contentPane.remove(balloon);
                chibreUser.send(balloon);
                this.balloon = null;
                contentPane.revalidate();
                contentPane.repaint();
            }
        });

        large = new JButton("Large");
        boutons.add(large);
        large.addActionListener(e -> {
            if (balloon != null)
                balloon.changeSize(+10);
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });

        setVisible(true);
        setSize(223,265);
    }

    /**
     * Affichage du ballon donné en paramètre
     * @param balloonJPanel le ballon à afficher
     */
    @Override
    public void display(BalloonJPanel balloonJPanel) {
        this.balloon = balloonJPanel;
        Container contentPane = getContentPane();
        contentPane.add(balloonJPanel, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
        pack();
    }

    /**
     * Action à effectuer lorsque la fenêtre est fermée
     */
    @Override
    public void quit() {
        if (balloon != null)
            chibreUser.send(balloon);
        System.exit(0);
    }
}