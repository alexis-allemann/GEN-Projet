/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     CardDTO.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Objet de transfer de données pour une carte
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.network.objects.DTOs;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;

public class CardDTO implements DTO {
    // Attributs
    private final CardColor cardColor;
    private final CardType cardType;
    private final int id;

    /**
     * Instancier un DTO pour une carte
     *
     * @param cardColor la couleur de la carte
     * @param cardType  le type de la carte
     * @param id        l'identification de la carte
     */
    public CardDTO(CardColor cardColor, CardType cardType, int id) {
        this.cardColor = cardColor;
        this.cardType = cardType;
        this.id = id;
    }

    // Getters

    /**
     * @return la couleur de la carte
     */
    public CardColor getCardColor() {
        return cardColor;
    }

    /**
     * @return le type de la carte
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * @return l'identifiant de la carte
     */
    public int getId() {
        return id;
    }
}
