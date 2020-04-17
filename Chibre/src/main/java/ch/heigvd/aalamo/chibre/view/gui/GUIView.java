/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     GUIView.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe de la fenêtre utilisateur du jeu
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.view.gui;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;
import ch.heigvd.aalamo.chibre.ChibreController;
import ch.heigvd.aalamo.chibre.assets.GuiAssets;
import ch.heigvd.aalamo.chibre.view.BaseView;
import ch.heigvd.aalamo.chibre.view.DrawableRessource;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIView extends BaseView<ImageIcon> {
    // Attributs
    private final JLabel messageLabel = new JLabel("");
    private final JLabel headerLabel = new JLabel("Bienvenue sur le jeu du Chibre !");
    private List<CardJPanel> cards = new ArrayList<>(9);
    private final static ImageIcon UNKNOWN_ICON;
    private static final int WINDOW_SIZE = 600;
    private CardJPanel lastPressed = null;
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));

    // Initialisation des attributs statiques
    static {
        BufferedImage img = new BufferedImage(211, 322, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        UNKNOWN_ICON = new ImageIcon(img);
    }

    /**
     * Instanciation des cartes
     */
    private static class CardResource implements DrawableRessource<ImageIcon> {

        private ImageIcon icon;

        CardResource(BufferedImage bufferedImage) {
            icon = new ImageIcon(bufferedImage);
        }

        @Override
        public ImageIcon getResource() {
            return icon;
        }
    }

    /**
     * Chargement des ressources images
     *
     * @param image image à charger
     * @return la ressource
     */
    public static DrawableRessource<ImageIcon> createResource(BufferedImage image) {
        return new CardResource(image);
    }

    /**
     * Instanciation de la vue
     *
     * @param controller controlleur de la vue
     */
    public GUIView(ChibreController controller) {
        super(controller);
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        GuiAssets.loadAssets(this);
        initializeGui();
    }

    /**
     * Démarrage de la vue
     */
    @Override
    public void startView() {
        Runnable r = () -> {
            JFrame f = new JFrame("Chibre");
            f.add(gui);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setLocationByPlatform(true);
            f.setSize(WINDOW_SIZE, WINDOW_SIZE);
            f.setVisible(true);
        };
        SwingUtilities.invokeLater(r);
    }

    /**
     * Action à effectuer lorsque la fenêtre est fermée
     */
    @Override
    public void quit() {
        System.exit(0);
    }

    /**
     * Afficher une carte
     *
     * @param type     type de la carte (as, roi, dame, valet, dix, neuf, huit, sept, six)
     * @param color    couleur de la carte (carreau, coeur, pique, trèfle)
     * @param position la posistion dans le deck de l'utilisateur (9 cartes => 0:8)
     */
    @Override
    public void addCard(CardType type, CardColor color, int position) {
        cards.get(position).setIcon(loadResourceFor(type, color, UNKNOWN_ICON));
    }

    /**
     * Poser une question à l'utilisateur
     *
     * @param title         titre de la fenêtre
     * @param question      la question
     * @param possibilities les options (liste déroulante)
     * @param <T>           type des options
     * @return l'option choisie dans le type donné
     */
    @Override
    public <T extends UserChoice> T askUser(String title, String question, T... possibilities) {
        T result = possibilities.length > 0 ? possibilities[0] : null;
        if (possibilities.length > 1) {
            result = (T) JOptionPane.showInputDialog(null,
                    question, title, JOptionPane.QUESTION_MESSAGE, null, possibilities, result);
        }
        return result;
    }

    /**
     * Action lors du clic sur une carte
     *
     * @param cardJPanel la carte
     */
    private void cardAction(CardJPanel cardJPanel) {
        // Rien de sélectionner
        if (lastPressed == null) {
            lastPressed = cardJPanel;
            cardJPanel.select();
        }
        // Quelque chose de sélectionner avant
        else {
            lastPressed.deselect();
            lastPressed = null;
        }
    }

    /**
     * Instanciation de la fenêtre avant affichage
     */
    private void initializeGui() {
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        tools.addSeparator();
        tools.add(headerLabel);
        tools.addSeparator();
        tools.addSeparator();
        tools.add(messageLabel);
        messageLabel.setForeground(Color.RED);

        JPanel cardBoard = new JPanel(new GridLayout(0, 9)) {
        };

        cardBoard.setBorder(new CompoundBorder(
                new EmptyBorder(0, 0, 0, 0),
                new LineBorder(Color.WHITE)
        ));

        for (int i = 0; i < 9; ++i) {
            CardJPanel cardJPanel = new CardJPanel(i);
            cardJPanel.addActionListener(e -> cardAction(cardJPanel));
            cards.add(cardJPanel);
            cardBoard.add(cardJPanel);
        }

        gui.add(cardBoard);
    }
}


