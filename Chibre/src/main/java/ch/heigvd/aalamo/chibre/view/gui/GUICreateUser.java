package ch.heigvd.aalamo.chibre.view.gui;

import ch.heigvd.aalamo.chibre.ChibreController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUICreateUser {
    // Composants
    private JTextField tbxUsername;
    private JPasswordField pwdPassword1;
    private JPasswordField pwdPassword2;
    private JButton btnCreate;
    private JButton btnCancel;
    private JPanel pnlNewPlayer;
    private JLabel lblErrorMessage;

    // Attributs
    JFrame gui = new JFrame("Identification du joueur");
    private final ChibreController controller;
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 200;

    /**
     * Instanciation de la vue d'authentification
     */
    public GUICreateUser(ChibreController controller) {
        this.controller = controller;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        initializeGui();

        // Listeners
        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pwd1 = pwdPassword1.getText();
                String pwd2 = pwdPassword2.getText();
                String username = tbxUsername.getText();

                if (pwd1.isEmpty() || pwd2.isEmpty() || username.isEmpty())
                    displayErrorMessage("Tous les champs doivent être remplis");
                else if (!pwd1.equals(pwd2))
                    displayErrorMessage("Les mots de passe ne sont pas identiques");
                else
                    controller.sendCreateUser(username, pwd1);

            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.displayAuthentication();
                close();
            }
        });
    }

    /**
     * Instanciation de la fenêtre avant affichage
     */
    private void initializeGui() {
        // Propriétés globales de la GUI
        gui.setContentPane(pnlNewPlayer);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.pack();
        gui.setVisible(true);
        gui.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        gui.setLocation(dim.width / 2 - WINDOW_WIDTH / 2, dim.height / 2 - WINDOW_HEIGHT / 2);
        JRootPane rootPane = SwingUtilities.getRootPane(btnCreate);
        rootPane.setDefaultButton(btnCreate);

        // Masquer le message d'erreur
        lblErrorMessage.setVisible(false);
    }

    /**
     * Fermeture de la fenêtre
     */
    public void close() {
        gui.dispose();
    }

    /**
     * Affichage du message d'erreur d'authentification
     */
    public void displayErrorMessage(String message) {
        lblErrorMessage.setVisible(true);
        lblErrorMessage.setText(message);
        gui.setSize(WINDOW_WIDTH, WINDOW_HEIGHT + 50);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        gui.setLocation(dim.width / 2 - WINDOW_WIDTH / 2, dim.height / 2 - WINDOW_HEIGHT / 2);
        tbxUsername.grabFocus();
    }
}
