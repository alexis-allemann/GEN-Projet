/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Card.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une carte
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.server;

public class Card {
    private CardType cardType;
    private CardColor cardColor;
    private String imgSrc;

    public Card(CardType cardType, CardColor cardColor) {
        this.cardType = cardType;
        this.cardColor = cardColor;
        setImgSrc(cardType, cardColor);
    }

    private void setImgSrc(CardType cardType, CardColor cardColor) {
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
