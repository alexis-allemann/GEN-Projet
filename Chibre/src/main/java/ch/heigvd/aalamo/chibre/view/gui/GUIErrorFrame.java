package ch.heigvd.aalamo.chibre.view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class GUIErrorFrame {
    // Composants
    private JPanel pnlMainPanel;
    private JLabel lblErrorMessage;

    // Attributs
    JFrame gui = new JFrame("Erreur");
    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 100;

    /**
     * Instancier la fenêtre d'erreur
     *
     * @param errorMessage message d'erreur à afficher
     */
    public GUIErrorFrame(String errorMessage) {
        // Propriétés globales de la GUI
        gui.setContentPane(pnlMainPanel);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.pack();
        gui.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        gui.setLocation(dim.width / 2 - WINDOW_WIDTH / 2, dim.height / 2 - WINDOW_HEIGHT / 2);
        gui.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Ajout du message d'erreur
        lblErrorMessage.setText(errorMessage);
    }
}
