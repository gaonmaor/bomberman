package bomberman.ui;

import bomberman.utils.Utils;

import javax.swing.*;
import java.awt.*;

public class MainWindow  extends JFrame {
    public MainWindow() {
        super("Bomberman");
        Dimension screen = getToolkit().getScreenSize();
        getContentPane().setLayout(null);
        setSize(screen);
        setLocation(0, 0);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(99, 80, 135));
        Utils.getInstance().loadImage("misc/icon.png", "misc/icon.png");
        setIconImage(Utils.getInstance().getImage("misc/icon.png"));
        setVisible(true);
    }

    public static void main(String[] args) {
        //MainWindow mainWindow = new MainWindow();
        //Main.initAll(mainWindow, mainWindow.getContentPane());
        //mainWindow.requestFocusInWindow();
        //mainWindow.repaint();
        //mainWindow.validate();
    }
}