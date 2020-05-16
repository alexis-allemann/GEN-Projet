/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     GUIAuthentication.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Fenêtre d'authentification de l'utilisateur
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.view.gui;

import ch.heigvd.aalamo.chibre.ChibreController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GUIAuthentication {
    // Composants
    private JButton btnConnect;
    private JButton btnCancel;
    private JTextField tbxUsername;
    private JPasswordField tbxPassword;
    private JLabel lblNewPlayer;
    private JPanel pnlAuthentication;
    private JLabel lblErrorMessage;

    // Attributs
    JFrame gui = new JFrame("Identification du joueur");
    private final ChibreController controller;
    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 200;

    /**
     * Instanciation de la vue d'authentification
     */
    public GUIAuthentication(ChibreController controller) {
        this.controller = controller;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        initializeGui();

        // Listeners
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tbxPassword.getText().isEmpty() || tbxUsername.getText().isEmpty())
                    displayErrorMessage();
                else
                    controller.sendAuthentication(tbxUsername.getText(), toHexString(getSHA(tbxPassword.getText())));
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.quit();
            }
        });
    }

    // Méthodes

    /**
     * Instanciation de la fenêtre avant affichage
     */
    private void initializeGui() {
        // Propriétés globales de la GUI
        gui.setContentPane(pnlAuthentication);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.pack();
        gui.setVisible(true);
        gui.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        gui.setLocation(dim.width / 2 - WINDOW_WIDTH / 2, dim.height / 2 - WINDOW_HEIGHT / 2);

        // Masquer le message d'erreur
        lblErrorMessage.setVisible(false);
    }

    /**
     * Affichage du message d'erreur d'authentification
     */
    public void displayErrorMessage() {
        lblErrorMessage.setVisible(true);
        tbxUsername.grabFocus();
        tbxPassword.setText("");
    }

    /**
     * Fermeture de la fenêtre
     */
    public void close() {
        gui.dispose();
    }

    /**
     * Hachage d'un texte donné
     *
     * @param input le texte à hacher
     * @return le tableau de bytes du hachage
     */
    public static byte[] getSHA(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Obtenir le hash en string
     *
     * @param hash tableau de bytes à convertir en string
     * @return la string du hachage
     */
    public static String toHexString(byte[] hash) {
        // Conversion du tableau de byte en numérique
        BigInteger number = new BigInteger(1, hash);

        // Conversion du message en hexadécimal
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Ajout des zéros significatifs
        while (hexString.length() < 32)
            hexString.insert(0, '0');

        return hexString.toString();
    }
}
