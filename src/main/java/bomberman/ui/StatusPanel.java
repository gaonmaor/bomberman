package bomberman.ui;

import bomberman.entities.GameBoard;
import bomberman.logic.GameManager;
import bomberman.entities.Player;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {

    public StatusPanel(GameBoard gameBoard) {
        setSize(600, 40);
        setLocation(220, 50);
    }

    public void paint(Graphics g) {
        Player activePlayer = GameManager.getInstance().getActivePlayer();
        String message = "";
        String name = "";
        if (activePlayer != null) {
            name = activePlayer.getName();
            if (activePlayer.getStatus().equals("dead")) {
                message = " You are dead!";
            } else {
                message = " score: " + activePlayer.getScore() + " bombs: " + activePlayer.getBombCount();
            }
        }
        g.setColor(new Color(167, 198, 254));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.setFont(new Font("Ariel", 1, 22));
        g.drawString(" name:", 0, 22);
        g.setFont(new Font("Ariel", 1, 22));
        g.drawString(name + message, 75, 20);
    }
}