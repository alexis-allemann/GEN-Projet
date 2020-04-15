/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreJFrame.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant la fenêtre graphique de l'utilisateur
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChibreJFrame extends JFrame implements UserInterface {
    // Attributs
    private CardJPanel card;
    private ChibreUser chibreUser;
    private static final int WINDOW_SIZE = 600;

    /**
     * Instanciation d'une interface utilisateur
     *
     * @param chibreUser la classe user gérant l'interface
     */
    public ChibreJFrame(ChibreUser chibreUser) {
        // Validation de l'utilisateur reçu
        if (chibreUser == null)
            throw new IllegalArgumentException("Chibre user nul");

        // Relier l'interface avec l'utilisateur
        this.chibreUser = chibreUser;
        chibreUser.setUserInterface(this);

        // Titre de la fenêtre
        setTitle("Chibre");

        // Contenu de la fenêtre
        Container contentPane = getContentPane();
        JLabel waitingUsers = new JLabel();
        waitingUsers.setText("En attente d'autres utilisateurs. Minimum 4 pour débuter la partie.");
        contentPane.add(waitingUsers);

        // Listeners de l'application
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });

        // Affichage de la fenêtre
        setVisible(true);
        setSize(WINDOW_SIZE, WINDOW_SIZE);
    }

    /**
     * Affichage de la carte reçue en paramètre
     *
     * @param cardJPanel la carte à afficher
     */
    @Override
    public void display(CardJPanel cardJPanel) {
        this.card = cardJPanel;
        Container contentPane = getContentPane();
        contentPane.add(cardJPanel, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
        pack();
    }

    /**
     * Action à effectuer lorsque la fenêtre est fermée
     */
    @Override
    public void quit() {
        System.exit(0);
    }
}