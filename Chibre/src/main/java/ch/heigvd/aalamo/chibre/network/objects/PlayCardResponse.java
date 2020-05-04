package ch.heigvd.aalamo.chibre.network.objects;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.engine.Card;

public class PlayCardResponse implements Response{
    private final Card card;

    public PlayCardResponse(Card card){
        this.card = card;
    }

    @Override
    public UserAction getUserAction() {
        return null;
    }

    @Override
    public CardColor getTrumpColor() {
        return null;
    }

    @Override
    public Card getCard() {
        return card;
    }
}
