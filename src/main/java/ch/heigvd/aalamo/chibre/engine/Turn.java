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

import java.util.*;

public class Turn {
    // Attributs
    private final List<Card> cards = new ArrayList<>(Game.NB_PLAYERS);
    private final Round round;
    private boolean isPlayed;
    private Player firstPlayer;
    private Player winner;
    private final Turn lastTurn;
    private Timer timer;
    private boolean playMatch = true;

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
        if (round.isFirstTurn()) {
            firstPlayer = round.getTrumpPlayer();
            System.out.print("<Tour 1> Premier joueur a le 7 de carreau");
        } else {
            System.out.print("<Tour " + round.getTurns().size() + "> Premier joueur est le vainqueur du tour d'avant");
            firstPlayer = lastTurn.getWinner();
        }
        System.out.println(" : " + firstPlayer.getUsername());

        round.getGame().sendToAllPlayers(new Request(ServerAction.SEND_CURRENT_PLAYER, firstPlayer.serialize()));

        System.out.println("Demande la carte à " + firstPlayer.getUsername());

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
        System.out.println("Points : " + points);
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
            winner.getTeam().addPoints(getTotalPoints());

            // Ajout de points de fin de round
            if (round.getTurns().size() == Game.NB_CARDS_PLAYER) {
                winner.getTeam().addPoints(5);
                if (playMatch)
                    winner.getTeam().addPoints(100);
            }

            if(winner.getTeam() == round.getGame().getTeams().get(0))
                round.addPointsTeam1(getTotalPoints());
            else
                round.addPointsTeam2(getTotalPoints());

            round.sendPoints();

            // Envoi de l'équipe qui a remporté la plie
            round.getGame().sendToAllPlayers(new Request(ServerAction.SEND_WINNING_PLAYER, winner.serialize()));

            // Attente d'un délais avant de lancer le prochain turn/round
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    round.getGame().sendToAllPlayers(new Request(ServerAction.SEND_RESET_CARDS));
                    newTurn();
                }
            }, 5000);
        } else {
            Player nextPlayer = round.getTable().getTrumpPlayer(firstPlayer, cards.size());
            System.out.println("Prochain joueur : " + nextPlayer.getUsername());
            round.getGame().sendToAllPlayers(new Request(ServerAction.SEND_CURRENT_PLAYER, nextPlayer.serialize()));
        }
    }

    /**
     * Démarrer un nouveau tour de table
     */
    private void newTurn() {
        isPlayed = false;
        if (round.getTurns().size() == Game.NB_CARDS_PLAYER) {
            round.getGame().sendToAllPlayers(new Request(ServerAction.END_ROUND));
            round.givePointsForAnnouncements();
            round.getGame().newRound();
        } else {
            System.out.println("Nouveau tour dans le round id#" + round.getId());
            Turn turn = new Turn(round, this);
            round.addTurn(turn);
            turn.initTurn();
        }
    }

    /**
     * Jouer une carte
     *
     * @param card carte jouée
     */
    public void playCard(Card card) {
        cards.add(card);

        System.out.print("<" + card.getCardColor().toString() + ">");
        System.out.print("<" + card.getCardType().toString() + ">");
        System.out.println(" joué par " + card.getPlayer().getUsername());

        round.getGame().sendToAllPlayers(new Request(ServerAction.SEND_CARD_PLAYED, card.serialize()));
    }

    /**
     * Définir le joueur qui a posé la meilleure carte
     */
    private void defineWinner() {
        Card winningCard = cards.get(0);

        for (Card card : cards) {
            if (
                    (card.getCardColor() == round.getTrumpColor() &&
                            winningCard.getCardColor() != round.getTrumpColor())
                            ||
                            (card.getCardColor() == round.getTrumpColor() &&
                                    card.getCardType().getOrderOfTrump() > winningCard.getCardType().getOrderOfTrump())
                            ||
                            (card.getCardColor() != round.getTrumpColor() &&
                                    card.getCardColor() == winningCard.getCardColor() &&
                                    card.getCardType().getOrder() > winningCard.getCardType().getOrder())
            )
                winningCard = card;
        }

        winner = winningCard.getPlayer();

        // Savoir si le match peut toujours être joué (remporter toutes les cartes du round)
        if (winner.getTeam() != firstPlayer.getTeam())
            playMatch = false;

        System.out.print("Le gagnant du tour est : " + winner.getUsername());
        System.out.println(" avec la carte <" + winningCard.getCardColor().toString() + "><" + winningCard.getCardType().toString() + ">");
    }
}

