package ch.heigvd.aalamo.chibre.network.objects;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.engine.Card;

public class SendCardPlayedResponse implements Response{

    // Atttributs
    private Card cardPlayed;

    public SendCardPlayedResponse(Card cardPlayed) {
        this.cardPlayed = cardPlayed;
    }

    @Override
    public UserAction getUserAction() {
        return UserAction.PLAY_CARD;
    }

    @Override
    public CardColor getTrumpColor() {
        return null;
    }

    @Override
    public Card getCardPlayed() {
        return null;
    }
}
