package ch.heigvd.aalamo.chibre.network.objects;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.engine.Card;
import ch.heigvd.aalamo.chibre.engine.Game;
import ch.heigvd.aalamo.chibre.engine.Player;
import ch.heigvd.aalamo.chibre.engine.Team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class State implements Serializable {
    // Attributs
    private List<Card> cards = new ArrayList<>(Game.NB_CARDS_PLAYER);
    private CardColor trumpColor;
    private UserAction userAction;

    // Getters
    public List<Card> getCards() {
        return cards;
    }

    public CardColor getTrumpColor() {
        return trumpColor;
    }

    public UserAction getUserAction() {
        return userAction;
    }

    // Setter
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void setTrumpColor(CardColor trumpColor) {
        this.trumpColor = trumpColor;
    }

    public void setUserAction(UserAction userAction) {
        this.userAction = userAction;
    }
}
