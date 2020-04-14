/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Game.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant une partie de Chibre
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {
    // Attributs
    public static final int NB_PLAYERS = 4;
    private int id;
    private List<ChibreHandler> players = new ArrayList<>(NB_PLAYERS);

    public Game(int id) {
        this.id = id;
    }

    public boolean addPlayer(ChibreHandler player) {
        // Validation du joueur reçu
        if (player == null)
            throw new IllegalArgumentException("Joueur nul");

        if (players.size() < NB_PLAYERS) {
            players.add(player);
            return true;
        }

        return false;
    }

    public void startGame() {
        if (players.size() != NB_PLAYERS)
            throw new RuntimeException("La partie ne contient pas 4 joueurs");

        try{
            for (ChibreHandler player : players)
                player.send(new CardJPanel("img/COEUR-VALET.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
