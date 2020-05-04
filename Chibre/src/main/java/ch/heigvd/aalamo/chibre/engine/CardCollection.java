package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardCollection {
    // Attributs
    private final List<Card> collection = new ArrayList(Game.NB_CARDS);
    private final Random random = new Random();

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
     * @return true si le joueur a le 7 de carreau
     */
    public boolean distributeCards(Player player, int nb) {
        boolean hasDiamondSeven = false; // Pour savoir si le joueur fait atout au premier tour
        for (int i = 0; i < nb; ++i) {
            int cardIndex = random.nextInt(collection.size());
            Card currentCard = collection.get(cardIndex);

            // Si c'est le 7 de carreau
            if(currentCard.getCardColor() == CardColor.DIAMOND && currentCard.getCardType() == CardType.SEVEN)
                hasDiamondSeven = true;

            player.distributeCard(currentCard);
            currentCard.setPlayer(player);

            collection.remove(cardIndex);
        }

        return hasDiamondSeven;
    }
}
