/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Round.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un tour d'atout dans une partie
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.backend;

import java.util.ArrayList;
import java.util.List;

public class Round {
    // Attributs
    private CardColor trumpColor;
    private List<Turn> turns = new ArrayList<>(Game.NB_CARDS_PLAYER);

    /**
     * Instancier un tour de jeu
     *
     * @param trumpColor l'atout
     */
    public Round(CardColor trumpColor) {
        this.trumpColor = trumpColor;
        //TODO :
        // 1. Récupèrer les joueurs
        // 2. Distribuer les cartes à chaque joueur
        // 3.
    }
}
