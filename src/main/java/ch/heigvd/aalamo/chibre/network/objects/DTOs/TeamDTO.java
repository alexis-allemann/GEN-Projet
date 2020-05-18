/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     TeamDTO.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Objet de transfer de données pour une équipe
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.network.objects.DTOs;

import java.util.List;

public class TeamDTO implements DTO {
    // Attributs
    private int points;
    private List<PlayerDTO> players;
    private int id;

    /**
     * Instancier un DTO pour une équipe
     *
     * @param id      identifiant de l'équipe
     * @param points  le nombre de points actuels
     * @param players les joueurs faisant partie de l'équipe
     */
    public TeamDTO(int id, int points, List<PlayerDTO> players) {
        this.points = points;
        this.players = players;
        this.id = id;
    }

    // Getters

    /**
     * @return le nombre de points actuels de l'équipe
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return liste des joueurs de l'équipe
     */
    public List<PlayerDTO> getPlayers() {
        return players;
    }

    /**
     * @return l'identifiant de l'équipe
     */
    public int getId() {
        return id;
    }
}
