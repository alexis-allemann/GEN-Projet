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
import ch.heigvd.aalamo.chibre.network.objects.State;
import ch.heigvd.aalamo.chibre.network.objects.UserAction;

import java.util.ArrayList;
import java.util.List;

public class Round {
    // Attributs
    private final int id;
    private static int count = 1;
    private CardColor trumpColor;
    private List<Turn> turns = new ArrayList<>(Game.NB_CARDS_PLAYER);
    private final CardCollection cardCollection = new CardCollection();
    private final Game game;
    private boolean isPlayed = true;
    Player trumpPlayer;


    /**
     * Instancier un tour de jeu
     */
    public Round(Game game) {
        this.game = game;
        this.id = count++;
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
            State state = new State();
            state.setCards(player.getCards());
            state.setUserAction(UserAction.SEND_CARDS);
            player.sendState(state);
        }

        this.trumpPlayer = game.getTable().nextTrumpPlayer(id, game.getTable().getPlayerPosition(game.getFirstPlayerTrump()));

        State state = new State();
        state.setUserAction(UserAction.CHOOSE_TRUMP);
        trumpPlayer.sendState(state);

        /*
        while (!playerCardsEmpty()) {
            Turn turn = new Turn(this);
            turns.add(turn);
            turn.run();
        }

        isPlayed = false;*/
    }

    private boolean playerCardsEmpty() {
        for (Player player : game.getPlayers()) {
            if (player.getCards().size() > 1)
                return false;
        }
        return true;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public Game getGame() {
        return game;
    }
}
