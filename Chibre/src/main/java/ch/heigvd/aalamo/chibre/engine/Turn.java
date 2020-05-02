/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Turn.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un tour de jeu
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.CardColor;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    // Attributs
    private List<Card> cards = new ArrayList<>(Game.NB_PLAYERS);
    private Round round;

    public Turn(Round round){
        this.round = round;
    }

    public void run() {
    }
}
