package ch.heigvd.aalamo.chibre.network.objects;

import java.io.Serializable;

public enum ServerAction implements Serializable {
    SEND_CARDS,
    ASK_TRUMP,
    ASK_ANNOUNCEMENT,
    ASK_CARD
}
