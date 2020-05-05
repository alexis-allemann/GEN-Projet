/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Round.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un tour d'atout dans une partie
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.network.objects.Request;
import ch.heigvd.aalamo.chibre.network.objects.ServerAction;

import java.util.ArrayList;
import java.util.List;

public class Round {
    // Attributs
    private final int id;
    private static int count = 1;
    private CardColor trumpColor;
    private final List<Turn> turns = new ArrayList<>(Game.NB_CARDS_PLAYER);
    private final CardCollection cardCollection = new CardCollection();
    private final Game game;
    private boolean isPlayed;
    private Player trumpPlayer;

    /**
     * Instancier un tour de jeu
     */
    public Round(Game game) {
        this.game = game;
        this.id = count++;
        this.isPlayed = true;
    }

    // Getters

    /**
     * @return le joueur qui fait atout dans le tour
     */
    public Player getTrumpPlayer() {
        return trumpPlayer;
    }

    /**
     * @return la partie qui a instancier le tour
     */
    public Game getGame() {
        return game;
    }

    /**
     * @return la couleur atout du tour
     */
    public CardColor getTrumpColor() {
        return trumpColor;
    }

    /**
     * @return la table de jeu du tour
     */
    public Table getTable() {
        return game.getTable();
    }

    /**
     * @return la tour de table en cours, null si aucun tour de table n'a commencé
     */
    public Turn getCurrentTurn() {
        for (Turn turn : turns)
            if (turn.isPlayed())
                return turn;

        return null;
    }

    // Setters

    /**
     * Définir la couleur atout du tour
     *
     * @param trumpColor la couleur atout
     */
    public void setTrumpColor(CardColor trumpColor) {
        this.trumpColor = trumpColor;
        game.sendToAllPlayers(new Request(ServerAction.SEND_TRUMP_COLOR, trumpColor));
    }

    // Méthodes

    /**
     * Initialiser le tour
     */
    public void initRound() {

        for (Player player : game.getPlayers()) {
            if (cardCollection.distributeCards(player, Game.NB_CARDS_PLAYER) && game.getRounds().size() == 1)
                game.setFirstPlayerTrump(player);
            player.sendRequest(new Request(ServerAction.SEND_CARDS, player.getCards()));
        }

        this.trumpPlayer = game.getTable().nextTrumpPlayer(id, game.getTable().getPositionByPlayer(game.getFirstPlayerTrump()));


        game.sendToAllPlayers(new Request(ServerAction.SEND_TRUMP_PLAYER, trumpPlayer.getName()));

        game.sendToAllPlayers(new Request(ServerAction.SEND_CURRENT_PLAYER, trumpPlayer.getName()));

        trumpPlayer.sendRequest(new Request(ServerAction.ASK_TRUMP));
    }

    /**
     * Savoir si le tour en train d'être joué
     *
     * @return si le tour en train d'être joué
     */
    public boolean isPlayed() {
        return isPlayed;
    }

    /**
     * Définir que le tour est terminé
     */
    public void endRound() {
        isPlayed = false;
    }

    /**
     * Démarrer un tour de table
     */
    public void initTurn() {
        Turn turn = new Turn(this, null);
        turns.add(turn);
        turn.initTurn();
    }

    /**
     * Savoir si les joueurs ont encore des cartes à jouer dans le tour
     *
     * @return booléen si les joueurs ont des cartes ou non
     */
    public boolean playerCardsEmpty() {
        for (Player player : game.getPlayers()) {
            if (player.getCards().size() > 1)
                return false;
        }
        return true;
    }

    /**
     * Savoir si le tour de jeu est le premier de la partie
     *
     * @return booléen si c'est le premier ou non
     */
    public boolean isFirstRound() {
        return id == 1;
    }

    /**
     * Ajouter un tour de table au tour de jeu
     *
     * @param turn tour de table à ajouter
     */
    public void addTurn(Turn turn) {
        turns.add(turn);
    }
}
