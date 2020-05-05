/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Player.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un joueur
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.network.Handler;
import ch.heigvd.aalamo.chibre.network.objects.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player {
    // Attributs
    private List<Card> cards = new ArrayList<>(Game.NB_CARDS_PLAYER);
    private Handler handler;
    private int id;
    private static int count = 1;
    private Game game;

    /**
     * Instancier un joueur
     *
     * @param handler handler relié
     */
    public Player(Handler handler) {
        if (handler == null)
            throw new IllegalArgumentException("Handler nul");

        this.handler = handler;
        this.id = count++;
        handler.setPlayer(this);
    }

    // Getters

    /**
     * @return la partie dans laquelle le joueur joue
     */
    public Game getGame() {
        return game;
    }

    /**
     * @return la liste des cartes du joueur
     */
    public List<Card> getCards() {
        return cards;
    }

    // Setters

    /**
     * Définir la partie dans lequel le joueur joue
     *
     * @param game la partie à laquelle il joue
     */
    public void setGame(Game game) {
        this.game = game;
    }

    // Méthodes

    /**
     * Distribuer une carte à un joueur
     *
     * @param card la carte
     */
    public void distributeCard(Card card) {
        if (card == null)
            throw new IllegalArgumentException("Carte nulle");
        cards.add(card);
    }

    /**
     * Envoyer une requête à la GUI du joueur via le réseau
     *
     * @param request la requête à envoyer
     */
    public void sendRequest(Request request) {
        if (request == null)
            throw new IllegalArgumentException("State nul");

        handler.sendRequest(request);
    }
}
