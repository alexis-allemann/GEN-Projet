package ch.heigvd.aalamo.chibre.network.objects;

import ch.heigvd.aalamo.chibre.engine.Card;

import java.util.List;

public class SendCardsRequest implements Request{

    private List<Card> cards;

    public SendCardsRequest(List<Card> cards){
        this.cards = cards;
    }

    @Override
    public ServerAction getServerAction() {
        return ServerAction.SEND_CARDS;
    }

    public List<Card> getCards() {
        return cards;
    }
}
