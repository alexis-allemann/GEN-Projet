/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Turn.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un tour de table
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.network.objects.Request;
import ch.heigvd.aalamo.chibre.network.objects.ServerAction;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    // Attributs
    private final List<Card> cards = new ArrayList<>(Game.NB_PLAYERS);
    private final Round round;
    private boolean isPlayed;
    private Player firstPlayer;
    private Player winner;
    private final Turn lastTurn;

    /**
     * Instanciation d'un tour de table
     *
     * @param round    tour de jeu du tour de table
     * @param lastTurn dernier tour de table joué
     */
    public Turn(Round round, Turn lastTurn) {
        this.round = round;
        this.lastTurn = lastTurn;
        this.isPlayed = true;
    }

    // Getters

    /**
     * @return le joueur qui a posé la meilleure carte
     */
    public Player getWinner() {
        return winner;
    }

    // Méthodes

    /**
     * Démarrer le tour de table
     */
    public void initTurn() {
        System.out.println("Début du tour");
        if (round.isFirstTurn()){
            System.out.println("<Tour 1> Premier joueur a le 7 de carreau");
            firstPlayer = round.getTrumpPlayer();
        }
        else{
            System.out.println("<Tour "+ round.getTurns().size() +"> Premier joueur est le vainqueur du tour d'avant");
            firstPlayer = lastTurn.getWinner();
        }


        System.out.println("Demande la carte à " + firstPlayer.getUsername());
        firstPlayer.sendRequest(new Request(ServerAction.ASK_CARD));

        // TODO : Renvoyez une erreur au client ?
    }

    /**
     * @return si le tour est en cours de jeu ou non
     */
    public boolean isPlayed() {
        return isPlayed;
    }

    /**
     * @return le nombre de points des cartes jouées dans le tour de table
     */
    private int getTotalPoints() {
        int points = 0;

        for (Card card : cards)
            if (card.getCardColor() == round.getTrumpColor())
                points += card.getCardType().getValueOfTrump();
            else
                points += card.getCardType().getValue();

        return points;
    }

    /**
     * Méthode appelée pour continuer le tour de table lorsqu'une carte est jouée par un joueur
     */
    public void pursueTurn() {

        // Si le tour est fini car chaque joueur à poser une carte
        if (cards.size() == Game.NB_PLAYERS) {
            System.out.println("Le tour est fini.");
            defineWinner();
            System.out.println("Définition du gagnant : " + winner.getUsername());
            System.out.println("Points : " + getTotalPoints());
            this.winner.getTeam().addPoints(getTotalPoints());
            // Suppression des cartes
            // TODO refactor après serialization
            round.getGame().sendToAllPlayers(new Request(ServerAction.SEND_POINTS_TEAM1, round.getGame().getTeams().get(0).getPoints()));
            round.getGame().sendToAllPlayers(new Request(ServerAction.SEND_POINTS_TEAM2, round.getGame().getTeams().get(1).getPoints()));
            round.getGame().sendToAllPlayers(new Request(ServerAction.SEND_RESET_CARDS));
            newTurn();
        } else {

            Player nextPlayer = round.getTable().getTrumpPlayer(firstPlayer, cards.size());
            System.out.println("Prochain joueur : " + nextPlayer.getUsername());
            round.getGame().sendToAllPlayers(new Request(ServerAction.SEND_CURRENT_PLAYER, nextPlayer.getUsername()));
            nextPlayer.sendRequest(new Request(ServerAction.ASK_CARD));
        }
    }

    /**
     * Démarer un nouveau tour de table
     */
    private void newTurn() {
        System.out.println("Nouveau tour dans le round id#"+round.getId());
        // TODO : voir si on peut démarrer un nouveau round dans game plutôt
        isPlayed = false;
        if (round.playerCardsEmpty())
            round.getGame().newRound();
        else {
            Turn turn = new Turn(round, this);
            round.addTurn(turn);

            turn.initTurn();
        }
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    /**
     * Jouer une carte
     *
     * @param card carte jouée
     */
    public void playCard(Card card) {
        cards.add(card);
        Card cardPlayed = null;
        try {
            cardPlayed = (Card) card.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        // TODO : décommenter si sérialization
        cardPlayed.setPlayer(null);
        round.getGame().sendToAllPlayers(new Request(ServerAction.SEND_CARD_PLAYED, cardPlayed));
    }

    /**
     * Définir le joueur qui a posé la meilleure carte
     */
    private void defineWinner() {
        Card winningCard = null;
        for (Card card : cards) {
            if (winningCard == null)
                winningCard = card;
            else if (card.getCardColor() != round.getTrumpColor()) {
                if (winningCard.getCardColor() != round.getTrumpColor() &&
                        card.getCardType().getOrder() > winningCard.getCardType().getOrder()) {
                    winningCard = card;
                }
            } else {
                if (winningCard.getCardColor() != round.getTrumpColor()) {
                    if (card.getCardType().getOrderOfTrump() > winningCard.getCardType().getOrder())
                        winningCard = card;
                } else {
                    if (card.getCardType().getOrderOfTrump() > winningCard.getCardType().getOrder())
                        winningCard = card;
                }
            }
        }

        if (winningCard != null)
            this.winner = winningCard.getPlayer();
    }
}

