/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreUser.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un utilisateur du chibre
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.network;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.ChibreController;
import ch.heigvd.aalamo.chibre.ChibreView;
import ch.heigvd.aalamo.chibre.engine.Card;
import ch.heigvd.aalamo.chibre.engine.Game;
import ch.heigvd.aalamo.chibre.network.objects.*;
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
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private List<Card> cards = new ArrayList<>(Game.NB_CARDS_PLAYER);
    private Card lastCardPlayed;
    private String playerName;
    private String currentPlayer;
    private String topPlayer;
    private String rightPlayer;
    private String leftPlayer;
    private String trumpPlayer;
    private int pointsTeam1;
    private int pointsTeam2;
    private int points;
    private Timer timer;

    /**
     * Instancier un utilisateur de l'application
     *
     * @throws RuntimeException si aucun serveur n'est disponible sur le réseau
     */
    public User() {
        try {
            socket = new Socket("localhost", 6666);
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
                        playerName = ((PlayerDTO) request.getObject()).getUsername();
                        view.setUserName(playerName);
                        break;
                    case CREATE_USER_FAILED:
                        view.createUserFailed();
                        break;
                    case SEND_PLAYER_NAMES:
                        // Affichage des joueurs
                        if (request.getObject() instanceof List) {
                            List<String> names = (List<String>) request.getObject();
                            int index = names.indexOf(playerName);
                            if (index >= 2) {
                                if (index == 2) {
                                    view.setTopPlayerName(names.get(3));
                                    view.setRightPlayerName(names.get(1));
                                    view.setLeftPlayerName(names.get(0));
                                    topPlayer = names.get(3);
                                    rightPlayer = names.get(1);
                                    leftPlayer = names.get(0);
                                }
                                if (index == 3) {
                                    view.setTopPlayerName(names.get(2));
                                    view.setRightPlayerName(names.get(0));
                                    view.setLeftPlayerName(names.get(1));
                                    topPlayer = names.get(2);
                                    rightPlayer = names.get(0);
                                    leftPlayer = names.get(1);
                                }
                            } else {
                                if (index == 0) {
                                    view.setTopPlayerName(names.get(1));
                                    view.setRightPlayerName(names.get(2));
                                    view.setLeftPlayerName(names.get(3));
                                    topPlayer = names.get(1);
                                    rightPlayer = names.get(2);
                                    leftPlayer = names.get(3);
                                }
                                if (index == 1) {
                                    view.setTopPlayerName(names.get(0));
                                    view.setRightPlayerName(names.get(3));
                                    view.setLeftPlayerName(names.get(2));
                                    topPlayer = names.get(0);
                                    rightPlayer = names.get(3);
                                    leftPlayer = names.get(2);
                                }
                            }
                            view.setTeam1Player1(names.get(0));
                            view.setTeam1Player2(names.get(1));
                            view.setTeam2Player1(names.get(2));
                            view.setTeam2Player2(names.get(3));
                        }
                        break;
                    case SEND_CARDS:
                        // Affichage des cartes reçues
                        if (request.getObject() instanceof List) {
                            cards = new ArrayList<>(Game.NB_CARDS_PLAYER);
                            for (Card card : (List<Card>) request.getObject()) {
                                view.addCard(card.getCardType(), card.getCardColor(), cards.size());
                                cards.add(card);
                            }
                        }
                        break;
                    case SEND_TRUMP_PLAYER:
                        String trumpPlayerName = (String) request.getObject();
                        view.setTrumpPlayer(trumpPlayerName);
                        trumpPlayer = trumpPlayerName;
                        break;
                    case SEND_TRUMP_COLOR:
                        view.setTrumpColor((CardColor) request.getObject());
                        break;
                    case SEND_CURRENT_PLAYER:
                        currentPlayer = (String) request.getObject();
                        view.setCurrentPlayer(currentPlayer);
                        break;
                    case SEND_CARD_PLAYED:
                        Card card = (Card) request.getObject();
                        if (currentPlayer.equals(topPlayer))
                            view.setTopPlayerCard(card);
                        else if (currentPlayer.equals(leftPlayer))
                            view.setLeftPlayerCard(card);
                        else if (currentPlayer.equals(rightPlayer))
                            view.setRightPlayerCard(card);
                        break;
                    case SEND_POINTS_TEAM1:
                        points = (int) request.getObject();
                        view.setPointsTeam1(points);
                        pointsTeam1 = points;
                        setWinningTeam();
                        break;
                    case SEND_POINTS_TEAM2:
                        points = (int) request.getObject();
                        view.setPointsTeam2(points);
                        pointsTeam2 = points;
                        setWinningTeam();
                        break;
                    case SEND_RESET_CARDS:
                        // Attente de 5 secondes puis réinitalisation du tapis de jeu
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                view.resetPlayedCards();
                            }
                        }, 5000);
                        break;
                    case ASK_TRUMP:
                        List<UserChoice> choices = new ArrayList<>();
                        choices.add(new UserChoice("Carreau", CardColor.DIAMOND));
                        choices.add(new UserChoice("Coeur", CardColor.HEART));
                        choices.add(new UserChoice("Pique", CardColor.SPADE));
                        choices.add(new UserChoice("Trèfle", CardColor.CLUB));

                        // Le choix de chibre n'est disponible que si on est le joueur qui fait atout
                        if (trumpPlayer != null && trumpPlayer.equals(playerName))
                            choices.add(new UserChoice("Chibrer", null));

                        UserChoice choice = view.askUser("Choix atout", "Quel couleur voulez-vous faire atout ?", choices);

                        // Envoi du choix de l'utilisateur qui fait atout
                        chooseTrump((CardColor) choice.getValue());

                    case ASK_ANNOUNCEMENT:
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Définir l'équipe qui gagne provisoirement
     */
    private void setWinningTeam() {
        if (pointsTeam1 > pointsTeam2)
            view.setWinningTeam("Equipe 1");
        else if (pointsTeam1 < pointsTeam2)
            view.setWinningTeam("Equipe 2");
        else
            view.setWinningTeam("");
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

        if (!currentPlayer.equals(playerName)) {
            view.showMessage("Ce n'est pas votre tour", "C'est le tour de " + currentPlayer);
            if (lastCardPlayed != null)
                view.setBottomPlayerCard(cards.get(cards.indexOf(lastCardPlayed)));
            else
                view.resetBottomPlayerCard();
            view.addCard(cards.get(index).getCardType(), cards.get(index).getCardColor(), index);
        } else {
            sendResponse(new Response(UserAction.PLAY_CARD, cards.get(index)));
            System.out.println("Carte envoyée");
        }
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
     * Fermeture de la GUI
     */
    @Override
    public void quit() {
        view.quit();
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
}
