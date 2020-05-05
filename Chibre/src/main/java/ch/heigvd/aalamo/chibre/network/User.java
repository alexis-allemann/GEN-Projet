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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
    private int pointsTeam1;
    private int pointsTeam2;
    private int points;

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
                        view.setTrumpPlayer((String) request.getObject());
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
                        if(currentPlayer.equals(topPlayer))
                            view.setTopPlayerCard(card);
                        else if(currentPlayer.equals(leftPlayer))
                            view.setLeftPlayerCard(card);
                        else if(currentPlayer.equals(rightPlayer))
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
                        view.resetPlayedCards();
                        break;
                    case ASK_TRUMP:
                        // Création du choix utilisateur
                        ChibreView.UserChoice choice = view.askUser("Choix atout", "Quel couleur voulez-vous faire atout ?",
                                new ChibreView.UserChoice() {
                                    @Override
                                    public Object value() {
                                        return CardColor.DIAMOND;
                                    }

                                    @Override
                                    public String toString() {
                                        return "Carreau";
                                    }
                                },
                                new ChibreView.UserChoice() {
                                    @Override
                                    public Object value() {
                                        return CardColor.HEART;
                                    }

                                    @Override
                                    public String toString() {
                                        return "Coeur";
                                    }
                                },
                                new ChibreView.UserChoice() {
                                    @Override
                                    public Object value() {
                                        return CardColor.SPADE;
                                    }

                                    @Override
                                    public String toString() {
                                        return "Pique";
                                    }
                                },
                                new ChibreView.UserChoice() {
                                    @Override
                                    public Object value() {
                                        return CardColor.CLUB;
                                    }

                                    @Override
                                    public String toString() {
                                        return "Trèfle";
                                    }
                                },
                                new ChibreView.UserChoice() {
                                    @Override
                                    public Object value() {
                                        return null;
                                    }

                                    @Override
                                    public String toString() {
                                        return "Chibrer";
                                    }
                                });

                        // Envoi du choix de l'utilisateur qui fait atout
                        chooseTrump((CardColor) choice.value());

                    case ASK_ANNOUNCEMENT:
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
        String playerName = view.askUser("Identification", "Quel est votre nom : ");
        view.setUserName(playerName);
        sendPlayerName(playerName);
        receive();
    }

    /**
     * Envoi du nom du joueur
     *
     * @param name nom du joueur
     */
    @Override
    public void sendPlayerName(String name) {
        try {
            playerName = name;
            out.writeObject(new Response(UserAction.SEND_NAME, name));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println("Nom envoyé");
    }

    /**
     * Envoi d'une carte
     */
    @Override
    public void sendCard(int index) {
        if (index >= cards.size())
            throw new IndexOutOfBoundsException("index plus grand que le nombre de cartes");

        if(! currentPlayer.equals(playerName)){
            view.showMessage("Ce n'est pas votre tour", "C'est le tour de " + currentPlayer);
            if(lastCardPlayed != null)
                view.setBottomPlayerCard(cards.get(cards.indexOf(lastCardPlayed)));
            else
                view.resetBottomPlayerCard();
            view.addCard(cards.get(index).getCardType(), cards.get(index).getCardColor(), index);
        }
        else{
            try {
                out.writeObject(new Response(UserAction.PLAY_CARD, cards.get(index)));
                lastCardPlayed = cards.get(index);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            System.out.println("Carte envoyée");
        }
    }

    /**
     * Envoi du choix de l'atout
     *
     * @param color couleur de l'atout
     */
    @Override
    public void chooseTrump(CardColor color) {
        try {
            out.writeObject(new Response(UserAction.SEND_TRUMP, color));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println("Choix atout envoyé");
    }

    private void setWinningTeam(){
        if(pointsTeam1 > pointsTeam2)
            view.setWinningTeam("Equipe 1");
        else if(pointsTeam1 < pointsTeam2)
            view.setWinningTeam("Equipe 2");
        else
            view.setWinningTeam("");
    }
}
