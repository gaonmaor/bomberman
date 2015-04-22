package bomberman.ui;

import bomberman.utils.Defaults;

import javax.swing.*;
import java.awt.*;

public class MessagePanel
        extends JPanel {
    public static JLabel lblMessage;

    public MessagePanel(Component mainWindow) {
        setSize(600, 40);
        setLocation((mainWindow.getWidth() - getWidth()) / 2, (mainWindow.getHeight() + 600 - getHeight()) / 2);
        initComponents();
    }

    private void initComponents() {
        lblMessage = new JLabel();
        setBackground(new Color(204, 204, 204));
        lblMessage.setBackground(new Color(204, 204, 204));
        lblMessage.setFont(new Font("Arial", 1, 18));
        lblMessage.setHorizontalAlignment(0);
        lblMessage.setBorder(BorderFactory.createBevelBorder(0));
        lblMessage.setHorizontalTextPosition(0);
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        GroupLayout.ParallelGroup paraGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        paraGroup.addComponent(lblMessage, GroupLayout.Alignment.CENTER, Defaults.MIN_COMPONENT_SIZE, 400, Defaults.MAX_COMPONENT_SIZE);
        layout.setHorizontalGroup(paraGroup);
        paraGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        paraGroup.addComponent(lblMessage, Defaults.MIN_COMPONENT_SIZE, 42, Defaults.MAX_COMPONENT_SIZE);
        layout.setVerticalGroup(paraGroup);

        //layout.setHorizontalGroup(layout.createParallelGroup(1).add(2, lblMessage, -1, 400, 32767));
        //layout.setVerticalGroup(layout.createParallelGroup(1).add(lblMessage, -1, 42, 32767));
    }
}