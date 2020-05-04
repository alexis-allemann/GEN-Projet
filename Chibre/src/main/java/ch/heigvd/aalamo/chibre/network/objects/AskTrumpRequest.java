package ch.heigvd.aalamo.chibre.network.objects;

import ch.heigvd.aalamo.chibre.engine.Card;

import java.util.List;

public class AskTrumpRequest implements Request{

    @Override
    public ServerAction getServerAction() {
        return ServerAction.ASK_TRUMP;
    }

    @Override
    public List<Card> getCards() {
        return null;
    }
}
