package ch.heigvd.aalamo.chibre.view.gui;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;
import ch.heigvd.aalamo.chibre.ChibreController;
import ch.heigvd.aalamo.chibre.assets.GuiAssets;
import ch.heigvd.aalamo.chibre.engine.Card;
import ch.heigvd.aalamo.chibre.engine.Player;
import ch.heigvd.aalamo.chibre.network.objects.CardDTO;
import ch.heigvd.aalamo.chibre.network.objects.PlayerDTO;
import ch.heigvd.aalamo.chibre.network.objects.TeamDTO;
import ch.heigvd.aalamo.chibre.view.BaseView;
import ch.heigvd.aalamo.chibre.view.DrawableRessource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
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
    private JLabel lblCardPlayedPlayerBottom;
    private JLabel lblCardPlayedPlayerTop;
    private JLabel lblCardPlayedPlayerRight;
    private JLabel lblCardPlayedPlayerLeft;
    private JLabel lblTeam1Player1;
    private JLabel lblTeam1Player2;
    private JLabel lblTeam2Player1;
    private JLabel lblTeam2Player2;
    private JLabel lblTrumpPlayer;
    private JLabel lblTrumpColor;
    private JLabel lblCurrentPlayer;
    private JLabel lblTeam1Points;
    private JLabel lblTeam2Points;
    private JLabel lblWinningTeam;

    // Attributs
    JFrame gui = new JFrame("Chibre");
    GUIAuthentication guiAuthentication;
    private static final int WINDOW_WIDTH = 1260;
    private static final int WINDOW_HEIGHT = 850;
    private List<JLabel> cards = new ArrayList<>(9);
    private final static ImageIcon UNKNOWN_ICON;
    private JLabel dragSource;
    private ChibreController controller;
    private ImageIcon dropIcon;

    // Initialisation des attributs statiques
    static {
        BufferedImage img = new BufferedImage(211, 322, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        UNKNOWN_ICON = new ImageIcon(img);
    }

    // Instanciation de la GUI

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
     * Instanciation de la fenêtre avant affichage
     */
    private void initializeGui() {
        // Propriétés globales de la GUI
        gui.setContentPane(pnlMain);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.pack();
        gui.setVisible(true);
        gui.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        gui.setLocation(dim.width / 2 - WINDOW_WIDTH / 2, dim.height / 2 - WINDOW_HEIGHT / 2);

        // Création des emplacements pour les cartes
        cards.add(lblCard1);
        cards.add(lblCard2);
        cards.add(lblCard3);
        cards.add(lblCard4);
        cards.add(lblCard5);
        cards.add(lblCard6);
        cards.add(lblCard7);
        cards.add(lblCard8);
        cards.add(lblCard9);

        // Autoriser le drag and drop entre les cartes
        for (JLabel card : cards)
            card.setTransferHandler(new TransferHandler("icon"));

        // Création de la zone pour déposer les cartes jouées
        BufferedImage dropImage;
        try {
            dropImage = ImageIO.read(GuiAssets.class.getResource("images/DropImage.png"));
            dropIcon = new ImageIcon(dropImage);
            lblCardPlayedPlayerTop.setIcon(dropIcon);
            lblCardPlayedPlayerLeft.setIcon(dropIcon);
            lblCardPlayedPlayerRight.setIcon(dropIcon);
            lblCardPlayedPlayerBottom.setIcon(dropIcon);
            lblCardPlayedPlayerBottom.setTransferHandler(new TransferHandler("icon"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ajout du listeneur pour envoyer une carte jouée
        lblCardPlayedPlayerBottom.addPropertyChangeListener(e -> {
            // Identification de la carte jouée si la propriété icon a changée
            if (e.getPropertyName().equals("icon") && dragSource != null)
                dragSource.setIcon(null);

            if (e.getNewValue() != dropIcon)
                controller.sendCard(cards.indexOf(dragSource));
        });
    }

    // Implémentation des méthodes de la vue

    /**
     * Afficher la fenêtre d'authentification
     */
    @Override
    public void displayAuthentication() {
        guiAuthentication = new GUIAuthentication(controller);
    }

    /**
     * Afficher le message d'erreur d'authentification
     */
    @Override
    public void authenticationFailed() {
        guiAuthentication.displayErrorMessage();
    }

    /**
     * Action lors de l'erreur de création d'un utilisateur sur le serveur
     */
    @Override
    public void createUserFailed() {
        guiAuthentication.displayCreateUserError();
    }

    /**
     * Action lorsque l'authentification est correcte
     */
    @Override
    public void authenticationSucceed() {
        guiAuthentication.close();
        guiAuthentication = null;
    }

    /**
     * Afficher les équipes et les joueurs de la partie
     */
    @Override
    public void displayTeams(TeamDTO team1, TeamDTO team2) {
        lblTeam1Player1.setText(team1.getPlayers().get(0).getUsername());
        lblTeam1Player2.setText(team1.getPlayers().get(1).getUsername());
        lblTeam2Player1.setText(team2.getPlayers().get(0).getUsername());
        lblTeam2Player2.setText(team2.getPlayers().get(1).getUsername());
    }

    /**
     * Afficher les joueurs autour du tapis de jeu
     *
     * @param players            les joueurs
     * @param currentPlayerIndex l'index du joueur qui joue sur la GUI
     */
    @Override
    public void displayPlayers(List<PlayerDTO> players, int currentPlayerIndex) {
        lblTableRightName.setText(players.get((currentPlayerIndex + 1) % players.size()).getUsername());
        lblTableTopName.setText(players.get((currentPlayerIndex + 2) % players.size()).getUsername());
        lblTableLeftName.setText(players.get((currentPlayerIndex + 3) % players.size()).getUsername());
    }

    /**
     * Afficher la carte jouée par un joueur
     *
     * @param card   la carte à afficher
     * @param player le joueur qui à jouer la carte
     */
    @Override
    public void displayCardPlayed(CardDTO card, PlayerDTO player) {
        Icon icon = loadResourceFor(card.getCardType(), card.getCardColor(), UNKNOWN_ICON);
        if (lblTableTopName.getText().equals(player.getUsername()))
            lblCardPlayedPlayerTop.setIcon(icon);
        else if (lblTableLeftName.getText().equals(player.getUsername()))
            lblCardPlayedPlayerLeft.setIcon(icon);
        else if (lblTableRightName.getText().equals(player.getUsername()))
            lblCardPlayedPlayerRight.setIcon(icon);
    }

    /**
     * Action à effectuer lorsque la fenêtre est fermée
     */
    @Override
    public void quit() {
        if (guiAuthentication != null)
            guiAuthentication.close();
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
     * @return l'option choisie dans le type donné
     */
    @Override
    public UserChoice askUser(String title, String question, List<UserChoice> possibilities) {
        UserChoice result = possibilities.size() > 0 ? possibilities.get(0) : null;
        if (possibilities.size() > 1) {
            result = (UserChoice) JOptionPane.showInputDialog(null,
                    question, title, JOptionPane.QUESTION_MESSAGE, null, possibilities.toArray(), result);
        }
        return result;
    }

    /**
     * Définir le nom de l'utilisateur en cours
     *
     * @param userName nom de l'utilisateur
     */
    @Override
    public void setUserName(String userName) {
        lblTableBottomName.setText(userName);
    }

    /**
     * Affichage du joueur qui fait atout
     *
     * @param userName nom de l'utilisateur
     */
    @Override
    public void setTrumpPlayer(String userName) {
        lblTrumpPlayer.setText(userName);
    }

    /**
     * Affichage de la couleur atout
     *
     * @param trumpColor couleur atout
     */
    @Override
    public void setTrumpColor(CardColor trumpColor) {
        BufferedImage trumpImage;
        try {
            trumpImage = ImageIO.read(GuiAssets.class.getResource("images/" + trumpColor.toString() + ".png"));
            lblTrumpColor.setIcon(new ImageIcon(trumpImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affichage du joueur qui a le tour
     *
     * @param userName joueur qui a le tour
     */
    @Override
    public void setCurrentPlayer(String userName) {
        lblCurrentPlayer.setText("C'est au tour de " + userName + ", temps restant :");
    }

    /**
     * Afficher la carte jouée par le joueur en cours
     *
     * @param card carte à afficher
     */
    @Override
    public void setBottomPlayerCard(CardDTO card) {
        lblCardPlayedPlayerBottom.setIcon(loadResourceFor(card.getCardType(), card.getCardColor(), UNKNOWN_ICON));
    }

    /**
     * Redéfinir la carte jouée à vide
     */
    @Override
    public void resetBottomPlayerCard() {
        lblCardPlayedPlayerBottom.setIcon(dropIcon);
    }

    /**
     * Afficher un message
     *
     * @param title   titre de la popup
     * @param message message à afficher
     */
    @Override
    public void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Afficher les points de l'équipe 1 à l'utilisateur
     *
     * @param points points à afficher
     */
    @Override
    public void setPointsTeam1(int points) {
        lblTeam1Points.setText(String.valueOf(points));
    }

    /**
     * Afficher les points de l'équipe 2 à l'utilisateur
     *
     * @param points points à afficher
     */
    @Override
    public void setPointsTeam2(int points) {
        lblTeam2Points.setText(String.valueOf(points));
    }

    /**
     * Afficher l'équipe gagnante provisoire
     *
     * @param team team à afficher
     */
    @Override
    public void setWinningTeam(String team) {
        lblWinningTeam.setText(team);
    }

    /**
     * Permet d'enlever l'affichage des cartes jouées
     */
    @Override
    public void resetPlayedCards() {
        lblCardPlayedPlayerTop.setIcon(dropIcon);
        lblCardPlayedPlayerRight.setIcon(dropIcon);
        lblCardPlayedPlayerBottom.setIcon(dropIcon);
        lblCardPlayedPlayerLeft.setIcon(dropIcon);
    }

    // Instanciation des ressources graphiques

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
     * Chargement des ressources images des cartes
     *
     * @param image image à charger
     * @return la ressource
     */
    public static DrawableRessource<ImageIcon> createResource(BufferedImage image) {
        return new GUIView.CardResource(image);
    }
}


