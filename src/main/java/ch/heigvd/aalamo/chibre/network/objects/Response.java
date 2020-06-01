/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Response.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une réponser de la GUI au serveur transitant par le réseau
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.network.objects;

import java.io.Serializable;

public class Response implements Serializable {

    // Attributs
    private final UserAction action;
    private final Object object;

    /**
     * Instanciation d'une réponse au serveur
     *
     * @param action de la réponse
     * @param object envoyé au serveur pour la réponse
     */
    public Response(UserAction action, Object object) {
        this.action = action;
        this.object = object;
    }

    // Getters

    /**
     * @return l'action de la réponse
     */
    public UserAction getAction() {
        return action;
    }

    /**
     * @return l'objet de la réponse
     */
    public Object getObject() {
        return object;
    }
}
