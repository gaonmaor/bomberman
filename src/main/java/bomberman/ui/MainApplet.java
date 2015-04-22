package bomberman.ui;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class MainApplet extends JApplet {
    public MainApplet() {
        setName("Bomberman");
        Dimension screen = getToolkit().getScreenSize();
        getContentPane().setLayout(null);
        setSize(screen);
        setLocation(0, 0);
        getContentPane().setBackground(new Color(99, 80, 135));
        Main.initAll(this, getContentPane());
        setVisible(true);
    }

    public void close() {
        try {
            getAppletContext().showDocument(new URL(getCodeBase() + "close.html"));
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void init() {
    }
}