/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Announcement.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une annonce
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.AnnouncementDTO;

import java.util.ArrayList;
import java.util.List;

public class Announcement {
    // Attributs
    private BonusType bonusType;
    private Card bestCard;
    private int nbSuiteCards;
    private static int count = 1;
    private int id;

    /**
     * Instanciation d'une annonce
     *
     * @param bonusType Type de l'annonce
     */
    public Announcement(BonusType bonusType) {
        this(bonusType, null);
    }

    /**
     * Instanciation d'une annonce
     *
     * @param bonusType Type de l'annonce
     * @param bestCard  Meilleure carte de l'annonce
     */
    public Announcement(BonusType bonusType, Card bestCard) {
        this(bonusType, bestCard, 0);
    }

    /**
     * Instanciation d'une annonce
     *
     * @param bonusType    Type de l'annonce
     * @param bestCard     Meilleure carte de l'annonce
     * @param nbSuiteCards nombre de cartes à la suite
     */
    public Announcement(BonusType bonusType, Card bestCard, int nbSuiteCards) {
        this.bonusType = bonusType;
        this.bestCard = bestCard;
        this.nbSuiteCards = nbSuiteCards;
        this.id = count++;
    }

    // Getters

    /**
     * @return l'id de l'annonce
     */
    public int getId() {
        return id;
    }

    /**
     * @return le type de l'annonce
     */
    public BonusType getBonusType() {
        return bonusType;
    }

    /**
     * @return le nombre de cartes à la suite
     */
    public int getNbSuiteCards() {
        return nbSuiteCards;
    }

    /**
     * @return le joueur qui a fait l'annonce
     */
    public Player getPlayer() {
        if (bestCard != null)
            return bestCard.getPlayer();
        return null;
    }

    /**
     * @return l'équipe qui a fait l'annonce
     */
    public Team getTeam() {
        if (bestCard != null)
            return bestCard.getPlayer().getTeam();
        return null;
    }

    /**
     * @return les points de l'annonce
     */
    public int getPoints() {
        return bonusType.getPoints();
    }

    /**
     * @return l'ordre de la meilleure carte de l'annonce
     */
    public int getOrder() {
        if (bestCard != null)
            return bestCard.getCardType().getOrder();
        return 0;
    }

    /**
     * @return la couleur de la meilleure carte de l'annonce
     */
    public CardColor getCardColor() {
        if (bestCard != null)
            return bestCard.getCardColor();
        return null;
    }

    // Méthodes

    /**
     * Détecter un carré de valet
     *
     * @return L'annonce d'un carré de valet
     */
    public static Announcement squareJackAnnouncement(List<Card> cards) {
        return squareCards(cards, CardType.JACK);
    }

    /**
     * Détecter un carré de neufs
     *
     * @return L'annonce d'un carré de neufs
     */
    public static Announcement squareNineAnnouncement(List<Card> cards) {
        return squareCards(cards, CardType.NINE);
    }

    /**
     * Détecter un carré de cartes
     *
     * @return une liste avec toutes les annonces avec des carrés
     */
    public static List<Announcement> squareCardsAnnouncement(List<Card> cards) {
        List<Announcement> announcements = new ArrayList<>();
        for (CardType type : CardType.values())
            if (type != CardType.JACK && type != CardType.NINE) {
                Announcement announcement = squareCards(cards, type);
                if (announcement != null)
                    announcements.add(announcement);
            }

        return announcements;
    }

    /**
     * Détecte une annonce de type carré d'un type donné
     *
     * @param type Type de la carte
     * @return L'annonce du carré du type de la carte si elle existe
     */
    private static Announcement squareCards(List<Card> cards, CardType type) {
        int count = 0;
        for (Card card : cards)
            if (card.getCardType() == type)
                count++;

        if (count == 4)
            return new Announcement(BonusType.SQUARE_CARDS, null);
        else
            return null;
    }

    /**
     * Trouver les annonces de type suite de cartes
     *
     * @return une liste avec toutes les annonces de type suite
     */
    public static List<Announcement> suiteAnnouncement(List<Card> cards) {
        List<Announcement> announcements = new ArrayList<>();
        int count = 1;
        Card lastCard = cards.get(0);
        for (Card card : cards) {
            // Si la carte en cours est la carte de la même couleur qui suit la carte précédente
            if (card.getId() - 1 == lastCard.getId() && card.getCardColor() == lastCard.getCardColor())
                count++;
            else if (count >= 3) { // Si on a un série d'au moins 3 cartes
                announcements.add(addSuite(cards.get(cards.indexOf(lastCard) - count + 1), count));
                count = 1; // Réinitialiser le compteur
            } else
                count = 1; // Réinitialiser le compteur

            lastCard = card;
        }

        if (count >= 3)
            announcements.add(addSuite(cards.get(cards.indexOf(lastCard) - count + 1), count));

        return announcements;
    }

    /**
     * Ajout d'une annonce de type suite
     *
     * @param lastCard la dernière carte
     * @param count    le compteur
     * @return l'annonce
     */
    private static Announcement addSuite(Card lastCard, int count) {
        switch (count) {
            case 3:
                return new Announcement(BonusType.THREE_CARDS, lastCard);
            case 4:
                return new Announcement(BonusType.FOUR_CARDS, lastCard);
            default: // 5 cartes ou plus à la suite
                return new Announcement(BonusType.SUITE, lastCard, count);
        }
    }

    /**
     * Détection d'une annonce Schtöckr
     *
     * @return l'annonce si elle est détectée sinon null
     */
    public static Announcement schtockrAnnouncement(List<Card> cards, CardColor trumpColor) {
        boolean hasTrumpKing = false;
        boolean hasTrumpQueen = false;
        for (Card card : cards)
            if (card.getCardColor() == trumpColor)
                if (card.getCardType() == CardType.KING)
                    hasTrumpKing = true;
                else if (card.getCardType() == CardType.QUEEN)
                    hasTrumpQueen = true;

        if (hasTrumpKing && hasTrumpQueen)
            return new Announcement(BonusType.SCHTOCKR);
        return null;
    }

    /**
     * Sérialisation d'une annonce
     *
     * @return la DTO de l'annonce
     */
    public AnnouncementDTO serialize() {
        if (bestCard != null)
            return new AnnouncementDTO(id, bonusType, bestCard.serialize());
        else
            return new AnnouncementDTO(id, bonusType);
    }
}
