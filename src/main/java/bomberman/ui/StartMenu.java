package bomberman.ui;

import bomberman.utils.Utils;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import static javax.swing.WindowConstants.*;

public class StartMenu {
    private JPanel mainPanel;
    private JButton joinGameButton;
    private JButton hostGameButton;
    private JButton editPlayerButton;
    private JButton helpButton;
    private JButton aboutButton;
    private JButton exitButton;
    private JPanel buttonPanel;
    private int selectedIndex;
    private java.util.List<JButton> buttons = Arrays.asList(
            joinGameButton,
            hostGameButton,
            editPlayerButton,
            helpButton,
            aboutButton,
            exitButton
    );

    public static void main(String[] args) {
        Utils.getInstance().initAllImages();
        JFrame frame = new JFrame("StartMenu");
        frame.setContentPane(new StartMenu().mainPanel);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public StartMenu() {
        selectedIndex = 0;
        exitButton.addActionListener(e -> System.exit(0));
        joinGameButton.addActionListener(e -> JoinGameDialog.showDialog());
        hostGameButton.addActionListener(e -> HostGameDialog.showDialog());
        editPlayerButton.addActionListener(e -> PlayerDialog.showDialog());
        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        selectedIndex = selectedIndex > 0? selectedIndex - 1: 0;
                        buttons.get(selectedIndex).requestFocus();
                        break;
                    case KeyEvent.VK_DOWN:
                        selectedIndex = selectedIndex < buttons.size() - 1? selectedIndex + 1: buttons.size() - 1;
                        buttons.get(selectedIndex).requestFocus();
                        break;
                    case KeyEvent.VK_ENTER:
                        buttons.get(selectedIndex).doClick();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                }
            }
        };
        buttons.forEach(button -> button.addKeyListener(keyListener));
        mainPanel.addKeyListener(keyListener);
        mainPanel.setFocusable(true);
    }
}