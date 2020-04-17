/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Team.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une équipe
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import java.util.List;

public class Team {
    // Attributs
    private List<Player> players;

    /**
     * Instanciation d'une équipe
     *
     * @param players joueurs de l'équipe
     */
    public Team(List<Player> players) {
        if (players.size() != Game.NB_PLAYERS_TEAMS)
            throw new IllegalArgumentException("La liste de joueur ne contient pas deux joueurs pour créer l'équipe.");
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getMaxIDPlayer() {
        return Math.max(this.players.get(0).getId(), this.players.get(1).getId());
    }

    public int getMinIDPlayer() {
        return Math.min(this.players.get(0).getId(), this.players.get(1).getId());
    }
}
