/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     PlayerDTO.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Objet de transfer de données pour un joueur
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.network.objects.DTOs;

import java.util.List;
import java.util.Objects;

public class PlayerDTO implements DTO {
    // Attributs
    private String username;
    private List<CardDTO> cards;

    /**
     * Instanciation d'un DTO pour un joueur
     *
     * @param username le nom d'utilisateur
     * @param cards    ses cartes
     */
    public PlayerDTO(String username, List<CardDTO> cards) {
        this.username = username;
        this.cards = cards;
    }

    // Getters

    /**
     * @return le nom d'utilisateur
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return la liste de ses cartes
     */
    public List<CardDTO> getCards() {
        return cards;
    }

    // Méthodes

    /**
     * Test d'égalité entre deux DTO de joueurs
     *
     * @param o l'objet à comparer
     * @return si l'objet donnée est égal au joueur
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDTO playerDTO = (PlayerDTO) o;
        return Objects.equals(username, playerDTO.username);
    }

    /**
     * Redéfinition du code de hashage pour l'égalité
     *
     * @return le hashcode d'un joueur
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
