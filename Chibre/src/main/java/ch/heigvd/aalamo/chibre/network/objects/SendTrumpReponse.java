package ch.heigvd.aalamo.chibre.network.objects;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.engine.Card;

public class SendTrumpReponse implements Response{

    // Attributs
    CardColor trumpColor;

    public SendTrumpReponse(CardColor trumpColor) {
        this.trumpColor = trumpColor;
    }

    @Override
    public UserAction getUserAction() {
        return UserAction.SEND_TRUMP;
    }

    @Override
    public CardColor getTrumpColor() {
        return trumpColor;
    }

    @Override
    public Card getCardPlayed() {
        return null;
    }
}
