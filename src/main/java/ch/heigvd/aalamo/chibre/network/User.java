/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     User.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un utilisateur du chibre
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.network;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;
import ch.heigvd.aalamo.chibre.ChibreController;
import ch.heigvd.aalamo.chibre.ChibreView;
import ch.heigvd.aalamo.chibre.engine.Game;
import ch.heigvd.aalamo.chibre.network.objects.*;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.*;
import ch.heigvd.aalamo.chibre.view.gui.GUIErrorFrame;
import ch.heigvd.aalamo.chibre.view.gui.UserChoice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class User implements ChibreController {
    // Attributs
    private ChibreView view;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final List<CardDTO> cards = new ArrayList<>(Game.NB_CARDS_PLAYER);
    private List<Boolean> markedCards = new ArrayList<>(Arrays.asList(false, false, false, false, false, false, false, false, false));
    private PlayerDTO currentPlayer;
    private PlayerDTO hasTurnPlayer;
    private PlayerDTO trumpPlayer;
    private CardColor trumpColor;
    private CardColor firstCardColor = null;

    /**
     * Instancier un utilisateur de l'application
     */
    public User() {
        try {
            Socket socket = new Socket("localhost", 6666);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            new GUIErrorFrame("Aucun serveur n'est disponible sur le réseau");
            throw new RuntimeException("Aucun serveur n'est disponible");
        }
    }

    // Méthodes

    /**
     * Lecture du réseau afin de recevoir des objets et des les envoyer à la GUI
     */
    private void receive() {
        Request request;
        try {
            while ((request = (Request) in.readObject()) != null) {
                // Selon le type de requête du serveur
                switch (request.getAction()) {
                    case AUTHENTICATION_FAILED:
                        view.authenticationFailed();
                        break;
                    case AUTHENTICATION_SUCCEED:
                        view.authenticationSucceed();
                        currentPlayer = (PlayerDTO) request.getObject();
                        view.setUserName(currentPlayer.getUsername());
                        break;
                    case CREATE_USER_FAILED:
                        view.createUserFailed();
                        break;
                    case SEND_TEAMS:
                        List<TeamDTO> teams = (List<TeamDTO>) request.getObject();
                        view.displayTeams(teams.get(0), teams.get(1));
                        List<PlayerDTO> players = new ArrayList<>();
                        players.add(teams.get(0).getPlayers().get(0));
                        players.add(teams.get(1).getPlayers().get(0));
                        players.add(teams.get(0).getPlayers().get(1));
                        players.add(teams.get(1).getPlayers().get(1));
                        view.displayPlayers(players, players.indexOf(currentPlayer));
                        break;
                    case SEND_CARDS:
                        CardDTO card = (CardDTO) request.getObject();
                        view.addCard(card.getCardType(), card.getCardColor(), cards.size());
                        cards.add(card);
                        break;
                    case SEND_TRUMP_PLAYER:
                        PlayerDTO trumpPlayerName = (PlayerDTO) request.getObject();
                        view.setTrumpPlayer(trumpPlayerName.getUsername());
                        trumpPlayer = trumpPlayerName;
                        view.setInfoMessage(trumpPlayerName.getUsername() + " choisi l'atout");
                        break;
                    case SEND_CHIBRE:
                        PlayerDTO trumpPlayer = (PlayerDTO) request.getObject();
                        view.setInfoMessage(this.trumpPlayer.getUsername() + " a chibré, " + trumpPlayer.getUsername() + " choisi l'atout");
                        break;
                    case SEND_TRUMP_COLOR:
                        trumpColor = (CardColor) request.getObject();
                        view.setTrumpColor(trumpColor);
                        break;
                    case SEND_CURRENT_PLAYER:
                        hasTurnPlayer = (PlayerDTO) request.getObject();
                        if (hasTurnPlayer.getUsername().equals(currentPlayer.getUsername()))
                            view.setInfoMessage("C'est votre tour, jouez une carte");
                        else
                            view.setInfoMessage("C'est au tour de " + hasTurnPlayer.getUsername());
                        break;
                    case SEND_CARD_PLAYED:
                        CardDTO cardPlayed = (CardDTO) request.getObject();
                        view.displayCardPlayed(cardPlayed, hasTurnPlayer);
                        if (firstCardColor == null)
                            firstCardColor = cardPlayed.getCardColor();
                        break;
                    case SEND_POINTS:
                        teams = (List<TeamDTO>) request.getObject();
                        view.setPoints(teams.get(0).getPoints(), teams.get(1).getPoints());
                        break;
                    case SEND_RESET_CARDS:
                        firstCardColor = null;
                        view.resetPlayedCards();
                        break;
                    case SEND_WINNING_PLAYER:
                        hasTurnPlayer = null;
                        PlayerDTO winningPlayer = (PlayerDTO) request.getObject();
                        view.setInfoMessage(winningPlayer.getUsername() + " remporte le tour de table");
                        break;
                    case ASK_TRUMP:
                        view.setInfoMessage("Vous devez choisir l'atout");

                        List<UserChoice> choices = new ArrayList<>();
                        choices.add(new UserChoice("Carreau", CardColor.DIAMOND));
                        choices.add(new UserChoice("Coeur", CardColor.HEART));
                        choices.add(new UserChoice("Pique", CardColor.SPADE));
                        choices.add(new UserChoice("Trèfle", CardColor.CLUB));

                        // Le choix de chibre n'est disponible que si on est le joueur qui fait atout
                        if (this.trumpPlayer != null && this.trumpPlayer.equals(currentPlayer))
                            choices.add(new UserChoice("Chibrer", null));
                        else
                            view.setInfoMessage("Vous devez choisir l'atout car votre coéquipier a chibré");

                        UserChoice choice = view.askUser("Choix atout", "Quel couleur voulez-vous faire atout ?", choices);

                        // Envoi du choix de l'utilisateur qui fait atout
                        chooseTrump((CardColor) choice.getValue());
                        break;
                    case END_ROUND:
                        cards.clear();
                        markedCards = new ArrayList<>(Arrays.asList(false, false, false, false, false, false, false, false, false));
                        view.resetWinningAnnouncement();
                        break;
                    case SEND_WINNER:
                        view.setInfoMessage("Partie terminée");
                        view.setWinner((TeamDTO) request.getObject());
                        break;
                    case SEND_ANNOUCEMENTS:
                        AnnouncementDTO announcement = (AnnouncementDTO) request.getObject();
                        view.displayAnnouncement(announcement);
                        break;
                    case DISPLAY_ANNOUNCEMENT:
                        announcement = (AnnouncementDTO) request.getObject();
                        view.displayAnnouncementPlayed(announcement, hasTurnPlayer);
                        break;
                    case WINNING_ANNOUNCEMENT:
                        view.resetAnnouncementsDisplayed();
                        announcement = (AnnouncementDTO) request.getObject();
                        view.displayWinningAnnouncement(announcement);
                        break;
                }
            }
        } catch (IOException |
                ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    // Méthodes du controlleur

    /**
     * Lancement de la vue
     *
     * @param view la vue à utiliser
     */
    @Override
    public void start(ChibreView view) {
        if (view == null)
            throw new RuntimeException("La vue est requise");

        this.view = view;
        displayAuthentication();
        receive();
    }

    /**
     * Envoi d'une carte
     */
    @Override
    public void sendCard(int index) {
        if (index >= cards.size())
            throw new IndexOutOfBoundsException("index plus grand que le nombre de cartes");

        if (hasTurnPlayer == null || !hasTurnPlayer.equals(currentPlayer)) {
            view.showMessage("Ce n'est pas votre tour", hasTurnPlayer != null ? "C'est le tour de " + hasTurnPlayer.getUsername() : "La carte ne peut pas être jouée maintenant");
            resetCard(index);
        } else {
            CardColor playedColor = cards.get(index).getCardColor();
            boolean isCardValid = true;
            if (firstCardColor != null && playedColor != firstCardColor && playedColor != trumpColor) {
                for (int i = 0; i < cards.size() && isCardValid; ++i)
                    if (!markedCards.get(i) && cards.get(i).getCardColor() == firstCardColor) {
                        if (firstCardColor == trumpColor && cards.get(i).getCardType() != CardType.JACK)
                            isCardValid = false;
                        else if (firstCardColor != trumpColor)
                            isCardValid = false;
                    }
            }

            if (!isCardValid) {
                view.showMessage("La carte ne peut pas être jouée", "Carte jouée invalide");
                resetCard(index);
            } else {
                sendResponse(new Response(UserAction.PLAY_CARD, cards.get(index)));
                markedCards.set(index, true);
                System.out.println("Carte envoyée");
                view.resetAnnouncements();
            }
        }
    }

    /**
     * Remettre la carte jouée à vide car elle n'est pas valide
     *
     * @param index de la carte jouée
     */
    private void resetCard(int index) {
        view.resetBottomPlayerCard();
        view.addCard(cards.get(index).getCardType(), cards.get(index).getCardColor(), index);
    }

    /**
     * Affichage de l'authentification
     */
    @Override
    public void displayAuthentication() {
        view.displayAuthentication();
    }

    /**
     * Envoi de l'authentification au serveur
     *
     * @param username nom de l'utilisateur
     * @param password mot de passe haché
     */
    @Override
    public void sendAuthentication(String username, String password) {
        sendResponse(new Response(UserAction.AUTHENTICATION, new AuthenticationDTO(username, toHexString(getSHA(password)))));
        System.out.println("Authentification envoyée");
    }

    /**
     * Méthode pour créer un utilisateur
     *
     * @param username nom de l'utilisateur
     * @param password mot de passe
     */
    @Override
    public void sendCreateUser(String username, String password) {
        sendResponse(new Response(UserAction.CREATE_USER, new AuthenticationDTO(username, toHexString(getSHA(password)))));
        System.out.println("Création de l'utilisateur " + username);
    }

    /**
     * Envoi du choix de l'atout
     *
     * @param color couleur de l'atout
     */
    @Override
    public void chooseTrump(CardColor color) {
        sendResponse(new Response(UserAction.SEND_TRUMP, color));
        System.out.println("Choix atout envoyé");
    }

    /**
     * Envoi d'une réponse au serveur
     *
     * @param response à envoyer
     */
    private void sendResponse(Response response) {
        try {
            out.writeObject(response);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Envoyer une annonce
     *
     * @param announcement l'annonce à envoyer
     */
    @Override
    public void sendAnnouncement(AnnouncementDTO announcement) {
        sendResponse(new Response(UserAction.SEND_ANNOUCEMENT, announcement));
    }

    /**
     * Fermeture de la GUI
     */
    @Override
    public void quit() {
        view.quit();
    }
}
