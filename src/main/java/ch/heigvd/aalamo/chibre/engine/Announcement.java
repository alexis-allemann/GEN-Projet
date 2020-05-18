/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Announcement.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une annonce
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import ch.heigvd.aalamo.chibre.network.objects.DTOs.AnnouncementDTO;

public class Announcement {
    // Attributs
    private Player player;
    private BonusType bonusType;
    private Card bestCard;
    private static int count = 1;
    private int id;

    /**
     * Instanciation d'une annonce
     *
     * @param player    Joueur qui annonce
     * @param bonusType Type de l'annonce
     * @param bestCard  Meilleure carte de l'annonce
     */
    public Announcement(Player player, BonusType bonusType, Card bestCard) {
        this.player = player;
        this.bonusType = bonusType;
        this.bestCard = bestCard;
        this.id = count++;
    }

    public int getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public BonusType getBonusType() {
        return bonusType;
    }

    public Card getBestCard() {
        return bestCard;
    }

    public AnnouncementDTO serialize() {
        return new AnnouncementDTO(id, player.serialize(), bonusType, bestCard.serialize());
    }
}
