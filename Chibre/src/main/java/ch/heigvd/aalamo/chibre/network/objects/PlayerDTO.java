package ch.heigvd.aalamo.chibre.network.objects;

import java.util.List;

public class PlayerDTO implements DTO{
    private int id;
    private String name;
    private List<CardDTO> cards;



    public PlayerDTO(int id, String name, List<CardDTO> cards) {
        this.id = id;
        this.name = name;
        this.cards = cards;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CardDTO> getCards() {
        return cards;
    }
}
