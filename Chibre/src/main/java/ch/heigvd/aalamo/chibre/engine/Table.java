/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     Table.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant la table d'une partie
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.engine;

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

    //Constante
    public static final boolean CLOCKWISE = true;

    // Atribut
    private List<Pair> table;

    /**
     * Instancier une parte
     *
     * @param game la partie jouée
     */
    public Table(Game game){
        // Instanciation du random
        Random random = new Random();


        int teamPosition = random.nextInt(1);
        for (Team team : game.getTeams()) {
            int playerPosition = random.nextInt(team.getMaxIDPlayer() - team.getMinIDPlayer()) +
                    team.getMinIDPlayer();
            if (TablePosition.TOP.getTeam() == teamPosition) {
                if (playerPosition == team.getPlayers().get(0).getId()) {
                    table.add(new Pair(team.getPlayers().get(0), TablePosition.TOP));
                    table.add(new Pair(team.getPlayers().get(1), TablePosition.BOTTOM));
                } else {
                    table.add(new Pair(team.getPlayers().get(1), TablePosition.TOP));
                    table.add(new Pair(team.getPlayers().get(0), TablePosition.BOTTOM));
                }
            } else {
                if (playerPosition == team.getPlayers().get(0).getId()) {
                    table.add(new Pair(team.getPlayers().get(0), TablePosition.RIGHT));
                    table.add(new Pair(team.getPlayers().get(1), TablePosition.LEFT));
                } else {
                    table.add(new Pair(team.getPlayers().get(1), TablePosition.RIGHT));
                    table.add(new Pair(team.getPlayers().get(0), TablePosition.LEFT));
                }
            }
        }
    }

    /**
     * Permet d'obtenir le prochain joueur qui fera atout en fonction
     * du round et de la position du premier à avoir donné atout
     * @param roundId Numéro du round
     * @param startPosition Position du joueur ayant donné atout au départ
     * @return Le joueur qui doit donner atout
     */
    public Player nextTrumpPlayer(int roundId, TablePosition startPosition) {
        // TODO : Pas sûr du calcul xD
        return table.get((roundId % TablePosition.values().length) + startPosition.getIndex()).player;
    }


    /**
     * Permet d'obtenir la position d'un joueur
     * @param player Joueur dont on veut la position
     * @return La position du joueur sur la table
     */
    public TablePosition getPlayerPosition(Player player){
        for(Pair pair : table){
            if(pair.getPlayer() == player)
                return pair.getTablePosition();
        }
        return null;
    }
}
