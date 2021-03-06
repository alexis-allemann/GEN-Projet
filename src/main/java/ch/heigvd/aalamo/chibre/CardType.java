/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     CardType.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe représentant un type de cartes
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

public enum CardType {
    ACE(11, 11, 8, 6),
    KING(4, 4, 7, 5),
    QUEEN(3, 3, 6, 4),
    JACK(2, 20, 5, 8),
    TEN(10, 10, 4, 3),
    NINE(0, 14, 3, 7),
    EIGHT(0, 0, 2, 2),
    SEVEN(0, 0, 1, 1),
    SIX(0, 0, 0, 0);

    // Attributs
    private final int value;
    private final int valueOfTrump;
    private final int order;
    private final int orderOfTrump;

    /**
     * Instanciation d'un type de carte
     *
     * @param value        valeur standard de la carte
     * @param valueOfTrump valeur de la carte si c'est atout
     * @param order        ordre standard
     * @param orderOfTrump ordre si c'est atout
     */
    CardType(int value, int valueOfTrump, int order, int orderOfTrump) {
        this.order = order;
        this.orderOfTrump = orderOfTrump;
        this.value = value;
        this.valueOfTrump = valueOfTrump;
    }

    // Getters

    /**
     * @return l'ordre standard
     */
    public int getOrder() {
        return order;
    }

    /**
     * @return la valeur standard
     */
    public int getValue() {
        return value;
    }

    /**
     * @return la valeur si atout
     */
    public int getValueOfTrump() {
        return valueOfTrump;
    }

    /**
     * @return l'ordre si atout
     */
    public int getOrderOfTrump() {
        return orderOfTrump;
    }
}
