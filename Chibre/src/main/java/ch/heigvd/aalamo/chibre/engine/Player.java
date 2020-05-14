/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Player.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un joueur
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.network.Handler;
import ch.heigvd.aalamo.chibre.network.objects.CardDTO;
import ch.heigvd.aalamo.chibre.network.objects.PlayerDTO;
import ch.heigvd.aalamo.chibre.network.objects.Request;
import ch.heigvd.aalamo.chibre.network.objects.TeamDTO;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    // Attributs
    private List<Card> cards = new ArrayList<>(Game.NB_CARDS_PLAYER);
    private Handler handler;
    private int id;
    private Team team; // TODO : a supprimer
    private static int count = 1;
    private Game game;
    private String name;

    /**
     * Instancier un joueur
     *
     * @param handler handler relié
     */
    public Player(Handler handler) {
        if (handler == null)
            throw new IllegalArgumentException("Handler nul");

        this.handler = handler;
        this.id = count++;
        handler.setPlayer(this);
    }

    // Getters

    /**
     * @return le nom du joueur
     */
    public String getName() {
        return name;
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
     * @return l'id du joueur
     */
    public int getId() {
        return id;
    }

// Setters

    /**
     * Définir la partie dans lequel le joueur joue
     *
     * @param game la partie à laquelle il joue
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Définir le nom du joueur
     *
     * @param name nom du joueur
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Définir l'équipre
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
        System.out.println("Ajout de la carte");
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

    PlayerDTO serialize() {
        List<CardDTO> cardsDto = new ArrayList<>(Game.NB_CARDS_PLAYER);
        for(Card card : cards)
            cardsDto.add(new CardDTO(card.getCardColor(), card.getCardType()));

        return new PlayerDTO(this.id, this.name, cardsDto);
    }
}
