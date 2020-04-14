package ch.heigvd.aalamo.chibre.server;

public class Card {
    private CardType cardType;
    private CardColor cardColor;
    private String imgSrc;

    public Card(CardType cardType, CardColor cardColor){
        this.cardType = cardType;
        this.cardColor = cardColor;
        setImgSrc(cardType, cardColor);
    }

    private void setImgSrc(CardType cardType, CardColor cardColor){
        this.imgSrc = "img/" + cardColor.name() + "-" + cardType.name() + ".png";
    }

    public CardType getCardType() {
        return cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public String getImgSrc() {
        return imgSrc;
    }
}
