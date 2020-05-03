package ch.heigvd.aalamo.chibre.network.objects;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.engine.Card;
import ch.heigvd.aalamo.chibre.engine.Game;
import ch.heigvd.aalamo.chibre.engine.Player;
import ch.heigvd.aalamo.chibre.engine.Team;

import java.util.ArrayList;
import java.util.List;

public class State {
    // Attributs
    private Team team1, team2;
    private Player currentPlayer;
    private List<Card> cards = new ArrayList<>(Game.NB_CARDS_PLAYER);
    private CardColor trumpColor;
    private UserAction userAction;

    // Getters
    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Card> getCards() {
        return cards;
    }

    public CardColor getTrumpColor() {
        return trumpColor;
    }

    // Setter
    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void setTrumpColor(CardColor trumpColor) {
        this.trumpColor = trumpColor;
    }
}
