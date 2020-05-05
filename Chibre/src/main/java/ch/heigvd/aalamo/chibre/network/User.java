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

    /**
     * Instancier un utilisateur de l'application
     */
    public User() {
        try {
            socket = new Socket("localhost", 6666);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
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
        receive();
    }

    /**
     * Envoi d'une carte
     */
    @Override
    public void sendCard(int index) {
        if (index >= cards.size())
            throw new IndexOutOfBoundsException("index plus grand que le nombre de cartes");

        try {
            out.writeObject(new Response(UserAction.PLAY_CARD, cards.get(index)));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println("Carte envoyée");
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
}
