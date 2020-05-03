package ch.heigvd.aalamo.chibre.network.objects;

import java.io.Serializable;

public enum UserAction implements Serializable {
    SEND_CARDS,
    PLAY_CARD,
    CHOOSE_TRUMP,
    MAKE_ANNOUNCEMENT
}
