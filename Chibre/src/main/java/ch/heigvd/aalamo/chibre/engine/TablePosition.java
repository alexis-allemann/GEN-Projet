/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     TablePosition.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Enumération représentant les positions à une table du jeu
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

public enum TablePosition {
    TOP(0),
    RIGHT(1),
    BOTTOM(0),
    LEFT(1);

    // Attributs
    private int team;

    /**
     * Instancier une position à la table
     *
     * @param team équipe
     */
    TablePosition(int team) {
        this.team = team;
    }

    /**
     * Obtenir l'équipe à la place
     *
     * @return l'équipe
     */
    public int getTeam() {
        return team;
    }
}
