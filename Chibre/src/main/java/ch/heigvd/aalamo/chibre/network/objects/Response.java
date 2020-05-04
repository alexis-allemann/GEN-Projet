package ch.heigvd.aalamo.chibre.network.objects;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.engine.Card;

import java.io.Serializable;

public interface Response extends Serializable {
    UserAction getUserAction();

    CardColor getTrumpColor();

    Card getCardPlayed();
}
