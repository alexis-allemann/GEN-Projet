/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Team.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une équipe
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.backend;

import java.util.ArrayList;
import java.util.List;

public class Team {
    // Attributs
    private List<Player> players = new ArrayList<>(Game.NB_PLAYERS_TEAMS);

    public Team(List<Player> players) {
        this.players = players;
    }
}
