/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Request.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une requête du serveur à la GUI transitant par le réseau
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.network.objects;

import java.io.Serializable;

public class Request implements Serializable {

    // Attributs
    private ServerAction action;
    private Object object;

    /**
     * Instancier une nouvelle requête à la GUI
     *
     * @param action de la requête
     */
    public Request(ServerAction action) {
        this(action, null);
    }

    /**
     * Instancier une nouvelle requête à la GUI
     *
     * @param action de la requête
     * @param object envoyé à la GUI pour la requête
     */
    public Request(ServerAction action, Object object) {
        this.action = action;
        this.object = object;
    }

    // Getters

    /**
     * @return l'action de la requête
     */
    public ServerAction getAction() {
        return action;
    }

    /**
     * @return l'objet de la requête
     */
    public Object getObject() {
        return object;
    }
}
