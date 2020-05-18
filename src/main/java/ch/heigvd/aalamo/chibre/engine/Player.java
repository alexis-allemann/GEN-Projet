/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Player.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un joueur
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.CardType;
import ch.heigvd.aalamo.chibre.network.Handler;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.CardDTO;
import ch.heigvd.aalamo.chibre.network.objects.DTOs.PlayerDTO;
import ch.heigvd.aalamo.chibre.network.objects.Request;
import ch.heigvd.aalamo.chibre.network.objects.ServerAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Player {
    // Attributs
    private final List<Card> cards = new ArrayList<>(Game.NB_CARDS_PLAYER);
    private Handler handler;
    private Team team; // TODO : a supprimer
    private Game game;
    private String username;
    private String password;

    /**
     * Instancier un joueur
     *
     * @param username nom d'utilisateur
     * @param password mot de passe
     */
    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters

    /**
     * @return le nom du joueur
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return le mot de passe haché de l'utilisateur
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return la partie dans laquelle le joueur joue
     */
    public Game getGame() {
        return game;
    }

    /**
     * @return la liste des cartes du joueur
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * @return l'équipe du joueur
     */
    public Team getTeam() {
        return team;
    }

    public Card getCardWithId(int id) {
        for (Card card : cards)
            if (card.getId() == id)
                return card;
        return null;
    }

    // Setters

    /**
     * Définir le handler du joueur
     *
     * @param handler relié au joueur
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
        handler.setPlayer(this);
    }

    /**
     * Définir la partie dans lequel le joueur joue
     *
     * @param game la partie à laquelle il joue
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Définir l'équipre
     *
     * @param team équipe du joueur
     */
    public void setTeam(Team team) {
        this.team = team;
    }


    // Méthodes

    /**
     * Distribuer une carte à un joueur
     *
     * @param card la carte
     */
    public void distributeCard(Card card) {
        if (card == null)
            throw new IllegalArgumentException("Carte nulle");
        System.out.print("Distribue <" + card.getCardColor().toString() + "><" + card.getCardType().toString() + ">");
        System.out.println(" à " + username);
        cards.add(card);
    }

    /**
     * Envoyer une requête à la GUI du joueur via le réseau
     *
     * @param request la requête à envoyer
     */
    public void sendRequest(Request request) {
        if (request == null)
            throw new IllegalArgumentException("Requête nulle");

        handler.sendRequest(request);
    }

    public PlayerDTO serialize() {
        List<CardDTO> cardsDto = new ArrayList<>(Game.NB_CARDS_PLAYER);
        for (Card card : cards)
            cardsDto.add(new CardDTO(card.getCardColor(), card.getCardType(), card.getId()));

        return new PlayerDTO(username, cardsDto);
    }

    /**
     * Envoie toutes les annonces sur le réseau
     */
    public void sendAnnoucements() {
        List<Announcement> announcements = threeCardsAnnouncement();
        if(!announcements.isEmpty())
            for(Announcement announcement : announcements){
                sendRequest(new Request(ServerAction.SEND_ANNOUCEMENTS, announcement.serialize()));
                game.getCurrentRound().addPossibleAnnouncement(announcement);
            }

        announcements = fourCardsAnnouncement();
        if(!announcements.isEmpty())
            for(Announcement announcement : announcements)
                sendRequest(new Request(ServerAction.SEND_ANNOUCEMENTS, announcement.serialize()));

        announcements = squareCardsAnnouncement();
        if(!announcements.isEmpty())
            for(Announcement announcement : announcements)
                sendRequest(new Request(ServerAction.SEND_ANNOUCEMENTS, announcement.serialize()));

        announcements = suiteAnnouncement();
        if(!announcements.isEmpty())
            for(Announcement announcement : announcements)
                sendRequest(new Request(ServerAction.SEND_ANNOUCEMENTS, announcement.serialize()));

        if(squareNineAnnouncement() != null)
            sendRequest(new Request(ServerAction.SEND_ANNOUCEMENTS, squareNineAnnouncement().serialize()));

        if(squareJackAnnouncement() != null)
            sendRequest(new Request(ServerAction.SEND_ANNOUCEMENTS, squareJackAnnouncement().serialize()));
    }

    /**
     * @return L'annonce d'un carré de valet
     */
    private Announcement squareJackAnnouncement() {
        return squareCards(CardType.JACK);
    }

    /**
     * @return L'annonce d'un carré de neufs
     */
    private Announcement squareNineAnnouncement() {
        return squareCards(CardType.NINE);
    }

    /**
     * @return une liste avec toutes les annonces avec des carrés
     */
    private List<Announcement> squareCardsAnnouncement() {
        List<Announcement> announcements = new ArrayList<>();
        for(CardType type : CardType.values())
            if(type != CardType.JACK && type != CardType.NINE)
                announcements.add(squareCards(type));

        return announcements;
    }

    /**
     *
     * @param type Type de la carte
     * @return L'annonce du carré du type de la carte si elle existe
     */
    private Announcement squareCards(CardType type) {
        List<Card> square = new ArrayList<>();
        for(Card card : cards)
            if(card.getCardType() == type)
                square.add(card);

        if(square.size() == 4)
            return new Announcement(this, BonusType.SQUARE_CARDS, null);
        else
            return null;
    }

    /**
     * @return une liste avec toutes les annonces à plus que quatre cartes
     */
    private List<Announcement> suiteAnnouncement() {
        List<Announcement> announcements = new ArrayList<>();
        for(int i = cards.size() - 1; i > 4; --i){
            if(cards.get(i).getCardColor() == cards.get(i - 1).getCardColor() &&
                    cards.get(i - 1).getCardColor() == cards.get(i - 2).getCardColor() &&
                    cards.get(i - 2).getCardColor() == cards.get(i - 3).getCardColor() &&
                    cards.get(i - 3).getCardColor() == cards.get(i - 4).getCardColor() &&
                    cards.get(i).getCardType().getOrder() == cards.get(i - 1).getCardType().getOrder() - 1 &&
                    cards.get(i - 1).getCardType().getOrder() == cards.get(i - 2).getCardType().getOrder() - 1 &&
                    cards.get(i - 2).getCardType().getOrder() == cards.get(i - 3).getCardType().getOrder() - 1 &&
                    cards.get(i - 3).getCardType().getOrder() == cards.get(i - 4).getCardType().getOrder() - 1)
                announcements.add(new Announcement(
                        this,BonusType.SUITE, cards.get(i)
                ));
        }
        return announcements;
    }

    /**
     * @return une liste avec toutes les annonces à quatre cartes
     */
    private List<Announcement> fourCardsAnnouncement() {
        List<Announcement> announcements = new ArrayList<>();
        for(int i = cards.size() - 1; i > 3; --i){
            if(cards.get(i).getCardColor() == cards.get(i - 1).getCardColor() &&
                    cards.get(i - 1).getCardColor() == cards.get(i - 2).getCardColor() &&
                    cards.get(i - 2).getCardColor() == cards.get(i - 3).getCardColor() &&
                    cards.get(i).getCardType().getOrder() == cards.get(i - 1).getCardType().getOrder() - 1 &&
                    cards.get(i - 1).getCardType().getOrder() == cards.get(i - 2).getCardType().getOrder() - 1 &&
                    cards.get(i - 2).getCardType().getOrder() == cards.get(i - 3).getCardType().getOrder() - 1)
                announcements.add(new Announcement(
                        this,BonusType.FOUR_CARDS, cards.get(i)
                ));
        }
        return announcements;
    }

    /**
     * @return une liste avec toutes les annonces à trois cartes
     */
    private List<Announcement> threeCardsAnnouncement() {
        List<Announcement> announcements = new ArrayList<>();
        for(int i = cards.size() - 1; i > 2; --i){
            if(cards.get(i).getCardColor() == cards.get(i - 1).getCardColor() &&
                    cards.get(i - 1).getCardColor() == cards.get(i - 2).getCardColor() &&
                    cards.get(i).getCardType().getOrder() == cards.get(i - 1).getCardType().getOrder() - 1 &&
                    cards.get(i - 1).getCardType().getOrder() == cards.get(i - 2).getCardType().getOrder() - 1)
                announcements.add(new Announcement(
                        this,BonusType.THREE_CARDS, cards.get(i)
                ));
        }
        return announcements;
    }
}
