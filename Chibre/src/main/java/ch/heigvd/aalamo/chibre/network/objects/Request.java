package ch.heigvd.aalamo.chibre.network.objects;

import ch.heigvd.aalamo.chibre.engine.Card;

import java.io.Serializable;
import java.util.List;


public interface Request extends Serializable {

    ServerAction getServerAction();
    List<Card> getCards();
}
