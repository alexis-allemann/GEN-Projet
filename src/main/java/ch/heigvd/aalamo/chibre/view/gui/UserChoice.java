/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     UserChoice.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un choix utilisateur sur la GUI
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.view.gui;

/**
 * Classe représentant un choix utilisateur
 */
public class UserChoice {
    // Attributs
    private final String name;
    private final Object value;

    /**
     * Instancier une choix utilisateur
     *
     * @param name  le nom affichable du choix
     * @param value la valeur (l'objet)
     */
    public UserChoice(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    // Getters

    /**
     * @return l'objet représenté par la valeur
     */
    public Object getValue() {
        return value;
    }

    // Méthodes

    /**
     * Affichage du choix
     */
    @Override
    public String toString() {
        return name;
    }
}