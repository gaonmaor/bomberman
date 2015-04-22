package bomberman.ui;

import bomberman.entities.GameBoard;
import bomberman.utils.Defaults;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectMenu extends JPanel {
    private static SelectMenu currentSelectMenu;
    private Component mainWindow;
    private JButton btnAbout;
    private JButton btnEdit;
    private JButton btnExit;
    private JButton btnHelp;
    private JButton btnHost;
    private JButton btnJoin;

    public SelectMenu(Component mainWindow) {
        setCurrentSelectMenu(this);
        setSize(120, 215);
        setMainWindow(mainWindow);
        setLocation(mainWindow.getWidth() - 130, 1);
        initComponents();
    }

    public static SelectMenu getCurrentSelectMenu() {
        return currentSelectMenu;
    }

    public static void setCurrentSelectMenu(SelectMenu aCurrentSelectMenu) {
        currentSelectMenu = aCurrentSelectMenu;
    }

    public JButton getBtnAbout() {
        return this.btnAbout;
    }

    public void setBtnAbout(JButton btnAbout) {
        this.btnAbout = btnAbout;
    }

    public JButton getBtnEdit() {
        return this.btnEdit;
    }

    public void setBtnEdit(JButton btnEdit) {
        this.btnEdit = btnEdit;
    }

    public JButton getBtnExit() {
        return this.btnExit;
    }

    public void setBtnExit(JButton btnExit) {
        this.btnExit = btnExit;
    }

    public JButton getBtnHelp() {
        return this.btnHelp;
    }

    public void setBtnHelp(JButton btnHelp) {
        this.btnHelp = btnHelp;
    }

    public JButton getBtnHost() {
        return this.btnHost;
    }

    public void setBtnHost(JButton btnHost) {
        this.btnHost = btnHost;
    }

    public JButton getBtnJoin() {
        return this.btnJoin;
    }

    public void setBtnJoin(JButton btnJoin) {
        this.btnJoin = btnJoin;
    }

    public Component getMainWindow() {
        return this.mainWindow;
    }

    public void setMainWindow(Component mainWindow) {
        this.mainWindow = mainWindow;
    }

    private JButton createButton(String title, ActionListener actionListener)
    {
        JButton button = new JButton();
        button.setBackground(Defaults.BUTTON_COLOR);
        button.setText(title);
        button.setEnabled(true);
        button.addActionListener(actionListener);
        return button;
    }

    private void initComponents() {
        setBackground(Defaults.SELECTED_MENU_COLOR);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        this.btnJoin = createButton("Join Game", SelectMenu.this::btnJoinActionPerformed);
        this.btnHost = createButton("Host Game", SelectMenu.this::btnHostActionPerformed);
        this.btnEdit = createButton("Edit Player", SelectMenu.this::btnEditActionPerformed);
        this.btnExit = createButton("Exit", SelectMenu.this::btnExitActionPerformed);
        this.btnHelp = createButton("Help", SelectMenu.this::btnHelpActionPerformed);
        this.btnAbout = createButton("About", SelectMenu.this::btnAboutActionPerformed);
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        GroupLayout.ParallelGroup paraGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        paraGroup.addComponent(this.btnAbout, Defaults.MIN_COMPONENT_SIZE, Defaults.BUTTON_SIZE, Defaults.MAX_COMPONENT_SIZE);
        paraGroup.addComponent(this.btnHelp, Defaults.MIN_COMPONENT_SIZE, Defaults.BUTTON_SIZE, Defaults.MAX_COMPONENT_SIZE);
        paraGroup.addComponent(this.btnHost, Defaults.MIN_COMPONENT_SIZE, Defaults.BUTTON_SIZE, Defaults.MAX_COMPONENT_SIZE);
        paraGroup.addComponent(this.btnJoin, Defaults.MIN_COMPONENT_SIZE, Defaults.BUTTON_SIZE, Defaults.MAX_COMPONENT_SIZE);
        paraGroup.addComponent(this.btnEdit, Defaults.MIN_COMPONENT_SIZE, Defaults.BUTTON_SIZE, Defaults.MAX_COMPONENT_SIZE);
        paraGroup.addComponent(this.btnExit, Defaults.MIN_COMPONENT_SIZE, Defaults.BUTTON_SIZE, Defaults.MAX_COMPONENT_SIZE);
        GroupLayout.SequentialGroup sequentGroup = layout.createSequentialGroup();
        sequentGroup.addContainerGap();
        sequentGroup.addGroup(paraGroup);
        sequentGroup.addContainerGap();
        GroupLayout.Group group = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        group.addGroup(sequentGroup);
        layout.setHorizontalGroup(group);

        sequentGroup = layout.createSequentialGroup();
        sequentGroup.addContainerGap();
        sequentGroup.addComponent(this.btnJoin);
        sequentGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        sequentGroup.addComponent(this.btnHost);
        sequentGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        sequentGroup.addComponent(this.btnEdit);
        sequentGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        sequentGroup.addComponent(this.btnHelp);
        sequentGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        sequentGroup.addComponent(this.btnAbout);
        sequentGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        sequentGroup.addComponent(this.btnExit);
        sequentGroup.addContainerGap(Defaults.BUTTON_SIZE, Defaults.MAX_COMPONENT_SIZE);
        paraGroup = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
        paraGroup.addGroup(sequentGroup);
        layout.setVerticalGroup(paraGroup);
        //layout.setHorizontalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(1).add(this.btnAbout1, -1, 85, 32767).add(2, this.btnHelp, -1, 85, 32767).add(this.btnHost, -1, -1, 32767).add(this.btnJoin, -1, 85, 32767).add(this.btnEdit, -1, 85, 32767).add(2, this.btnExit, -1, 85, 32767)).addContainerGap()));
        //layout.setVerticalGroup(layout.createParallelGroup(1).add(layout.createSequentialGroup().addContainerGap().add(this.btnJoin).addPreferredGap(0).add(this.btnHost).addPreferredGap(0).add(this.btnEdit).addPreferredGap(0).add(this.btnHelp).addPreferredGap(0).add(this.btnAbout1).addPreferredGap(0).add(this.btnExit).addContainerGap(85, 32767)));
    }

    private void btnAboutActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(null, "Made by Maor Gaon");
    }

    private void btnHelpActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(null, "Use the arrows to move and \nspace to place a bomb, \nfor the other player: use (a,s,d,w)\n to move and g to place a bomb.");
    }

    private void btnHostActionPerformed(ActionEvent evt) {
        SelectMap selectMap = new SelectMap();
        Component component = GameBoard.getInstance().getMainWindow();
        if ((component instanceof JFrame)) {
            JFrame jFrame = (JFrame) component;
            jFrame.getContentPane().add(selectMap, 0);
            selectMap.updateUI();
            jFrame.repaint();
        }
        if ((component instanceof JApplet)) {
            JApplet jApplet = (JApplet) component;
            jApplet.getContentPane().add(selectMap, 0);
            selectMap.updateUI();
            jApplet.repaint();
        }
    }

    private void btnEditActionPerformed(ActionEvent evt) {
        SelectPlayer selectPlayer = new SelectPlayer();
        Component component = GameBoard.getInstance().getMainWindow();
        if ((component instanceof JFrame)) {
            JFrame jFrame = (JFrame) component;
            jFrame.getContentPane().add(selectPlayer, 0);
            selectPlayer.updateUI();
            jFrame.repaint();
        }
        if ((component instanceof JApplet)) {
            JApplet jApplet = (JApplet) component;
            jApplet.getContentPane().add(selectPlayer, 0);
            selectPlayer.updateUI();
            jApplet.repaint();
        }
    }

    private void btnJoinActionPerformed(ActionEvent evt) {
        JoinGamePanel joinGamePanel = new JoinGamePanel();
        Component c = GameBoard.getInstance().getMainWindow();
        if ((c instanceof JFrame)) {
            JFrame jFrame = (JFrame) c;
            jFrame.getContentPane().add(joinGamePanel, 0);
            joinGamePanel.updateUI();
            jFrame.repaint();
        }
        if ((c instanceof JApplet)) {
            JApplet jApplet = (JApplet) c;
            jApplet.getContentPane().add(joinGamePanel, 0);
            joinGamePanel.updateUI();
            jApplet.repaint();
        }
    }

    private void btnExitActionPerformed(ActionEvent evt) {
        Component c = GameBoard.getInstance().getMainWindow();
        if ((c instanceof JFrame)) {
            System.exit(0);
        }
        if ((c instanceof JApplet)) {
            ((MainApplet)c).close();
        }
    }
}