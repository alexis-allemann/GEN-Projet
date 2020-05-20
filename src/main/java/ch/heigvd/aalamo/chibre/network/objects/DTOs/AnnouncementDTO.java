/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     AnnouncementDTO.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Objet de transfert de données pour une annonce
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.network.objects.DTOs;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;
import ch.heigvd.aalamo.chibre.engine.BonusType;

import java.util.List;

public class AnnouncementDTO implements DTO {
    // Attributs
    private int id;
    private BonusType bonusType;
    private CardType typeOfSquare;
    private CardColor colorOfSchtockr;
    private List<CardDTO> suiteCards;
    private PlayerDTO player;

    /**
     * Instanciation d'un DTO pour une annonce
     *
     * @param player    joueur qui a l'annonce
     * @param id        de l'annonce
     * @param bonusType type de l'annonce
     */
    public AnnouncementDTO(PlayerDTO player, int id, BonusType bonusType) {
        this.id = id;
        this.bonusType = bonusType;
        this.player = player;
    }

    // Getters

    /**
     * @return id unique de l'annonce
     */
    public int getId() {
        return id;
    }

    /**
     * @return type de bonus de l'annonce
     */
    public BonusType getBonusType() {
        return bonusType;
    }

    /**
     * @return le type de carré
     */
    public CardType getTypeOfSquare() {
        return typeOfSquare;
    }

    /**
     * @return la couleur du Schtöckr
     */
    public CardColor getColorOfSchtockr() {
        return colorOfSchtockr;
    }

    /**
     * @return les cartes de la suite
     */
    public List<CardDTO> getSuiteCards() {
        return suiteCards;
    }

    /**
     * @return le joueur qui a fait l'annonce
     */
    public PlayerDTO getPlayer() {
        return player;
    }

    // Setters

    /**
     * Définir le type de carré
     *
     * @param typeOfSquare type des cartes du carré
     */
    public void setTypeOfSquare(CardType typeOfSquare) {
        this.typeOfSquare = typeOfSquare;
    }

    /**
     * Définir la couleur du Schtöcker
     *
     * @param colorOfSchtockr couleur des cartes atout
     */
    public void setColorOfSchtockr(CardColor colorOfSchtockr) {
        this.colorOfSchtockr = colorOfSchtockr;
    }

    /**
     * Définir la suite de cartes
     *
     * @param suiteCards liste des cartes de la suite
     */
    public void setSuiteCards(List<CardDTO> suiteCards) {
        this.suiteCards = suiteCards;
    }
}
