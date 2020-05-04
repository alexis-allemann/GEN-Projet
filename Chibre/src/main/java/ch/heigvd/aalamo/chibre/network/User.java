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
     * Lecture du réseau afin de recevoir des objets et des les envoyer à la GUI
     */
    private void receive() {
        Request request;
        try {
            while ((request = (Request) in.readObject()) != null) {
                switch (request.getServerAction()) {
                    case SEND_CARDS:
                        cards = new ArrayList<>(Game.NB_CARDS_PLAYER);
                        for (Card card : request.getCards()) {
                            view.addCard(card.getCardType(), card.getCardColor(), cards.size());
                            cards.add(card);
                        }
                        break;
                    case ASK_TRUMP:
                        ChibreView.UserChoice choice = view.askUser("Choix atout", "Quel couleur voulez-vous faire atout ?",
                                new ChibreView.UserChoice() {
                                    @Override
                                    public String textValue() {
                                        return "Carreau";
                                    }

                                    @Override
                                    public String toString() {
                                        return "Carreau";
                                    }
                                },
                                new ChibreView.UserChoice() {
                                    @Override
                                    public String textValue() {
                                        return "Coeur";
                                    }

                                    @Override
                                    public String toString() {
                                        return "Coeur";
                                    }
                                },
                                new ChibreView.UserChoice() {
                                    @Override
                                    public String textValue() {
                                        return "Pique";
                                    }

                                    @Override
                                    public String toString() {
                                        return "Pique";
                                    }
                                },
                                new ChibreView.UserChoice() {
                                    @Override
                                    public String textValue() {
                                        return "Trèfle";
                                    }

                                    @Override
                                    public String toString() {
                                        return "Trèfle";
                                    }
                                },
                                new ChibreView.UserChoice() {
                                    @Override
                                    public String textValue() {
                                        return "Chibrer";
                                    }

                                    @Override
                                    public String toString() {
                                        return "Chibrer";
                                    }
                                });

                        CardColor trumpColor = null;
                        switch (choice.toString()) {
                            case "Carreau":
                                trumpColor = CardColor.DIAMOND;
                                break;
                            case "Coeur":
                                trumpColor = CardColor.HEART;
                                break;
                            case "Pique":
                                trumpColor = CardColor.SPADE;
                                break;
                            case "Trèfle":
                                trumpColor = CardColor.CLUB;
                                break;
                        }
                        chooseTrump(trumpColor);
                        break;
                    case ASK_ANNOUNCEMENT:
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Envoi d'une carte au serveur
     *
     * @throws IndexOutOfBoundsException si l'index dépasse la taille du tableau
     */
    public void sendCard(int index) {
        if (index >= cards.size())
            throw new IndexOutOfBoundsException("index plus grand que le nombre de cartes");

        try {
            out.writeObject(new SendCardPlayedResponse(cards.get(index)));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println("Carte envoyée");
    }

    @Override
    public void chooseTrump(CardColor color) {
        try {
            out.writeObject(new SendTrumpReponse(color));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println("Choix atout envoyé");
    }
}
