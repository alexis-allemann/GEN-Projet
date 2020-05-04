/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Table.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant la table d'une partie
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Table {

    /**
     * Classe représentant une paire
     */
    class Pair {

        private Player player;
        private TablePosition tablePosition;

        /**
         * Instancier une paire
         *
         * @param player        joueur
         * @param tablePosition position à la table
         */
        public Pair(Player player, TablePosition tablePosition){
            this.player = player;
            this.tablePosition = tablePosition;
        }

        /**
         * @return le joueur
         */
        public Player getPlayer(){
            return player;
        }

        /**
         * @return la position à la table
         */
        public TablePosition getTablePosition(){
            return tablePosition;
        }

        public void setPlayer(Player player){
            this.player = player;
        }

        public void setTablePosition(TablePosition tablePosition){
            this.tablePosition = tablePosition;
        }
    }

    // Atribut
    private List<Pair> table = new ArrayList<>();

    /**
     * Instancier une parte
     *
     * @param game la partie jouée
     */
    public Table(Game game){
        table.add(new Pair(game.getTeams().get(0).getPlayers().get(0), TablePosition.TOP));
        table.add(new Pair(game.getTeams().get(0).getPlayers().get(1), TablePosition.BOTTOM));
        table.add(new Pair(game.getTeams().get(1).getPlayers().get(0), TablePosition.RIGHT));
        table.add(new Pair(game.getTeams().get(1).getPlayers().get(1), TablePosition.LEFT));
    }

    /**
     * Permet d'obtenir le prochain joueur qui fera atout en fonction
     * du round et de la position du premier à avoir donné atout
     * @param roundId Numéro du round
     * @param startPosition Position du joueur ayant donné atout au départ
     * @return Le joueur qui doit donner atout
     */
    public Player nextTrumpPlayer(int roundId, TablePosition startPosition) {
        return getPlayer(startPosition, (roundId % TablePosition.values().length) - 1);
    }


    /**
     * Permet d'obtenir la position d'un joueur
     * @param player Joueur dont on veut la position
     * @return La position du joueur sur la table
     */
    public TablePosition getPositionByPlayer(Player player){
        for(Pair pair : table){
            if(pair.getPlayer() == player)
                return pair.getTablePosition();
        }
        return null;
    }

    private Player getPlayer(TablePosition startPosition, int nb){
        for(TablePosition position : TablePosition.values()) {
            if (position.getIndex() == (startPosition.getIndex() + nb))
                return getPlayerByPosition(position);
        }
        return null;
    }

    private Player getPlayerByPosition(TablePosition position){
        for(Pair pair : table){
            if(pair.getTablePosition() == position)
                return pair.getPlayer();
        }
        return null;
    }

    public Player getPlayer(Player firstPlayer, int nb) {
        return getPlayer(getPositionByPlayer(firstPlayer), nb);
    }

}
