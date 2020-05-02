/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Round.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un tour d'atout dans une partie
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.CardColor;

import java.util.ArrayList;
import java.util.List;

public class Round {
    // Attributs
    private CardColor trumpColor;
    private List<Turn> turns = new ArrayList<>(Game.NB_CARDS_PLAYER);
    private CardCollection cardCollection = new CardCollection();

    /**
     * Instancier un tour de jeu
     */
    public Round(Game game) {
        //TODO :
        // 1. Récupèrer les joueurs
        // 2. Distribuer les cartes à chaque joueur
        // 3. Choisir l'atout (le joueur à la suite et le premier tour celui qui a le 7 de carreau)

        Player trumpPlayer = game.getTrumpPlayer();

        for (Player player : game.getPlayers()) {
            boolean hasDiamondSeven = cardCollection.distributeCards(player, Game.NB_CARDS_PLAYER);
            if(trumpPlayer == null && hasDiamondSeven)
                trumpPlayer = player;
        }
    }
}
