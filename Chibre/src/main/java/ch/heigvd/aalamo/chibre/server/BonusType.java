/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     BonusType.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant les types d'annonces
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.server;

public enum BonusType {
    SCHTOCKR(20),
    THREE_CARDS(20),
    FOUR_CARDS(50),
    SQUARE_CARDS(100),
    SUITE(100),
    SQUARE_NINE(150),
    SQUARE_JACKS(200);

    private int points;

    BonusType(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
