package bomberman.ui;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

public class JoinGameDialog extends JDialog {
    private final JFrame frame;
    private JPanel joinGamePanel;
    private JTextField ipTextField;
    private JButton cancelButton;
    private JButton joinButton;
    private JTextField portTextField;
    private int selectedIndex;
    private java.util.List<JButton> buttons = Arrays.asList(
            joinButton,
            cancelButton
    );

    public JoinGameDialog(JFrame frame) {
        this.frame = frame;
        setContentPane(joinGamePanel);
        setModal(true);
        getRootPane().setDefaultButton(joinButton);
        joinButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        joinGamePanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ipTextField.addActionListener(e -> {
            portTextField.requestFocus();
            portTextField.selectAll();
        });
        portTextField.addActionListener(e -> buttons.get(JoinGameDialog.this.selectedIndex).doClick());

        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if(!portTextField.hasFocus()) {
                            portTextField.requestFocus();
                        } else {
                            ipTextField.requestFocus();
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if(!ipTextField.hasFocus()) {
                            ipTextField.requestFocus();
                        } else {
                            portTextField.requestFocus();
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        selectedIndex = selectedIndex > 0? selectedIndex - 1: 0;
                        buttons.get(selectedIndex).requestFocus();
                        break;
                    case KeyEvent.VK_RIGHT:
                        selectedIndex = selectedIndex < buttons.size() - 1? selectedIndex + 1: buttons.size() - 1;
                        buttons.get(selectedIndex).requestFocus();
                        break;
                    case KeyEvent.VK_ENTER:
                        buttons.get(JoinGameDialog.this.selectedIndex).doClick();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        frame.dispose();
                        break;
                }
            }
        };
        ipTextField.addKeyListener(keyListener);
        portTextField.addKeyListener(keyListener);
        buttons.forEach(button -> button.addKeyListener(keyListener));
        frame.addKeyListener(keyListener);
    }

    private void onOK() {
        frame.dispose();
    }

    private void onCancel() {
        frame.dispose();
    }

    public static void showDialog() {
        JFrame frame = new JFrame("JoinGameDialog");
        frame.setContentPane(new JoinGameDialog(frame).joinGamePanel);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
