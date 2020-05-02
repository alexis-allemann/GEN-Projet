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
    private boolean isPlayed = true; // TODO : remettre à zéro quand le round est fini
    Player firstPlayerTrump;

    /**
     * Instancier un tour de jeu
     */
    public Round(Game game) {
        this.game = game;
        this.id = count++;
    }

    public Player getTrumpPlayer() {
        return game.getTable().nextTrumpPlayer(id, game.getTable().getPlayerPosition(firstPlayerTrump));
    }

    public void run(){
        
        for(Player player : game.getPlayers()){
            if(cardCollection.distributeCards(player, Game.NB_CARDS_PLAYER) && game.getRounds().size() == 1)
                firstPlayerTrump = player;
        }

        while(!playerCardsEmpty()){
            Turn turn = new Turn(this);
            turns.add(turn);
            turn.run();
        }

        isPlayed = false;
    }

    private boolean playerCardsEmpty() {
        for(Player player : game.getPlayers()){
            if(player.getCards().size() > 1)
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
