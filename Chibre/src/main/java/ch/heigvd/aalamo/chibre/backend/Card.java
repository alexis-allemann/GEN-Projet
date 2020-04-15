/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Card.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une carte
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.backend;

import java.io.Serializable;

public class Card implements Serializable {
    // Attributs
    private CardType cardType;
    private CardColor cardColor;
    private String imgSrc;
    private Player player;

    /**
     * Instanciation d'une carte
     *
     * @param cardType  type de la carte
     * @param cardColor couleur de la carte
     */
    public Card(CardType cardType, CardColor cardColor) {
        this.cardType = cardType;
        this.cardColor = cardColor;
        setImgSrc(cardType, cardColor);
    }

    /**
     * Définition de l'url de l'image
     *
     * @param cardType  type de la carte
     * @param cardColor couleur de la carte
     */
    private void setImgSrc(CardType cardType, CardColor cardColor) {
        this.imgSrc = "img/" + cardColor.name() + "-" + cardType.name() + ".png";
    }

    /**
     * @return type de la carte
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * @return la couleur de la carte
     */
    public CardColor getCardColor() {
        return cardColor;
    }

    /**
     * @return l'URL de l'image de la carte
     */
    public String getImgSrc() {
        return imgSrc;
    }
}
