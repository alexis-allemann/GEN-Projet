/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Card.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une carte
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;

import java.io.Serializable;

public class Card implements Serializable{
    // Attributs
    private CardType cardType;
    private CardColor cardColor;

    /**
     * Instanciation d'une carte
     *
     * @param cardType  type de la carte
     * @param cardColor couleur de la carte
     */
    public Card(CardType cardType, CardColor cardColor) {
        this.cardType = cardType;
        this.cardColor = cardColor;
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
