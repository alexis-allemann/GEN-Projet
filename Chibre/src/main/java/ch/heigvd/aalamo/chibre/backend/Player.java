/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Player.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un joueur
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player {
    // Attributs
    private List<Card> cards = new ArrayList<>(Game.NB_CARDS_PLAYER);
    private Handler handler;

    public Player(Handler handler) {
        if (handler == null)
            throw new IllegalArgumentException("Handler nul");

        this.handler = handler;
    }

    public void distributeCard(Card card) {
        if (card == null)
            throw new IllegalArgumentException("Carte nulle");
        cards.add(card);
        try{
            handler.sendCard(card);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public Handler getHandler() {
        return handler;
    }
}
