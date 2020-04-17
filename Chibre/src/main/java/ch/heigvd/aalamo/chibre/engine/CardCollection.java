package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardCollection {

    private List<Card> collection = new ArrayList();
    private Random random = new Random();

    public CardCollection(){
        for(CardColor color : CardColor.values())
            for(CardType type : CardType.values())
                collection.add(new Card(type,color));
    }

    public void distributeCards(Player player, int nb){
        for(int i = 0; i < nb; ++i){
            int cardIndex = random.nextInt(collection.size());
            player.distributeCard(collection.get(cardIndex));
            collection.remove(cardIndex);
        }
    }
}
