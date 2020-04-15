/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Announcement.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une annonce
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.backend;

public class Announcement {
    // Attributs
    private Player player;
    BonusType bonusType;
    Round round;
    Team team;

    public Announcement(Player player, BonusType bonusType, Round round, Team team) {
        this.player = player;
        this.bonusType = bonusType;
        this.round = round;
        this.team = team;
    }
}
