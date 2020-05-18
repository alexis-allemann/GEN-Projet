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
import ch.heigvd.aalamo.chibre.ChibreController;
import ch.heigvd.aalamo.chibre.ChibreView;
import ch.heigvd.aalamo.chibre.engine.Game;
import ch.heigvd.aalamo.chibre.network.objects.*;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.AuthenticationDTO;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.CardDTO;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.PlayerDTO;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.TeamDTO;
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
    private List<CardDTO> cards = new ArrayList<>(Game.NB_CARDS_PLAYER);
    private PlayerDTO currentPlayer;
    private PlayerDTO hasTurnPlayer;
    private PlayerDTO trumpPlayer;
    private CardDTO lastCardPlayed;
    private Timer timer;

    /**
     * Instancier un utilisateur de l'application
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
                        break;
                    case SEND_TRUMP_COLOR:
                        view.setTrumpColor((CardColor) request.getObject());
                        break;
                    case SEND_CURRENT_PLAYER:
                        hasTurnPlayer = (PlayerDTO) request.getObject();
                        view.setCurrentPlayer(hasTurnPlayer.getUsername());
                        break;
                    case SEND_CARD_PLAYED:
                        CardDTO cardPlayed = (CardDTO) request.getObject();
                        view.displayCardPlayed(cardPlayed, hasTurnPlayer);
                        break;
                    case SEND_POINTS:
                        teams = (List<TeamDTO>) request.getObject();
                        view.setPoints(teams.get(0).getPoints(), teams.get(1).getPoints());
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
                        if (trumpPlayer != null && trumpPlayer.equals(currentPlayer))
                            choices.add(new UserChoice("Chibrer", null));

                        UserChoice choice = view.askUser("Choix atout", "Quel couleur voulez-vous faire atout ?", choices);

                        // Envoi du choix de l'utilisateur qui fait atout
                        chooseTrump((CardColor) choice.getValue());
                    case ASK_ANNOUNCEMENT:
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

        if (!hasTurnPlayer.equals(currentPlayer)) {
            view.showMessage("Ce n'est pas votre tour", "C'est le tour de " + hasTurnPlayer.getUsername());
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
     * Fermeture de la GUI
     */
    @Override
    public void quit() {
        view.quit();
    }
}
