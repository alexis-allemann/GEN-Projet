/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Team.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une équipe
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import java.io.Serializable;
import java.util.List;

public class Team {
    // Attributs
    private List<Player> players;
    private int points;

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

    // Getters

    /**
     * @return la liste des joueurs qui font partie de l'équipe
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return le nombre de points de l'équipe
     */
    public int getPoints() {
        return points;
    }

    // Méthodes

    /**
     * Ajouter des points l'équipe
     *
     * @param points le nombre de points à ajouter
     */
    public void addPoints(int points) {
        this.points += points;
    }

}
