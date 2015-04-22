package bomberman.ui;

import bomberman.entities.GameBoard;
import bomberman.entities.Player;
import bomberman.utils.Utils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class PlayerDialog extends JDialog {
    private JTextField nameTextField;
    private JButton cancelButton;
    private JButton setButton;
    private JPanel playerPanel;
    private JComboBox characterComboBox;
    private JLabel playerIcon;
    private JFrame frame;
    private int selectedIndex;
    private java.util.List<JButton> buttons = Arrays.asList(
            setButton,
            cancelButton
            );

    public static void showDialog() {
        JFrame frame = new JFrame("PlayerDialog");
        frame.setContentPane(new PlayerDialog(frame).playerPanel);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public PlayerDialog(JFrame frame) {
        this.frame = frame;
        setContentPane(playerPanel);
        setModal(true);
        getRootPane().setDefaultButton(setButton);
        setButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        playerPanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        nameTextField.addActionListener(e -> buttons.get(PlayerDialog.this.selectedIndex).doClick());
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
                        int selectedIndex = characterComboBox.getSelectedIndex() - 1;
                        selectedIndex = selectedIndex < 0 ? 0 : selectedIndex;
                        characterComboBox.setSelectedIndex(selectedIndex);
                        break;
                    case KeyEvent.VK_DOWN:
                        selectedIndex = characterComboBox.getSelectedIndex() + 1;
                        selectedIndex = selectedIndex >= characterComboBox.getItemCount() ? characterComboBox.getItemCount() - 1 : selectedIndex;
                        characterComboBox.setSelectedIndex(selectedIndex);
                        break;
                    case KeyEvent.VK_ENTER:
                        buttons.get(PlayerDialog.this.selectedIndex).doClick();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        frame.dispose();
                        break;
                    default:
                        nameTextField.requestFocus();
                        nameTextField.selectAll();
                }
            }
        };
        buttons.forEach(button -> button.addKeyListener(keyListener));
        frame.addKeyListener(keyListener);

        Utils.getInstance().getListMap("characters/").forEach(characterComboBox::addItem);
        frame.setFocusable(true);
        if (!GameBoard.getInstance().getPlayers().isEmpty()) {
            Player player = GameBoard.getInstance().getLocalPlayer();
            nameTextField.setText(player.getName());
            characterComboBox.setSelectedItem(player.getPlayerName());
        }
        setSelectedPlayer();
        characterComboBox.addItemListener(e -> setSelectedPlayer());
    }

    private void setSelectedPlayer() {
        if (GameBoard.getInstance().getPlayers().isEmpty()) {
            nameTextField.setText(characterComboBox.getSelectedItem().toString());
        }
        setSelectedPlayerImage();
    }

    private void setSelectedPlayerImage() {
        Image image = Utils.getInstance().getMainSpriteImage(characterComboBox.getSelectedItem().toString());
        image = image.getScaledInstance(100,100, Image.SCALE_SMOOTH);
        playerIcon.setIcon(new ImageIcon(image));
    }

    private void onOK() {
        GameBoard.getInstance().setLocalPlayer(nameTextField.getText(),
                characterComboBox.getSelectedItem().toString());
        frame.dispose();
    }

    private void onCancel() {
        frame.dispose();
    }
}