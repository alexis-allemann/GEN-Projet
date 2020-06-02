/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     AuthenticationDTO.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Objet de transfert de données pour l'authetification
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.network.objects.DTOs;

import java.io.Serializable;

public class AuthenticationDTO implements Serializable {
    // Attributs
    private final String userName;
    private final String password;

    /**
     * Instaciation d'un DTO d'authentification
     *
     * @param userName nom d'utilisateur
     * @param password mot de passe
     */
    public AuthenticationDTO(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // Getters

    /**
     * @return le nom de l'utilisateur
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return le mot de passe
     */
    public String getPassword() {
        return password;
    }
}
