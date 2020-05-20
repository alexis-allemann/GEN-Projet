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

public class Table {

    /**
     * Classe représentant une paire pour lier un position à un joueur
     */
    class Pair {
        // Attributs
        private Player player;
        private TablePosition tablePosition;

        /**
         * Instancier une paire
         *
         * @param player        joueur
         * @param tablePosition position à la table
         */
        public Pair(Player player, TablePosition tablePosition) {
            this.player = player;
            this.tablePosition = tablePosition;
        }

        // Getters

        /**
         * @return le joueur
         */
        public Player getPlayer() {
            return player;
        }

        /**
         * @return la position à la table
         */
        public TablePosition getTablePosition() {
            return tablePosition;
        }

        // Setters

        /**
         * Définir le joueur à la position
         *
         * @param player joueur
         */
        public void setPlayer(Player player) {
            this.player = player;
        }
    }

    // Attributs
    private List<Pair> table = new ArrayList<>();

    /**
     * Instancier une parte
     *
     * @param game la partie jouée
     */
    public Table(Game game) {
        table.add(new Pair(game.getTeams().get(0).getPlayers().get(0), TablePosition.TOP));
        table.add(new Pair(game.getTeams().get(0).getPlayers().get(1), TablePosition.BOTTOM));
        table.add(new Pair(game.getTeams().get(1).getPlayers().get(0), TablePosition.RIGHT));
        table.add(new Pair(game.getTeams().get(1).getPlayers().get(1), TablePosition.LEFT));
    }

    // Getters

    /**
     * Permet d'obtenir la position d'un joueur
     *
     * @param player Joueur dont on veut la position
     * @return La position du joueur sur la table
     */
    public TablePosition getPositionByPlayer(Player player) {
        for (Pair pair : table) {
            if (pair.getPlayer() == player)
                return pair.getTablePosition();
        }
        return null;
    }

    /**
     * Obtenir le joueur selon une position de départ et du numéro du round
     *
     * @param startPosition position de départ
     * @param nb            numéro du round
     * @return le joueur selon les paramètres donnés
     */
    private Player getTrumpPlayer(TablePosition startPosition, int nb) {
        for (TablePosition position : TablePosition.values()) {
            if (position.getIndex() == ((startPosition.getIndex() + nb) % Game.NB_PLAYERS))
                return getPlayerByPosition(position);
        }
        return null;
    }

    /**
     * Obtenir le joueur à une certaine position de la table
     *
     * @param position position du joueur
     * @return le joueur à la position donnée, null si aucun joueur
     */
    private Player getPlayerByPosition(TablePosition position) {
        for (Pair pair : table) {
            if (pair.getTablePosition() == position)
                return pair.getPlayer();
        }
        return null;
    }

    /**
     * Obtenir le joueur qui doit faire atout
     *
     * @param firstPlayer joueur qui a fait atout en premier
     * @param nb          numéro du round
     * @return le joueur qui doit faire atout
     */
    public Player getTrumpPlayer(Player firstPlayer, int nb) {
        return getTrumpPlayer(getPositionByPlayer(firstPlayer), nb);
    }

    public List<Player> getPlayers(){
        List<Player> players = new ArrayList<>(Game.NB_PLAYERS);
        for(Pair pair : table)
            players.add(pair.getPlayer());
        return players;
    }

    // Méthodes

    /**
     * Permet d'obtenir le prochain joueur qui fera atout en fonction
     * du round et de la position du premier à avoir donné atout
     *
     * @param roundId       Numéro du round
     * @param startPosition Position du joueur ayant donné atout au départ
     * @return Le joueur qui doit donner atout
     */
    public Player nextTrumpPlayer(int roundId, TablePosition startPosition) {
        return getTrumpPlayer(startPosition, (roundId % TablePosition.values().length) - 1);
    }
}
