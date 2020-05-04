package ch.heigvd.aalamo.chibre.network.objects;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.engine.Card;

public interface Response {

    UserAction getUserAction();

    CardColor getTrumpColor();

    Card getCard();

}
