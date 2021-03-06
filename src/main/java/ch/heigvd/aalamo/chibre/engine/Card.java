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
import ch.heigvd.aalamo.chibre.network.objects.DTOs.CardDTO;

public class Card{
    // Attributs
    private final CardType cardType;
    private final CardColor cardColor;
    private Player player;
    private final int id;

    /**
     * Instanciation d'une carte
     *
     * @param cardType  type de la carte
     * @param cardColor couleur de la carte
     * @param id        identifiant de la carte
     */
    public Card(CardType cardType, CardColor cardColor, int id) {
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.id = id;
    }

    // Getters

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
     * @return le joueur qui possède la carte
     */
    public Player getPlayer() {
        return player;
    }

    public int getId() {
        return id;
    }

    // Setters

    /**
     * Définir le joueur qui possède la carte
     *
     * @param player le joueur
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    // Méthodes

    /**
     * Sérialisation d'une carte
     *
     * @return le DTO de la carte sérialisée
     */
    public CardDTO serialize() {
        return new CardDTO(cardColor, cardType, id);
    }
}
