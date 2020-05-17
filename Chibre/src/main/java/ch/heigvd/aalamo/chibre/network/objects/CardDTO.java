package ch.heigvd.aalamo.chibre.network.objects;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;

import java.io.Serializable;

public class CardDTO implements DTO {
    private CardColor cardColor;
    private CardType cardType;
    private int id;

    public CardDTO(CardColor cardColor, CardType cardType, int id) {
        this.cardColor = cardColor;
        this.cardType = cardType;
        this.id = id;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public CardType getCardType() {
        return cardType;
    }

    public int getId() {
        return id;
    }
}
