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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private static final int WINDOW_ERROR_HEIGHT = 250;
    GUICreateUser guiCreateUser;

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
                    controller.sendAuthentication(tbxUsername.getText(), tbxPassword.getText());
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.quit();
            }
        });
        lblNewPlayer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                guiCreateUser = new GUICreateUser(controller);
                gui.setVisible(false);
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
        JRootPane rootPane = SwingUtilities.getRootPane(btnConnect);
        rootPane.setDefaultButton(btnConnect);

        // Masquer le message d'erreur
        lblErrorMessage.setVisible(false);
    }

    /**
     * Affichage du message d'erreur d'authentification
     */
    public void displayErrorMessage() {
        lblErrorMessage.setVisible(true);
        gui.setSize(WINDOW_WIDTH, WINDOW_ERROR_HEIGHT + 40);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        gui.setLocation(dim.width / 2 - WINDOW_WIDTH / 2, dim.height / 2 - WINDOW_HEIGHT / 2);
        tbxUsername.grabFocus();
        tbxPassword.setText("");
    }

    /**
     * Fermeture de la fenêtre
     */
    public void close() {
        if(guiCreateUser != null)
            guiCreateUser.close();
        gui.dispose();
    }

    /**
     * Afficher l'erreur lors de l'erreur de création d'un utilisateur
     */
    public void displayCreateUserError() {
        guiCreateUser.displayErrorMessage("Le nom d'utilisateur est déjà utilisé");
    }
}
