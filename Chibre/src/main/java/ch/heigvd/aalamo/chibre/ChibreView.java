/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     ChibreView.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Interface de la vue
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre;

public interface ChibreView {

    /**
     * Action à effectuer lorsque la fenêtre est fermée
     */
    void quit();

    /**
     * Afficher une carte sur la vue
     *
     * @param type     type de la carte (as, roi, dame, valet, dix, neuf, huit, sept, six)
     * @param color    couleur de la carte (carreau, coeur, pique, trèfle)
     * @param position la posistion dans le deck de l'utilisateur (9 cartes => 0:8)
     */
    void addCard(CardType type, CardColor color, int position);

    /**
     * Poser une question à l'utilisateur dans une fenêtre
     *
     * @param title         titre de la fenêtre
     * @param question      la question
     * @param possibilities les options (liste déroulante)
     * @param <T>           le type des choix
     * @return l'option choisie au type défini
     */
    <T extends UserChoice> T askUser(String title, String question, T... possibilities);


    /**
     * Interface représentant un choix utilisateur
     */
    interface UserChoice {
        /**
         * @return la valeur du choix au format texuel
         */
        String textValue();
    }
}
