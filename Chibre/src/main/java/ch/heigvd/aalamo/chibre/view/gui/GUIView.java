package ch.heigvd.aalamo.chibre.view.gui;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;
import ch.heigvd.aalamo.chibre.ChibreController;
import ch.heigvd.aalamo.chibre.assets.GuiAssets;
import ch.heigvd.aalamo.chibre.view.BaseView;
import ch.heigvd.aalamo.chibre.view.DrawableRessource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIView extends BaseView<ImageIcon> {
    // Composants
    private JPanel pnlMain;
    private JPanel pnlTable;
    private JPanel pnlTableBloc;
    private JPanel pnlInfo;
    private JPanel pnlCards;
    private JLabel lblTableTopName;
    private JLabel lblTableBottomName;
    private JLabel lblTableLeftName;
    private JLabel lblTableRightName;
    private JPanel pnlInfoGlobal;
    private JPanel pnlInfoTeam1;
    private JPanel pnlInfoTeam2;
    private JPanel pnlInfoBonus;
    private JButton btnBonus;
    private JPanel pnlInfoUser;
    private JLabel lblCard1;
    private JLabel lblCard2;
    private JLabel lblCard3;
    private JLabel lblCard4;
    private JLabel lblCard5;
    private JLabel lblCard6;
    private JLabel lblCard7;
    private JLabel lblCard8;
    private JLabel lblCard9;
    private JLabel cardPlayed;

    // Attributs
    JFrame gui = new JFrame("Chibre");
    private static final int WINDOW_WIDTH = 1260;
    private static final int WINDOW_HEIGHT = 850;
    private List<JLabel> cards = new ArrayList<>(9);
    private final static ImageIcon UNKNOWN_ICON;
    private JLabel dragSource;
    private ChibreController controller;

    // Initialisation des attributs statiques
    static {
        BufferedImage img = new BufferedImage(211, 322, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        UNKNOWN_ICON = new ImageIcon(img);
    }

    /**
     * Instanciation de la vue
     *
     * @param controller controlleur de la vue
     */
    public GUIView(ChibreController controller) {
        super(controller);
        this.controller = controller;
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        GuiAssets.loadAssets(this);
        initializeGui();
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
        return new GUIView.CardResource(image);
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
        JLabel card = cards.get(position);
        card.setIcon(loadResourceFor(type, color, UNKNOWN_ICON));
        card.setTransferHandler(new TransferHandler("icon"));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JComponent lab = (JComponent) e.getSource();
                TransferHandler handle = lab.getTransferHandler();
                handle.exportAsDrag(lab, e, TransferHandler.COPY);
                dragSource = card;
            }
        });
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
     * Instanciation de la fenêtre avant affichage
     */
    private void initializeGui() {
        gui.setContentPane(pnlMain);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.pack();
        gui.setVisible(true);
        gui.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        cards.add(lblCard1);
        cards.add(lblCard2);
        cards.add(lblCard3);
        cards.add(lblCard4);
        cards.add(lblCard5);
        cards.add(lblCard6);
        cards.add(lblCard7);
        cards.add(lblCard8);
        cards.add(lblCard9);

        for (JLabel card : cards)
            card.setTransferHandler(new TransferHandler("icon"));

        BufferedImage dropImage;
        try {
            dropImage = ImageIO.read(GuiAssets.class.getResource("images/DropImage.png"));
            cardPlayed.setIcon(new ImageIcon(dropImage));
            cardPlayed.setTransferHandler(new TransferHandler("icon"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        cardPlayed.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName() == "icon" && dragSource != null)
                    dragSource.setIcon(null);
                controller.sendCard(cards.indexOf(dragSource));
            }
        });
    }
}
