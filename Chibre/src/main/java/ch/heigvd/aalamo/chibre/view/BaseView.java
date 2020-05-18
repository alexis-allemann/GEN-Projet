/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     BaseView.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe abstraite permettant de définir la vue et ses ressources
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.view;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;
import ch.heigvd.aalamo.chibre.ChibreController;
import ch.heigvd.aalamo.chibre.ChibreView;

import java.util.HashMap;

public abstract class BaseView<Resource> implements ChibreView {
    // Attributs
    private final HashMap<CardType, HashMap<CardColor, DrawableRessource<Resource>>> resources = new HashMap<>();
    protected final ChibreController controller;

    /**
     * Instanciation d'une vue
     *
     * @param controller relié à la vue
     */
    public BaseView(ChibreController controller) {
        this.controller = controller;
    }

    // Méthodes

    /**
     * Enregistrement des ressources
     *
     * @param type     type de la carte
     * @param color    couleur de la carte
     * @param resource la ressource liée à la carte
     */
    public final void registerCardResource(CardType type, CardColor color, DrawableRessource<Resource> resource) {
        HashMap<CardColor, DrawableRessource<Resource>> pieceResources = resources.get(type);
        if (pieceResources == null) {
            pieceResources = new HashMap<>();
        }
        pieceResources.put(color, resource);
        resources.put(type, pieceResources);
    }

    /**
     * Chargement des ressources
     *
     * @param type  type de la carte
     * @param color couleur de la carte
     * @param def   la ressource liée à la carte
     * @return la ressource une fois chargée
     */
    protected final Resource loadResourceFor(CardType type, CardColor color, Resource def) {
        Resource icon = def;
        if (type != null && color != null) {
            HashMap<CardColor, DrawableRessource<Resource>> pieceResources = resources.get(type);
            if (pieceResources != null) {
                DrawableRessource<Resource> resource = pieceResources.get(color);
                if (resource != null) {
                    icon = resource.getResource();
                }
            }
        }
        return icon;
    }
}