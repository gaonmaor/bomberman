package bomberman.ui;

import bomberman.utils.Utils;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

public class HostGameDialog extends JDialog {
    private final JFrame frame;
    private JPanel hostGamePanel;
    private JTextField portTextField;
    private JComboBox worldComboBox;
    private JButton cancelButton;
    private JButton startButton;
    private JComboBox mapComboBox;
    private int selectedIndex;
    private java.util.List<JButton> buttons = Arrays.asList(
            startButton,
            cancelButton
    );

    public static void showDialog() {
        JFrame frame = new JFrame("HostGameDialog");
        frame.setContentPane(new HostGameDialog(frame).hostGamePanel);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public HostGameDialog(JFrame frame) {
        this.frame = frame;
        setContentPane(hostGamePanel);
        setModal(true);
        getRootPane().setDefaultButton(startButton);
        startButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        hostGamePanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        mapComboBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        worldComboBox.requestFocus();
                        break;
                }
            }
        });
        worldComboBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        portTextField.requestFocus();
                        portTextField.selectAll();
                        break;
                }
            }
        });
        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        selectedIndex = selectedIndex > 0?selectedIndex - 1: 0;
                        buttons.get(selectedIndex).requestFocus();
                        break;
                    case KeyEvent.VK_RIGHT:
                        selectedIndex = selectedIndex < buttons.size() - 1?selectedIndex + 1: buttons.size() - 1;
                        buttons.get(selectedIndex).requestFocus();
                        break;
                    case KeyEvent.VK_UP:
                        int selectedIndex = mapComboBox.getSelectedIndex() - 1;
                        selectedIndex = selectedIndex < 0 ? 0 : selectedIndex;
                        mapComboBox.setSelectedIndex(selectedIndex);
                        break;
                    case KeyEvent.VK_DOWN:
                        selectedIndex = mapComboBox.getSelectedIndex() + 1;
                        selectedIndex = selectedIndex >= mapComboBox.getItemCount() ? mapComboBox.getItemCount() - 1 : selectedIndex;
                        mapComboBox.setSelectedIndex(selectedIndex);
                        break;
                    case KeyEvent.VK_ENTER:
                        buttons.get(HostGameDialog.this.selectedIndex).doClick();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        frame.dispose();
                        break;
                    case KeyEvent.VK_N:
                        if (mapComboBox.hasFocus()) {
                            worldComboBox.requestFocus();
                        } else if (worldComboBox.hasFocus()) {
                            portTextField.requestFocus();
                        } else {
                            mapComboBox.requestFocus();
                        }
                        break;
                }
            }
        };
        buttons.forEach(button -> button.addKeyListener(keyListener));
        frame.addKeyListener(keyListener);
        mapComboBox.addKeyListener(keyListener);
        worldComboBox.addKeyListener(keyListener);
        portTextField.addKeyListener(keyListener);
        Utils.getInstance().getListMap("maps/").forEach(mapComboBox::addItem);
        Utils.getInstance().getListMap("worlds/images/").forEach(worldComboBox::addItem);
        frame.setFocusable(true);
    }

    private void onOK() {
        frame.dispose();
    }

    private void onCancel() {
        frame.dispose();
    }
}