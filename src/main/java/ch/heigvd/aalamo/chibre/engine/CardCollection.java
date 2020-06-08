package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;
import ch.heigvd.aalamo.chibre.network.objects.Request;
import ch.heigvd.aalamo.chibre.network.objects.ServerAction;

import java.util.*;

public class CardCollection {
    // Attributs
    private final List<Card> collection = new ArrayList<>(Game.NB_CARDS);
    private final Random random = new Random();

    /**
     * Instancier une collection de cartes
     */
    public CardCollection() {
        for (CardColor color : CardColor.values())
            for (CardType type : CardType.values())
                collection.add(new Card(type, color, collection.size()));
    }

    // Méthodes

    /**
     * Distribuer les cartes
     *
     * @param player utilisateur à qui distribuer
     * @param nb     le nombre de cartes
     * @return true si le joueur a le 7 de carreau
     */
    public boolean distributeCards(Player player, int nb) {
        System.out.println("Distribution des cartes");
        boolean hasDiamondSeven = false; // Pour savoir si le joueur fait atout au premier tour
        for (int i = 0; i < nb; ++i) {
            int cardIndex = 0;
            if (player.getGame().isRandomDistribution())
                cardIndex = random.nextInt(collection.size());
            Card currentCard = collection.get(cardIndex);

            // Si c'est le 7 de carreau
            if (currentCard.getCardColor() == CardColor.DIAMOND && currentCard.getCardType() == CardType.SEVEN)
                hasDiamondSeven = true;

            player.distributeCard(currentCard);
            currentCard.setPlayer(player);

            collection.remove(cardIndex);
        }

        // Permet un affichage plus harmonieux et facilite le calcul des annonces
        player.getCards().sort(Comparator.comparing(Card::getId));

        // Envoi des cartes après tri
        for (Card card : player.getCards())
            player.sendRequest(new Request(ServerAction.SEND_CARDS, card.serialize()));

        return hasDiamondSeven;
    }
}
