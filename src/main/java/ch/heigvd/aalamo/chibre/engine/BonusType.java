/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     BonusType.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant les types d'annonces
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

public enum BonusType {
    SCHTOCKR(20, "Schötckr"),
    THREE_CARDS(20, "3 cartes"),
    FOUR_CARDS(50, "Cinquante"),
    SQUARE_CARDS(100, "Cent"),
    SUITE(100, "Cent"),
    SQUARE_NINE(150, "Carré de neufs"),
    SQUARE_JACKS(200, "Carré de valets");

    // Attributs
    private int points;
    private String name;

    /**
     * Instanciation d'un bonus
     *
     * @param points nombre de points du bonus
     */
    BonusType(int points, String name) {
        this.points = points;
        this.name = name;
    }

    /**
     * @return les points du bonus
     */
    public int getPoints() {
        return points;
    }

    /**
     * Affichage du bonus
     *
     * @return l'affichage du bonus
     */
    @Override
    public String toString() {
        return name + " : " + getPoints() + " points";
    }
}
