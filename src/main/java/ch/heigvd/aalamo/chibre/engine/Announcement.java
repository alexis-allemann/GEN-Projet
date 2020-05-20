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
import ch.heigvd.aalamo.chibre.network.objects.DTOs.CardDTO;

import java.util.ArrayList;
import java.util.List;

public class Announcement {
    // Attributs
    private BonusType bonusType;
    private Card bestCard;
    private int nbSuiteCards;
    private static int count = 1;
    private int id;
    private CardType typeOfSquare;
    private CardColor colorOfSchtockr;
    private List<CardDTO> suiteCards;
    private Player player;

    /**
     * Instanciation d'une annonce
     *
     * @param bonusType Type de l'annonce
     */
    public Announcement(Player player, BonusType bonusType) {
        this(player, bonusType, null);
    }

    /**
     * Instanciation d'une annonce
     *
     * @param bonusType Type de l'annonce
     * @param bestCard  Meilleure carte de l'annonce
     */
    public Announcement(Player player, BonusType bonusType, Card bestCard) {
        this(player, bonusType, bestCard, 0);
    }

    /**
     * Instanciation d'une annonce
     *
     * @param bonusType    Type de l'annonce
     * @param bestCard     Meilleure carte de l'annonce
     * @param nbSuiteCards nombre de cartes à la suite
     */
    public Announcement(Player player, BonusType bonusType, Card bestCard, int nbSuiteCards) {
        this.bonusType = bonusType;
        this.bestCard = bestCard;
        this.nbSuiteCards = nbSuiteCards;
        this.id = count++;
        this.player = player;
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
        return player;
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

    // Setters

    public void setTypeOfSquare(CardType typeOfSquare) {
        this.typeOfSquare = typeOfSquare;
    }

    public void setColorOfSchtockr(CardColor colorOfSchtockr) {
        this.colorOfSchtockr = colorOfSchtockr;
    }

    public void setSuiteCards(List<CardDTO> suiteCards) {
        this.suiteCards = suiteCards;
    }

    // Méthodes

    /**
     * Détecter un carré de cartes
     *
     * @return une liste avec toutes les annonces avec des carrés
     */
    public static List<Announcement> squareCardsAnnouncement(List<Card> cards) {
        List<Announcement> announcements = new ArrayList<>();

        for (CardType type : CardType.values()) {
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

        Announcement announcement = null;

        Player player = cards.get(0).getPlayer();
        if (count == 4 && type != CardType.JACK && type != CardType.NINE) {
            announcement = new Announcement(player, BonusType.SQUARE_CARDS, null);
            announcement.setTypeOfSquare(type);
        } else if (count == 4 && type == CardType.JACK) {
            announcement = new Announcement(player, BonusType.SQUARE_JACKS, null);
            announcement.setTypeOfSquare(CardType.JACK);
        } else if (count == 4) {
            announcement = new Announcement(player, BonusType.SQUARE_NINE, null);
            announcement.setTypeOfSquare(CardType.NINE);
        }

        return announcement;
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
                announcements.add(addSuite(cards, cards.get(cards.indexOf(lastCard) - count + 1), count));
                count = 1; // Réinitialiser le compteur
            } else
                count = 1; // Réinitialiser le compteur

            lastCard = card;
        }

        if (count >= 3)
            announcements.add(addSuite(cards, cards.get(cards.indexOf(lastCard) - count + 1), count));

        return announcements;
    }

    /**
     * Ajout d'une annonce de type suite
     *
     * @param lastCard la dernière carte
     * @param count    le compteur
     * @return l'annonce
     */
    private static Announcement addSuite(List<Card> cards, Card lastCard, int count) {
        Announcement announcement;
        Player player = cards.get(0).getPlayer();
        switch (count) {
            case 3:
                announcement = new Announcement(player, BonusType.THREE_CARDS, lastCard);
                break;
            case 4:
                announcement = new Announcement(player, BonusType.FOUR_CARDS, lastCard);
                break;
            default: // 5 cartes ou plus à la suite
                announcement = new Announcement(player, BonusType.SUITE, lastCard, count);
                break;
        }

        // Ajout des cartes de la suite
        List<CardDTO> suiteCards = new ArrayList<>(count);
        for (int i = 1; i <= count; ++i)
            suiteCards.add(cards.get(cards.indexOf(lastCard) + count - i).serialize());

        announcement.setSuiteCards(suiteCards);

        return announcement;
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

        Announcement announcement = null;
        Player player = cards.get(0).getPlayer();
        if (hasTrumpKing && hasTrumpQueen) {
            announcement = new Announcement(player, BonusType.SCHTOCKR);
            announcement.setColorOfSchtockr(trumpColor);
            player.setHasSchtockr(true);
        } else
            player.setHasSchtockr(false);

        return announcement;
    }

    /**
     * Sérialisation d'une annonce
     *
     * @return la DTO de l'annonce
     */
    public AnnouncementDTO serialize() {
        AnnouncementDTO announcementDTO = new AnnouncementDTO(player.serialize(), id, bonusType);

        // Ajout des informations nécessaires en fonction du type d'annonce
        switch (announcementDTO.getBonusType()) {
            case SCHTOCKR:
                announcementDTO.setColorOfSchtockr(colorOfSchtockr);
                break;
            case THREE_CARDS:
            case FOUR_CARDS:
            case SUITE:
                announcementDTO.setSuiteCards(suiteCards);
                break;
            case SQUARE_CARDS:
            case SQUARE_NINE:
            case SQUARE_JACKS:
                announcementDTO.setTypeOfSquare(typeOfSquare);
                break;
        }
        return announcementDTO;
    }
}
