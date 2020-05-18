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
    TOP(0, 0),
    RIGHT(1, 1),
    BOTTOM(2, 0),
    LEFT(3, 1);

    // Attributs
    private final int index;
    private final int teamIndex;

    /**
     * Instancier une position à la table
     *
     * @param team équipe
     */
    TablePosition(int index, int team) {
        this.index = index;
        this.teamIndex = team;
    }

    // Getters

    /**
     * Obtenir l'index de la position
     *
     * @return l'index de la position
     */
    public int getIndex() {
        return index;
    }
}
