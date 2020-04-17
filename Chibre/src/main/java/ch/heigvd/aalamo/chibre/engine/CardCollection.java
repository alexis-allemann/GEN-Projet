package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardCollection {
    // Attributs
    private List<Card> collection = new ArrayList(Game.NB_CARDS);
    private Random random = new Random();

    /**
     * Instancier une collection de cartes
     */
    public CardCollection() {
        for (CardColor color : CardColor.values())
            for (CardType type : CardType.values())
                collection.add(new Card(type, color));
    }

    /**
     * Distribuer les cartes
     *
     * @param player utilisateur à qui distribuer
     * @param nb     le nombre de cartes
     */
    public void distributeCards(Player player, int nb) {
        for (int i = 0; i < nb; ++i) {
            int cardIndex = random.nextInt(collection.size());
            player.distributeCard(collection.get(cardIndex));
            collection.remove(cardIndex);
        }
    }
}
