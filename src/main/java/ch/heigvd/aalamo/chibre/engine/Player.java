/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Player.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un joueur
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.CardColor;
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
    private boolean hasSchtockr;

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

    /**
     * Retrouver la carte avec l'ID
     *
     * @param id de la carte
     * @return la carte correspondante à l'id donné
     */
    public Card getCardWithId(int id) {
        for (Card card : cards)
            if (card.getId() == id)
                return card;
        return null;
    }

    /**
     * @return si le joueur a Schtockr
     */
    public boolean hasSchtockr() {
        return hasSchtockr;
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

    /**
     * Définir si le joueur a Schtockr
     *
     * @param hasSchtockr s'il a l'annonce Schtockr
     */
    public void setHasSchtockr(boolean hasSchtockr) {
        this.hasSchtockr = hasSchtockr;
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

    /**
     * Séralisation d'un joueur en DTO
     *
     * @return le DTO du joueur
     */
    public PlayerDTO serialize() {
        List<CardDTO> cardsDto = new ArrayList<>(Game.NB_CARDS_PLAYER);
        for (Card card : cards)
            cardsDto.add(new CardDTO(card.getCardColor(), card.getCardType(), card.getId()));

        return new PlayerDTO(username, cardsDto);
    }

    /**
     * Envoi de toutes les annonces sur le réseau
     */
    public void sendAnnoucements(CardColor trumpColor) {
        // Suite de cartes (3 à 5 ou plus)
        List<Announcement> announcements = Announcement.suiteAnnouncement(cards);
        if (!announcements.isEmpty())
            for (Announcement announcement : announcements) {
                sendRequest(new Request(ServerAction.SEND_ANNOUCEMENTS, announcement.serialize()));
                getGame().getCurrentRound().addPossibleAnnouncement(announcement);
            }

        // Carré de cartes
        announcements = Announcement.squareCardsAnnouncement(cards);
        if (!announcements.isEmpty())
            for (Announcement announcement : announcements) {
                sendRequest(new Request(ServerAction.SEND_ANNOUCEMENTS, announcement.serialize()));
                getGame().getCurrentRound().addPossibleAnnouncement(announcement);
            }

        // Carré de neufs
        Announcement squareNineAnnouncement = Announcement.squareNineAnnouncement(cards);
        if (squareNineAnnouncement != null) {
            sendRequest(new Request(ServerAction.SEND_ANNOUCEMENTS, squareNineAnnouncement.serialize()));
            getGame().getCurrentRound().addPossibleAnnouncement(squareNineAnnouncement);
        }

        // Carré de valets
        Announcement squareJackAnnouncement = Announcement.squareJackAnnouncement(cards);
        if (squareJackAnnouncement != null) {
            sendRequest(new Request(ServerAction.SEND_ANNOUCEMENTS, squareJackAnnouncement.serialize()));
            getGame().getCurrentRound().addPossibleAnnouncement(squareJackAnnouncement);
        }

        // Schtöckr
        Announcement schtockrAnnouncement = Announcement.schtockrAnnouncement(cards, trumpColor);
        if (schtockrAnnouncement != null) {
            sendRequest(new Request(ServerAction.SEND_ANNOUCEMENTS, schtockrAnnouncement.serialize()));
            getGame().getCurrentRound().addPossibleAnnouncement(schtockrAnnouncement);
        }
    }
}
