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
import ch.heigvd.aalamo.chibre.network.objects.AskTrumpRequest;
import ch.heigvd.aalamo.chibre.network.objects.SendCardsRequest;

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
    Player trumpPlayer;


    /**
     * Instancier un tour de jeu
     */
    public Round(Game game, boolean isPlayed) {
        this.game = game;
        this.id = count++;
        this.isPlayed = isPlayed;
    }

    public Player getTrumpPlayer() {
        return trumpPlayer;
    }

    public void setTrumpColor(CardColor trumpColor) {
        this.trumpColor = trumpColor;
    }

    public void initRound() {

        for (Player player : game.getPlayers()) {
            if (cardCollection.distributeCards(player, Game.NB_CARDS_PLAYER) && game.getRounds().size() == 1)
                game.setFirstPlayerTrump(player);
            player.sendState(new SendCardsRequest(player.getCards()));
        }

        this.trumpPlayer = game.getTable().nextTrumpPlayer(id, game.getTable().getPositionByPlayer(game.getFirstPlayerTrump()));

        trumpPlayer.sendState(new AskTrumpRequest());
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void endRound() {
        isPlayed = false;
    }

    public Game getGame() {
        return game;
    }

    public void initTurn(){
        Turn turn = new Turn(this, null);
        turns.add(turn);
        turn.initTurn();
    }

    public CardColor getTrumpColor() {
        return trumpColor;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public boolean playerCardsEmpty() {
        for (Player player : game.getPlayers()) {
            if (player.getCards().size() > 1)
                return false;
        }
        return true;
    }

    public boolean isFirstRound(){
        return id == 1;
    }

    public Table getTable(){
        return game.getTable();
    }

    public Turn getCurrentTurn(){
        for (Turn turn : turns)
            if(turn.isPlayed())
                return turn;

        return null;
    }

    public void addTurn(Turn turn){
        turns.add(turn);
    }
}
