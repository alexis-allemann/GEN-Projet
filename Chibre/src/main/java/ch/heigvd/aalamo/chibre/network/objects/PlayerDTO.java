package ch.heigvd.aalamo.chibre.network.objects;

import java.util.List;

public class PlayerDTO implements DTO {
    private String username;
    private List<CardDTO> cards;

    public PlayerDTO(String username, List<CardDTO> cards) {
        this.username = username;
        this.cards = cards;
    }

    public String getUsername() {
        return username;
    }

    public List<CardDTO> getCards() {
        return cards;
    }
}
