package ch.heigvd.aalamo.chibre.network.objects;

import ch.heigvd.aalamo.chibre.engine.Card;

import java.util.List;

public class AskCardRequest implements Request{
    @Override
    public ServerAction getServerAction() {
        return ServerAction.ASK_CARD;
    }

    @Override
    public List<Card> getCards() {
        return null;
    }
}
