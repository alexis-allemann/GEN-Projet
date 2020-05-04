/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Turn.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un tour de jeu
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.network.objects.AskCardRequest;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    // Attributs
    private List<Card> cards = new ArrayList<>(Game.NB_PLAYERS);
    private final Round round;
    private boolean isPlayed;
    private Player firstPlayer;
    private Player winner;
    private Turn lastTurn;

    public Turn(Round round, Turn lastTurn) {
        this.round = round;
        this.lastTurn = lastTurn;
        this.isPlayed = true;
        //TODO :
        // 1. Récupérer le joueur qui doit jouer
        // 2. Lui demander de jouer une carte
        // 3. Faire ça pour les 3 autres joueur
        // 4. Regarder qui a remporter la mise
        // 5. Calculer les points
        // 6. Attribuer les points à la bonne équipe
        // 7. Enlever les cartes de la main des joueurs
        // 8. Recréer un tour si ils restent des cartes, sinon créer un nouveau round


    }

    public void initTurn() {
        if (round.isFirstRound())
            firstPlayer = round.getTrumpPlayer();
        else if (lastTurn != null)
            firstPlayer = lastTurn.getWinner();

        firstPlayer.sendState(new AskCardRequest());

        // TODO : Renvoyez une erreur au client ?
    }

    public void pursueTurn() {

        //TODO :
        // 1. Si la mise est pleine on calcule les points et
        // on les attribue à l'équipe du joueur qui remporte la main
        // 1.1. Si les joueurs n'ont plus du cartes, on crée un nouveau tour
        // 2. Sinon on demande au joueur suivant de jouer

        if (cards.size() == Game.NB_PLAYERS) {
            // TODO
            //  1. Calcul des points
            //  2. Définir le joueur gagant
            //  3. Ajout des points à l'équipe du joueur gagnant
            //  4. Suppression des cartes jouées aux joueurs
            defineWinner();
            int points = getTotalPoints();
            newTurn();
        } else {
            Player nextPlayer = round.getTable().getPlayer(firstPlayer, cards.size() - 1);
            nextPlayer.sendState(new AskCardRequest());
        }
    }

    // TODO: A voir si c'est propre
    private void newTurn() {
        if (round.playerCardsEmpty())
            round.getGame().newRound();
        else {
            Turn turn = new Turn(round, this);
            round.addTurn(turn);
            turn.initTurn();
        }
    }

    public Player getWinner() {
        return winner;
    }


    public void playCard(Card card) {
        cards.add(card);
    }

    public boolean isPlayed() {
        return isPlayed;
    }

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

    private int getTotalPoints() {
        int points = 0;

        for (Card card : cards)
            if (card.getCardColor() == round.getTrumpColor())
                points += card.getCardType().getValueOfTrump();
            else
                points += card.getCardType().getValue();

        return points;
    }
}

