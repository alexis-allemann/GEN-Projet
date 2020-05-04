package ch.heigvd.aalamo.chibre.network.objects;

import ch.heigvd.aalamo.chibre.CardColor;

public interface Response {
    UserAction getUserAction();

    CardColor getTrumpColor();

}
