/* ---------------------------
Projet de Génie Logiciel (GEN) - HEIG-VD
Fichier :     GuiAssets.java
Auteur(s) :   Alexis Allemann, Alexandre Mottier
Date :        01.04.2020 - 11.06.2020
But : Classe permettant de lier une image à une vue
Compilateur : javac 11.0.4
--------------------------- */
package ch.heigvd.aalamo.chibre.assets;

import ch.heigvd.aalamo.chibre.CardColor;
import ch.heigvd.aalamo.chibre.CardType;
import ch.heigvd.aalamo.chibre.view.gui.GUIView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GuiAssets {
    /**
     * Charger l'image sur une vue donnée
     *
     * @param view la vue
     */
    public static void loadAssets(GUIView view) {
        try {
            for (CardType type : CardType.values())
                for (CardColor color : CardColor.values())
                    view.registerCardResource(type, color, GUIView.createResource(assetsImage(color + "-" + type + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lecture de l'image
     *
     * @param imageName nom de l'image sur le disque
     * @return l'image
     * @throws IOException si le fichier n'a pas été trouvé
     */
    private static BufferedImage assetsImage(String imageName) throws IOException {
        return ImageIO.read(GuiAssets.class.getResource("images/" + imageName));
    }
}
