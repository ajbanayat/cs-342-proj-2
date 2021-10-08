import javafx.scene.Node;
import javafx.scene.control.Button;

public class GameButton extends Button {
    private int player;
    public GameButton() {
        super();
        player = 0;
    }

    public GameButton(String text) {
        super(text);
        player = 0;
    }

    public GameButton(String text, Node graphic) {
        super(text, graphic);
        player = 0;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}
